package com.streaming.vortex.services;

import java.io.File;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streaming.vortex.entities.Artist;
import com.streaming.vortex.entities.Video;
import com.streaming.vortex.repository.ArtistRepository;
import com.streaming.vortex.repository.PreviewImageRepository;
import com.streaming.vortex.repository.VideoRepository;

@Service
public class ScanServiceImpl implements ScanService {

	private final ArtistRepository artistRepository;
	private final VideoRepository videoRepository;
	private final String root;


	public ScanServiceImpl(ArtistRepository artistRepository, VideoRepository videoRepository,
			PreviewImageRepository previewRepository, @Value("${media.scan.root}") String root) {
		this.artistRepository = artistRepository;
		this.videoRepository = videoRepository;
		this.root = root;
	}

	@Override
	@Transactional
	public void scanLibrary() throws Exception {
		File root = new File(this.root);
		if (!root.exists()) {
			throw new RuntimeException("No existe la carpeta");
		}
		File[] artists = root.listFiles(File::isDirectory);
		if (artists == null)
			return;
		for (File folder : artists) {
			Artist artist = processArtist(folder);
			scanVideos(folder, artist);
		}
	}

	private Artist processArtist(File folder) {
		Artist artist = artistRepository.findByFolderPath(folder.getAbsolutePath()).orElse(new Artist());
		artist.setName(folder.getName());
		artist.setFolderPath(folder.getAbsolutePath());
		artist.setLastScan(new Date());
		return artistRepository.save(artist);
	}

	private void scanVideos(File folder, Artist artist) throws Exception {
		File[] videos = folder.listFiles(f -> f.getName().toLowerCase().endsWith(".mp4"));
		if (videos == null)
			return;
		for (File file : videos) {
			Video video = videoRepository.findByFilePath(file.getAbsolutePath()).orElse(new Video());
			video.setName(file.getName());
			video.setFilePath(file.getAbsolutePath());
			video.setSize(file.length());
			video.setLastModified(new Date(file.lastModified()));
			video.setLastScan(new Date());

			video.setArtist(artist);
			video = videoRepository.save(video);
		}
	}
}