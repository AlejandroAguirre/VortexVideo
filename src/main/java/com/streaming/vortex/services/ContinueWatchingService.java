package com.streaming.vortex.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.streaming.vortex.model.dto.ContinueWatchingRequest;
import com.streaming.vortex.model.dto.VideoDTO;

public interface ContinueWatchingService {

	void saveProgress(ContinueWatchingRequest request);

	void remove(Long videoId);

	Page<VideoDTO> getContinueWatching(Pageable pageable);

}