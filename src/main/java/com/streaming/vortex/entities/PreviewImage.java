package com.streaming.vortex.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "preview_images")
public class PreviewImage {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Integer position;

	@Column(name="image_path" ,length = 1000)
	private String imagePath;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "video_id")
	private Video video;

	public Long getId() {
		return id;
	}

	public Integer getPosition() {
		return position;
	}

	public String getImagePath() {
		return imagePath;
	}

	public Video getVideo() {
		return video;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public void setVideo(Video video) {
		this.video = video;
	}

}