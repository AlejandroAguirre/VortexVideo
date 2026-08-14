package com.streaming.vortex.services;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface ThumbnailEventService {

	SseEmitter subscribe();

	void notifyFinished(int total);

}
