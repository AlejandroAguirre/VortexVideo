package com.streaming.vortex.model.dto;

public class ContinueWatchingRequest {

	private Long videoId;

	private Long currentSecond;

	private Long duration;

	public Long getVideoId() {
		return videoId;
	}

	public Long getCurrentSecond() {
		return currentSecond;
	}

	public Long getDuration() {
		return duration;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public void setCurrentSecond(Long currentSecond) {
		this.currentSecond = currentSecond;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

}