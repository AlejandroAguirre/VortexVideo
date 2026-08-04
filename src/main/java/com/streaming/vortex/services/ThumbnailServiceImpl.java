package com.streaming.vortex.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.streaming.vortex.entities.PreviewImage;
import com.streaming.vortex.entities.Video;
import com.streaming.vortex.repository.PreviewImageRepository;
import com.streaming.vortex.repository.VideoRepository;

@Service
public class ThumbnailServiceImpl implements ThumbnailService {

	private final VideoRepository videoRepository;
	private final PreviewImageRepository previewRepository;
	private final String thumbRoot;

	public ThumbnailServiceImpl(VideoRepository videoRepository, PreviewImageRepository previewRepository,
			@Value("${media.thumbnail.root}") String thumbRoot) {
		this.videoRepository = videoRepository;
		this.previewRepository = previewRepository;
		this.thumbRoot = thumbRoot;
	}

	@Override
	@Async("scanExecutor")
	public void generateAll() {
		List<Video> videos = videoRepository.findByThumbnailPathIsNull();
		for (Video video : videos) {
			try {
				generateImages(video);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void generateImages(Video video) throws Exception {
		File root = new File(this.thumbRoot);
		if (!root.exists()) {
			boolean created = root.mkdirs();
			System.out.println("Creando carpeta thumbnails: " + created);
		}
		File thumb = new File(root, video.getId() + "_thumb.jpg");
		if (!thumb.exists()) {
			executeFFmpegThumbnail(video.getFilePath(), thumb);
		}
		if (!thumb.exists() || thumb.length() < 1000) {
			throw new RuntimeException("No se pudo generar thumbnail: " + video.getName());
		}
		video.setThumbnailPath(thumb.getAbsolutePath());
		videoRepository.save(video);
		generatePreviewFrames(video);
	}

	@Override
	public Resource getThumbnail(Long videoId) {
		Video video = videoRepository.findById(videoId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		return new FileSystemResource(video.getThumbnailPath());
	}

	@Override
	public Resource getPreview(Long id) {
		PreviewImage img = previewRepository.findById(id).orElseThrow(() -> new RuntimeException("Preview not found"));

		return new FileSystemResource(img.getImagePath());
	}

	private void executeFFmpegThumbnail(String input, File output) throws Exception {
		String[] cmd = { "ffmpeg", "-y", "-i", input, "-ss", "00:00:15", "-frames:v", "1", "-update", "1",
				output.getAbsolutePath() };
		Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println("FFMPEG: " + line);
			}
		}
		int exit = p.waitFor();
		System.out.println("FFMPEG EXIT: " + exit);
	}

	private void generatePreviewFrames(Video video) throws Exception {
		long count = previewRepository.countByVideoId(video.getId());
		if (count >= 5) {
			return;
		}
		File folder = new File(this.thumbRoot + "\\previews\\" + video.getId());
		folder.mkdirs();
		double duration = getVideoDuration(video.getFilePath());
		double start = 15; // segundos iniciales que se ignoran
		double end = duration - 10; // últimos 10 segundos que se ignoran
		// Si el video es muy corto, usamos todo el video
		if (end <= start) {
			start = 0;
			end = duration;
		}
		int totalFrames = 5;
		for (int i = 1; i <= totalFrames; i++) {
			File frame = new File(folder, i + ".jpg");
			double second = start + ((end - start) * i / (totalFrames + 1));
			if (!frame.exists()) {
				executeFFmpegFrame(video.getFilePath(), frame, second);
			}
			PreviewImage img = previewRepository.findByVideoIdAndPosition(video.getId(), i).orElse(new PreviewImage());
			img.setPosition(i);
			img.setImagePath(frame.getAbsolutePath());
			img.setVideo(video);
			previewRepository.save(img);
		}
	}

	private double getVideoDuration(String input) throws Exception {
		String[] cmd = { "ffprobe", "-v", "error", "-show_entries", "format=duration", "-of",
				"default=noprint_wrappers=1:nokey=1", input };
		Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
		String duration;
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
			duration = reader.readLine();
		}
		int exit = p.waitFor();
		if (exit != 0 || duration == null) {
			throw new RuntimeException("No se pudo obtener duración del video");
		}
		return Double.parseDouble(duration);
	}

	private void executeFFmpegFrame(String input, File output, double second) throws Exception {
		String time = String.format(Locale.US, "%.3f", second);
		String[] cmd = { "ffmpeg", "-y", "-ss", time, "-i", input, "-frames:v", "1", "-q:v", "2", "-update", "1",
				output.getAbsolutePath() };
		Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
			while (reader.readLine() != null) {
				// consumir salida FFmpeg
			}
		}
		int exit = p.waitFor();
		if (exit != 0) {
			throw new RuntimeException("FFmpeg frame falló código " + exit);
		}
	}

	@Override
	public String generateContinueFrame(Video video, Long currentSecond) {
		try {
			File folder = new File(thumbRoot, "continue");
			if (!folder.exists()) {
				folder.mkdirs();
			}
			File output = new File(folder, video.getId() + ".jpg");
			executeFFmpegFrame(video.getFilePath(), output, currentSecond.doubleValue());
			return output.getAbsolutePath();
		} catch (Exception e) {
			throw new RuntimeException("No se pudo generar imagen continuar viendo", e);
		}
	}

	@Override
	public Resource getContinuePreview(Long id) {
		File file = new File(thumbRoot + "\\continue\\" + id + ".jpg");
		if (!file.exists()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Imagen de continuar viendo no encontrada");
		}
		return new FileSystemResource(file);
	}
}