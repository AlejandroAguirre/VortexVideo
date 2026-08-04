package com.streaming.vortex.controller;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.streaming.vortex.model.dto.ContinueWatchingRequest;
import com.streaming.vortex.model.dto.VideoDTO;
import com.streaming.vortex.services.ContinueWatchingService;
import com.streaming.vortex.services.ThumbnailService;

@RestController
@RequestMapping("/api/vortex/continue")
@CrossOrigin(origins = "*")
public class ContinueWatchingController {

	private final ContinueWatchingService service;
	private final ThumbnailService thumbnailService;

	public ContinueWatchingController(ContinueWatchingService service, ThumbnailService thumbnailService) {
		this.service = service;
		this.thumbnailService = thumbnailService;
	}

	@GetMapping
	public Page<VideoDTO> continueWatching(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		return service.getContinueWatching(PageRequest.of(page, size));
	}

	@PostMapping
	public void save(@RequestBody ContinueWatchingRequest request) {
		service.saveProgress(request);
	}

	@DeleteMapping("/{id}")
	public void remove(@PathVariable Long id) {
		service.remove(id);
	}

	@GetMapping("/continue-preview")
	public ResponseEntity<Resource> continuePreview(@RequestParam Long id) {
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(thumbnailService.getContinuePreview(id));
	}

}