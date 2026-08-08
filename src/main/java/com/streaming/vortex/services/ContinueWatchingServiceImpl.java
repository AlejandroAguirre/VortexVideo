package com.streaming.vortex.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.streaming.vortex.common.Constantes;
import com.streaming.vortex.entities.ContinueWatching;
import com.streaming.vortex.entities.Video;
import com.streaming.vortex.mapper.ContinueWatchingMapper;
import com.streaming.vortex.model.dto.ContinueWatchingRequest;
import com.streaming.vortex.model.dto.VideoDTO;
import com.streaming.vortex.repository.ContinueWatchingRepository;
import com.streaming.vortex.repository.VideoRepository;

@Service
@Transactional
public class ContinueWatchingServiceImpl implements ContinueWatchingService {

	private final ContinueWatchingRepository continueRepository;
	private final VideoRepository videoRepository;
	private final ContinueWatchingMapper mapper;
	private final ThumbnailService thumbnailService;

	public ContinueWatchingServiceImpl(ContinueWatchingRepository continueRepository, VideoRepository videoRepository,
			ContinueWatchingMapper mapper, ThumbnailService thumbnailService) {

		this.continueRepository = continueRepository;
		this.videoRepository = videoRepository;
		this.mapper = mapper;
		this.thumbnailService = thumbnailService;
	}

	@Override
	public void saveProgress(ContinueWatchingRequest request) {

		Video video = videoRepository.findById(request.getVideoId())
				.orElseThrow(() -> new RuntimeException(Constantes.VIDEO_NOT_FOUND));
		// Si el usuario prácticamente terminó el video,
		// ya no debe aparecer en "Seguir viendo".
		if (request.getDuration() != null && request.getDuration() > 0 && request.getCurrentSecond() != null) {
			double progress = (request.getCurrentSecond() * 100D) / request.getDuration();

			if (progress >= 95D) {
				continueRepository.findByVideoId(video.getId()).ifPresent(item -> {
					if (item.getPreviewPath() != null) {
						try {
							Files.deleteIfExists(Paths.get(item.getPreviewPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					continueRepository.delete(item);
				});
				return;
			}
		}
		ContinueWatching continueWatching = continueRepository.findByVideoId(video.getId())
				.orElseGet(ContinueWatching::new);
		continueWatching.setVideo(video);
		continueWatching.setCurrentSecond(request.getCurrentSecond());
		continueWatching.setDuration(request.getDuration());
		continueWatching.setUpdatedAt(new Date());
		String preview = thumbnailService.generateContinueFrame(video, request.getCurrentSecond());
		continueWatching.setPreviewPath(preview);
		continueRepository.save(continueWatching);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<VideoDTO> getContinueWatching(Pageable pageable) {
	    return continueRepository
	            .findAllOrdered(pageable)
	            .map(mapper::toDTO);
	}

	@Override
	public void remove(Long videoId) {
		ContinueWatching progress = continueRepository.findByVideoId(videoId).orElse(null);

		if (progress != null) {
			if (progress.getPreviewPath() != null) {
				try {
					Files.deleteIfExists(Paths.get(progress.getPreviewPath()));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			continueRepository.delete(progress);
		}
	}


}