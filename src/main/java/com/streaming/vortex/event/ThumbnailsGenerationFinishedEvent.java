package com.streaming.vortex.event;

public class ThumbnailsGenerationFinishedEvent {
	private final int total;

	public ThumbnailsGenerationFinishedEvent(int total) {
		this.total = total;
	}

	public int getTotal() {
		return total;
	}
}
