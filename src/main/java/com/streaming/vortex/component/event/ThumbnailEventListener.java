package com.streaming.vortex.component.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.streaming.vortex.event.ThumbnailsGenerationFinishedEvent;
import com.streaming.vortex.services.ThumbnailEventService;

@Component
public class ThumbnailEventListener {

	private final ThumbnailEventService thumbnailEventService;

	public ThumbnailEventListener(ThumbnailEventService thumbnailEventService) {
		this.thumbnailEventService = thumbnailEventService;
	}

	@EventListener
	public void handleThumbnailsFinished(ThumbnailsGenerationFinishedEvent event) {
		thumbnailEventService.notifyFinished(event.getTotal());
	}
}