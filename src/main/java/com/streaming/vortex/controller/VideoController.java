package com.streaming.vortex.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.support.ResourceRegion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.streaming.vortex.model.dto.VideoDTO;
import com.streaming.vortex.services.VideoService;

import lombok.extern.slf4j.Slf4j;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/vortex")
@Slf4j
public class VideoController {

	private final VideoService videoService;

	public VideoController(VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping("/recent")
	public ResponseEntity<Page<VideoDTO>> recent(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "11") int size) {
		log.info("Solicitud de videos recientes - página: {}, tamaño: {}", page, size);
		Page<VideoDTO> videos = videoService.getRecentVideos(PageRequest.of(page, size));
		log.info("Videos recientes encontrados: {}", videos.getTotalElements());
		return ResponseEntity.ok(videos);
	}

	@GetMapping("/videos")
	public List<VideoDTO> listVideos(@RequestParam(defaultValue = "") String path) {
		log.info("Solicitud de listado de videos - ruta: {}", path);
		List<VideoDTO> videos = videoService.listVideos(path);
		log.info("Videos encontrados: {}", videos.size());
		return videos;
	}

	@GetMapping("/search")
	public ResponseEntity<Page<VideoDTO>> search(@RequestParam String q, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		log.info("Búsqueda de videos - criterio: '{}', página: {}, tamaño: {}", q, page, size);
		Page<VideoDTO> result = videoService.search(q, PageRequest.of(page, size));
		log.info("Resultados encontrados: {}", result.getTotalElements());
		return ResponseEntity.ok(result);
	}

	@GetMapping("/video/{filename}")
	public ResponseEntity<ResourceRegion> streamVideo(@PathVariable Long filename, @RequestHeader HttpHeaders headers)
			throws IOException {
		log.info("Solicitud de reproducción de video - id: {}", filename);
		ResourceRegion region = videoService.getVideoRegion(filename, headers);
		MediaType mediaType = MediaTypeFactory.getMediaType(region.getResource().getFilename())
				.orElse(MediaType.APPLICATION_OCTET_STREAM);
		log.info("Streaming iniciado - video: {}, tipo: {}", filename, mediaType);
		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline").header(HttpHeaders.ACCEPT_RANGES, "bytes")
				.body(region);
	}

	@GetMapping("/favorites")
	public ResponseEntity<Page<VideoDTO>> favorites(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		log.info("Solicitud de videos favoritos - página: {}, tamaño: {}", page, size);
		Page<VideoDTO> favorites = videoService.getFavorites(PageRequest.of(page, size));
		log.info("Videos favoritos encontrados: {}", favorites.getTotalElements());
		return ResponseEntity.ok(favorites);
	}

	@PostMapping("/like/{id}")
	public void like(@PathVariable Long id) {
		log.info("Marcando video como favorito - id: {}", id);
		videoService.like(id);
		log.info("Video marcado como favorito correctamente - id: {}", id);
	}

	@DeleteMapping("/like/{id}")
	public void unlike(@PathVariable Long id) {
		log.info("Eliminando video de favoritos - id: {}", id);
		videoService.unlike(id);
		log.info("Video eliminado de favoritos correctamente - id: {}", id);
	}

}