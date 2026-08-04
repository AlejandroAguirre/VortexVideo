package com.streaming.vortex.entities;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "continue_watching")
public class ContinueWatching {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_id", nullable = false, unique = true)
	private Video video;

	private Long currentSecond;

	private Long duration;

	@Column(length = 1000)
	private String previewPath;

	@Temporal(TemporalType.TIMESTAMP)
	private Date updatedAt = new Date();

	public Long getId() {
		return id;
	}

	public Video getVideo() {
		return video;
	}

	public Long getCurrentSecond() {
		return currentSecond;
	}

	public Long getDuration() {
		return duration;
	}

	public String getPreviewPath() {
		return previewPath;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

	public void setCurrentSecond(Long currentSecond) {
		this.currentSecond = currentSecond;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public void setPreviewPath(String previewPath) {
		this.previewPath = previewPath;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

}