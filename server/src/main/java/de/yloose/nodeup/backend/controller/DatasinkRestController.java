package de.yloose.nodeup.backend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.yloose.nodeup.backend.models.DatasinkEntity;
import de.yloose.nodeup.backend.repository.DatasinkRepository;

@RestController
@RequestMapping("/api/datasinks")
public class DatasinkRestController {

	@Autowired
	private DatasinkRepository datasinkRepository;

	@GetMapping("/getAllAvailable")
	public ResponseEntity<List<DatasinkEntity>> getAvailableDatasinks() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8; Cache-Control: no-cache");

		return ResponseEntity.ok().headers(headers).body(datasinkRepository.findAll());
	}
}
