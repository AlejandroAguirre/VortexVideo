package com.streaming.vortex.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.streaming.vortex.services.ThumbnailEventService;

@CrossOrigin(origins = { "*" })

@RestController
@RequestMapping("/api/vortex")
public class ThumbnailEventController {

	private final ThumbnailEventService thumbnailEventService;

	public ThumbnailEventController(ThumbnailEventService thumbnailEventService) {
		this.thumbnailEventService = thumbnailEventService;
	}

	@GetMapping(value = "/thumbnails/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter subscribe() {
		return thumbnailEventService.subscribe();
	}
}