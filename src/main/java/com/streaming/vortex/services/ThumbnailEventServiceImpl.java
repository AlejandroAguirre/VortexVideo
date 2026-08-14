package com.streaming.vortex.services;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import jakarta.annotation.PreDestroy;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class ThumbnailEventServiceImpl implements ThumbnailEventService {

	private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

	@Override
	public SseEmitter subscribe() {
		SseEmitter emitter = new SseEmitter(0L);
		emitters.add(emitter);
		emitter.onCompletion(() -> {
			emitters.remove(emitter);
			log.debug("SSE connection completed");
		});

		emitter.onTimeout(() -> {
			emitters.remove(emitter);
			log.debug("SSE connection timeout");
		});

		emitter.onError(e -> {
			emitters.remove(emitter);
			log.debug("SSE connection error: {}", e.getMessage());
		});

		return emitter;
	}

	@Override
	public void notifyFinished(int total) {
		for (SseEmitter emitter : emitters) {
			try {
				emitter.send(SseEmitter.event().name("thumbnails-finished").data(total));

			} catch (Exception e) {
				log.debug("SSE client disconnected: {}", e.getMessage());
				emitters.remove(emitter);
			}
		}
	}

	@PreDestroy
	public void shutdown() {
		for (SseEmitter emitter : emitters) {
			try {
				emitter.complete();
			} catch (Exception e) {
				log.debug("Error closing SSE emitter: {}", e.getMessage());
			}
		}
		emitters.clear();
	}
}