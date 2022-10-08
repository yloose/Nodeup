package de.yloose.nodeup.backend.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import de.yloose.nodeup.backend.models.NodeEntity;
import de.yloose.nodeup.backend.repository.NodeRepository;
import de.yloose.nodeup.backend.service.NodeService;

@RestController
@RequestMapping("/api/nodes")
public class NodeRestController {

	@Autowired
	private NodeService nodeService;
	
	@Autowired
	private NodeRepository nodeRepository;

	@GetMapping("/all")
	public ResponseEntity<List<NodeEntity>> getNodes() {

		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8; Cache-Control: no-cache");

		return ResponseEntity.ok().headers(headers).body(nodeRepository.findAll());
	}

	@GetMapping("")
	public ResponseEntity<NodeEntity> getNodeById(@RequestParam(required = true) UUID id) {
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, "application/json; charset=UTF-8; Cache-Control: no-cache");

		Optional<NodeEntity> node = nodeRepository.findById(id);

		if (node.isPresent()) {
			return ResponseEntity.ok().headers(headers).body(node.get());
		} else {
			return ResponseEntity.notFound().build();
		}

	}

	@PutMapping("update")
	public ResponseEntity<NodeEntity> updateNode(@RequestParam(required = true) UUID id, @RequestBody NodeEntity node) {
		
		NodeEntity savedEntity = nodeService.save(node); 
		
		if (savedEntity != null) {
			return ResponseEntity.ok(savedEntity);
		} else {
			return ResponseEntity.notFound().build();
		}
		
//		if (nodeRepository.existsById(node.getId())) {
//			return ResponseEntity.ok(nodeRepository.save(node));
//		} else {
//			return ResponseEntity.notFound().build();
//		}
	}

}
