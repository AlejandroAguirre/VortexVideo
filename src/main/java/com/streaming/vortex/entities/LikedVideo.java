package com.streaming.vortex.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "liked_videos")
public class LikedVideo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Date likedAt = new Date();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_id", nullable = false)
	private Video video;

	public Long getId() {
		return id;
	}

	public Date getLikedAt() {
		return likedAt;
	}

	public Video getVideo() {
		return video;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLikedAt(Date likedAt) {
		this.likedAt = likedAt;
	}

	public void setVideo(Video video) {
		this.video = video;
	}
}