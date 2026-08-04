package com.streaming.vortex.services;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.support.ResourceRegion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;

import com.streaming.vortex.model.dto.VideoDTO;

public interface VideoService {

	List<String> getPreviewUrls(Long videoId);

	ResourceRegion getVideoRegion(Long id, HttpHeaders headers) throws IOException;

	List<VideoDTO> listVideos(String path);

	void like(Long id);

	void unlike(Long id);

	Page<VideoDTO> getRecentVideos(Pageable pageable);

	Page<VideoDTO> getFavorites(Pageable pageable);

	Page<VideoDTO> search(String text, Pageable pageable);

}
