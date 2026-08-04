package com.streaming.vortex.model.dto;

import java.util.List;

public class VideoDTO {

	private String name;

	private String path;

	private String type;

	private boolean liked;

	private List<String> thumbnails;

	private Long currentSecond;

	private Long duration;

	private Integer progress;

	private String continueImage;

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getType() {
		return type;
	}

	public boolean isLiked() {
		return liked;
	}

	public List<String> getThumbnails() {
		return thumbnails;
	}

	public Long getCurrentSecond() {
		return currentSecond;
	}

	public Long getDuration() {
		return duration;
	}

	public Integer getProgress() {
		return progress;
	}

	public String getContinueImage() {
		return continueImage;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setLiked(boolean liked) {
		this.liked = liked;
	}

	public void setThumbnails(List<String> thumbnails) {
		this.thumbnails = thumbnails;
	}

	public void setCurrentSecond(Long currentSecond) {
		this.currentSecond = currentSecond;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public void setContinueImage(String continueImage) {
		this.continueImage = continueImage;
	}

}