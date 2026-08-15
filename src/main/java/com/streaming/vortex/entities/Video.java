package com.streaming.vortex.entities;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "videos")
public class Video {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true, length = 1000)
	private String filePath;

	@Column(nullable = false)
	private Long size;

	private Long duration;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModified;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastScan;

	@Column(length = 1000)
	private String thumbnailPath;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "artist_id")
	private Artist artist;

	@OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<PreviewImage> previews = new ArrayList<>();
	

	@OneToMany(mappedBy = "video")
	private List<LikedVideo> likes = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFilePath() {
		return filePath;
	}

	public Long getSize() {
		return size;
	}

	public Long getDuration() {
		return duration;
	}

	public Date getLastModified() {
		return lastModified;
	}

	public Date getLastScan() {
		return lastScan;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public Artist getArtist() {
		return artist;
	}

	public List<PreviewImage> getPreviews() {
		return previews;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}

	public void setLastScan(Date lastScan) {
		this.lastScan = lastScan;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public void setArtist(Artist artist) {
		this.artist = artist;
	}

	public void setPreviews(List<PreviewImage> previews) {
		this.previews = previews;
	}
	public List<LikedVideo> getLikes() {
		return likes;
	}

	public void setLikes(List<LikedVideo> likes) {
		this.likes = likes;
	}


}