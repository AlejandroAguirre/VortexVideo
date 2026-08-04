package com.streaming.vortex.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.streaming.vortex.entities.ContinueWatching;
import com.streaming.vortex.model.dto.VideoDTO;

@Mapper(componentModel = "spring")
public interface ContinueWatchingMapper {

	@Mapping(target = "name", source = "video.name")
	@Mapping(target = "path", expression = "java(String.valueOf(entity.getVideo().getId()))")
	@Mapping(target = "currentSecond", source = "currentSecond")
	@Mapping(target = "duration", source = "duration")
	@Mapping(target = "continueImage", expression = "java(\"/api/vortex/continue/continue-preview?id=\" + entity.getVideo().getId())")
	@Mapping(target = "progress", expression = "java(progress(entity))")
	@Mapping(target = "liked", expression = "java(isLiked(entity))")

	VideoDTO toDTO(ContinueWatching entity);

	default boolean isLiked(ContinueWatching entity) {
		return entity.getVideo() != null && entity.getVideo().getLikes() != null
				&& !entity.getVideo().getLikes().isEmpty();
	}

	default Integer progress(ContinueWatching entity) {
		if (entity.getDuration() == null || entity.getDuration() == 0) {
			return 0;
		}

		return (int) (entity.getCurrentSecond() * 100 / entity.getDuration());
	}
}