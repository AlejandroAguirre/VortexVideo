package com.streaming.vortex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.streaming.vortex.entities.PreviewImage;
import com.streaming.vortex.entities.Video;

public interface PreviewImageRepository extends JpaRepository<PreviewImage, Long> {

	List<PreviewImage> findByVideoOrderByPosition(Video video);

	void deleteByVideo(Video video);

	Optional<PreviewImage> findByVideoIdAndPosition(Long videoId, Integer position);

	long countByVideoId(Long id);

}