package com.streaming.vortex.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.streaming.vortex.repository.ArtistRepository;
import com.streaming.vortex.services.ThumbnailService;
import com.streaming.vortex.services.VideoService;

@CrossOrigin(origins = { "*" })
@RestController
@RequestMapping("/api/vortex")
public class ResourcesController {

	private final VideoService videoService;
	private final ThumbnailService thumbnailService;

	public ResourcesController(ArtistRepository artistRepository, VideoService VideoService,
			ThumbnailService thumbnailService) {
		this.videoService = VideoService;
		this.thumbnailService = thumbnailService;
	}

	@GetMapping("/previews")
	public List<String> previews(@RequestParam Long path) {
		return videoService.getPreviewUrls(path);
	}

	@GetMapping("/thumbnail")
	public ResponseEntity<Resource> getThumbnail(@RequestParam Long path) {
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(thumbnailService.getThumbnail(path));
	}

	@GetMapping("/preview")
	public ResponseEntity<Resource> preview(@RequestParam Long id) {
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(thumbnailService.getPreview(id));
	}

}
