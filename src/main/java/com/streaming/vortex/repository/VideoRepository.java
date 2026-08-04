package com.streaming.vortex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.streaming.vortex.entities.Artist;
import com.streaming.vortex.entities.Video;

public interface VideoRepository extends JpaRepository<Video, Long> {

	Optional<Video> findByFilePath(String filePath);

	List<Video> findByArtist(Artist artist);

	boolean existsByFilePath(String filePath);

	List<Video> findByThumbnailPathIsNull();
	
	@Query("SELECT v " +
		       "FROM Video v " +
		       "WHERE LOWER(v.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
		       "OR LOWER(v.artist.name) LIKE LOWER(CONCAT('%', :text, '%')) " +
		       "ORDER BY v.name")
	Page<Video> search(@Param("text") String text, Pageable pageable);

	Page<Video> findAllByOrderByLastModifiedDesc(Pageable pageable);

}