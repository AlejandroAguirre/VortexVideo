package com.streaming.vortex.services;

import org.springframework.core.io.Resource;

import com.streaming.vortex.entities.Video;

public interface ThumbnailService {

	void generateAll();

	void generateImages(Video video) throws Exception;

	Resource getThumbnail(Long videoId);

	Resource getPreview(Long id);

	String generateContinueFrame(Video video, Long currentSecond);

	Resource getContinuePreview(Long id);
}
