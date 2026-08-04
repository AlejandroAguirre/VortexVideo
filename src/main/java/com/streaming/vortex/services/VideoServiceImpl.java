package com.streaming.vortex.services;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.ResourceRegion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.streaming.vortex.entities.Artist;
import com.streaming.vortex.entities.LikedVideo;
import com.streaming.vortex.entities.PreviewImage;
import com.streaming.vortex.entities.Video;
import com.streaming.vortex.mapper.VideoMapper;
import com.streaming.vortex.model.dto.VideoDTO;
import com.streaming.vortex.repository.ArtistRepository;
import com.streaming.vortex.repository.LikedVideoRepository;
import com.streaming.vortex.repository.VideoRepository;

@Service
public class VideoServiceImpl implements VideoService {

	private final VideoRepository videoRepository;
	private final ArtistRepository artistRepository;
	private final LikedVideoRepository likedRepository;
	private final VideoMapper videoMapper;

	public VideoServiceImpl(VideoRepository videoRepository, ArtistRepository artistRepository,
			LikedVideoRepository likedRepository, VideoMapper videoMapper) {
		this.videoRepository = videoRepository;
		this.artistRepository = artistRepository;
		this.likedRepository = likedRepository;
		this.videoMapper = videoMapper;
	}

	@Override
	public Page<VideoDTO> search(String text, Pageable pageable) {
		if (text == null || text.trim().isEmpty()) {
			return Page.empty();
		}
		return videoRepository.search(text.trim(), pageable).map(videoMapper::videoToFileDTO);
	}

	@Override
	public List<String> getPreviewUrls(Long videoId) {
		Video video = videoRepository.findById(videoId).orElseThrow(() -> new RuntimeException("Video not found"));

		return video.getPreviews().stream().sorted(Comparator.comparing(PreviewImage::getPosition))
				.map(preview -> "/api/vortex/preview?id=" + preview.getId()).collect(Collectors.toList());
	}

	@Override
	public ResourceRegion getVideoRegion(Long id, HttpHeaders headers) throws IOException {
		Video videoEntity = videoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		File videoFile = new File(videoEntity.getFilePath());
		if (!videoFile.exists()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		UrlResource video = new UrlResource(videoFile.toURI());
		long contentLength = video.contentLength();

		if (headers.getRange().isEmpty()) {
			return new ResourceRegion(video, 0, contentLength);
		}
		HttpRange range = headers.getRange().get(0);
		long start = range.getRangeStart(contentLength);
		long end = range.getRangeEnd(contentLength);
		long rangeLength = Math.min(1024 * 1024, end - start + 1);

		return new ResourceRegion(video, start, rangeLength);
	}

	@Override
	public List<VideoDTO> listVideos(String path) {

		if (path == null || path.trim().isEmpty()) {
			return artistRepository.findRandomArtists().stream().map(videoMapper::artistToFolderDTO)
					.collect(Collectors.toList());
		}
		Artist artist = artistRepository.findByName(path).orElseThrow(() -> new RuntimeException("Artist not found"));
		return artist.getVideos().stream().map(videoMapper::videoToFileDTO).collect(Collectors.toList());
	}

	@Override
	public Page<VideoDTO> getRecentVideos(Pageable pageable) {
		return videoRepository.findAllByOrderByLastModifiedDesc(pageable).map(videoMapper::videoToDTO);
	}

	@Override
	@Transactional
	public void like(Long id) {
		Video video = videoRepository.findById(id).orElseThrow(() -> new RuntimeException("Video no encontrado"));
		if (!likedRepository.existsByVideoId(id)) {
			LikedVideo like = new LikedVideo();
			like.setVideo(video);
			likedRepository.save(like);
		}
	}

	@Transactional
	@Override
	public void unlike(Long id) {
		likedRepository.deleteByVideoId(id);

	}

	@Override
	public Page<VideoDTO> getFavorites(Pageable pageable) {
		return likedRepository.findAllByOrderByLikedAtDesc(pageable).map(LikedVideo::getVideo)
				.map(videoMapper::videoToDTO);
	}

}
