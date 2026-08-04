package com.streaming.vortex.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.streaming.vortex.entities.LikedVideo;

public interface LikedVideoRepository extends JpaRepository<LikedVideo, Long> {

	Page<LikedVideo> findAllByOrderByLikedAtDesc(Pageable pageable);

	boolean existsByVideoId(Long videoId);

	void deleteByVideoId(Long videoId);
	
	
}