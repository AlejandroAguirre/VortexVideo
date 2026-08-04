package com.streaming.vortex.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.streaming.vortex.entities.ContinueWatching;

public interface ContinueWatchingRepository extends JpaRepository<ContinueWatching, Long> {

	Optional<ContinueWatching> findByVideoId(Long videoId);

    @Query(
            value = "SELECT cw FROM ContinueWatching cw ORDER BY cw.updatedAt DESC",
            countQuery = "SELECT COUNT(cw) FROM ContinueWatching cw"
        )
		Page<ContinueWatching> findAllOrdered(Pageable pageable);

	void deleteByVideoId(Long videoId);
}