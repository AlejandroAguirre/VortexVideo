package com.streaming.vortex.model.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoDTO {

	private String name;

	private String path;

	private String type;

	private boolean liked;

	private List<String> thumbnails;

	private Long currentSecond;

	private Long duration;

	private Integer progress;

	private String continueImage;
}