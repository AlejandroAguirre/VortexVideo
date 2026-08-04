package com.streaming.vortex.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "artists")
public class Artist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String name;

	@Column(nullable = false)
	private String folderPath;

	private Date lastScan;

	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Video> videos = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public Date getLastScan() {
		return lastScan;
	}

	public List<Video> getVideos() {
		return videos;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public void setLastScan(Date lastScan) {
		this.lastScan = lastScan;
	}

	public void setVideos(List<Video> videos) {
		this.videos = videos;
	}

}