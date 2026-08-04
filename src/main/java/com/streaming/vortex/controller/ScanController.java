package com.streaming.vortex.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.streaming.vortex.services.ScanService;
import com.streaming.vortex.services.ThumbnailService;

@RestController
@RequestMapping("/api/vortex/admin")
public class ScanController {

	private final ScanService scanService;
	private final ThumbnailService thumbnailService;

	public ScanController(ScanService scanService, ThumbnailService thumbnailService) {
		this.scanService = scanService;
		this.thumbnailService = thumbnailService;
	}

	@PostMapping("/scan")
	public ResponseEntity<?> scan() throws Exception {
		scanService.scanLibrary();
		return ResponseEntity.ok("Escaneo terminado");

	}

	@PostMapping("/thumbnails")
	public ResponseEntity<?> generate() {
		thumbnailService.generateAll();
		return ResponseEntity.ok("Generación imagenes previas iniciada");
	}
}