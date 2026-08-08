package com.streaming.vortex.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContinueWatchingRequest {

	private Long videoId;

	private Long currentSecond;

	private Long duration;
}