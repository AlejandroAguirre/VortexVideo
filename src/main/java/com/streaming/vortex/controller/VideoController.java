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

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/vortex")
public class VideoController {

	private final VideoService videoService;

	public VideoController(VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping("/recent")
	public ResponseEntity<Page<VideoDTO>> recent(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "11") int size) {
		return ResponseEntity.ok(videoService.getRecentVideos(PageRequest.of(page, size)));
	}

	@GetMapping("/videos")
	public List<VideoDTO> listVideos(@RequestParam(defaultValue = "") String path) {
		return videoService.listVideos(path);
	}

	@GetMapping("/search")
	public ResponseEntity<Page<VideoDTO>> search(@RequestParam String q, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(videoService.search(q, PageRequest.of(page, size)));
	}

	@GetMapping("/video/{filename}")
	public ResponseEntity<ResourceRegion> streamVideo(@PathVariable Long filename, @RequestHeader HttpHeaders headers)
			throws IOException {

		ResourceRegion region = videoService.getVideoRegion(filename, headers);
		MediaType mediaType = MediaTypeFactory.getMediaType(region.getResource().getFilename())
				.orElse(MediaType.APPLICATION_OCTET_STREAM);

		return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).contentType(mediaType)
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline").header(HttpHeaders.ACCEPT_RANGES, "bytes")
				.body(region);
	}

	@GetMapping("/favorites")
	public ResponseEntity<Page<VideoDTO>> favorites(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {
		return ResponseEntity.ok(videoService.getFavorites(PageRequest.of(page, size)));
	}

	@PostMapping("/like/{id}")
	public void like(@PathVariable Long id) {
		videoService.like(id);
	}

	@DeleteMapping("/like/{id}")
	public void unlike(@PathVariable Long id) {
		videoService.unlike(id);
	}

}