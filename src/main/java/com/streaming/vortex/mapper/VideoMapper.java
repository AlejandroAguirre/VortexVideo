package com.streaming.vortex.mapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.streaming.vortex.entities.Artist;
import com.streaming.vortex.entities.PreviewImage;
import com.streaming.vortex.entities.Video;
import com.streaming.vortex.model.dto.VideoDTO;

@Mapper(componentModel = "spring")
public interface VideoMapper {

	@Mapping(target = "path", source = "name")
	@Mapping(target = "type", constant = "folder")
	@Mapping(target = "thumbnails", expression = "java(toArtistThumbnails(artist))")
	@Mapping(target = "liked", expression = "java(artist.getVideos().stream().anyMatch(v -> v.getLikes() != null && !v.getLikes().isEmpty()))")
	VideoDTO artistToFolderDTO(Artist artist);

	@Mapping(target = "path", source = "id", qualifiedByName = "idToString")
	@Mapping(target = "type", constant = "video")
	@Mapping(target = "thumbnails", expression = "java(toVideoThumbnails(video))")
	@Mapping(target = "liked", expression = "java(video.getLikes()!=null && !video.getLikes().isEmpty())")
	VideoDTO videoToDTO(Video video);

	@InheritConfiguration(name = "videoToDTO")
	@Mapping(target = "type", constant = "file")
	@Mapping(target = "thumbnails", ignore = true)
	@Mapping(target = "liked", expression = "java(video.getLikes()!=null && !video.getLikes().isEmpty())")
	VideoDTO videoToFileDTO(Video video);

	@Named("idToString")
	default String idToString(Long id) {
		return id != null ? id.toString() : null;
	}

	default List<String> toArtistThumbnails(Artist artist) {
		return artist.getVideos().stream().limit(4).map(v -> "/api/vortex/thumbnail?path=" + v.getId())
				.collect(Collectors.toList());
	}

	default List<String> toVideoThumbnails(Video video) {
		return video.getPreviews().stream().sorted(Comparator.comparing(PreviewImage::getPosition))
				.map(p -> "/api/vortex/preview?id=" + p.getId()).collect(Collectors.toList());
	}
}