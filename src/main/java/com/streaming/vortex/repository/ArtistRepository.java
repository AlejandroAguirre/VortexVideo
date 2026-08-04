package com.streaming.vortex.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.streaming.vortex.entities.Artist;

public interface ArtistRepository extends JpaRepository<Artist, Long> {

	Optional<Artist> findByFolderPath(String folderPath);

	Optional<Artist> findByName(String name);

	boolean existsByFolderPath(String folderPath);

	 @Query(value = "SELECT * FROM artists ORDER BY RAND() LIMIT 7", nativeQuery = true)
	    List<Artist> findRandomArtists();

}