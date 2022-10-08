package de.yloose.nodeup.backend.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.yloose.nodeup.backend.models.NodeEntity;
import de.yloose.nodeup.backend.repository.NodeRepository;

@Service
public class NodeService {
	
	@Autowired
	private NodeRepository nodeRepository;
	
	@Transactional
	public NodeEntity save(NodeEntity node) {
		Optional<NodeEntity> nodeFromDB = nodeRepository.findById(node.getId());
		
		if (nodeFromDB.isPresent()) {
			nodeFromDB.get().setDisplayName(node.getDisplayName());
			nodeFromDB.get().setConfig(node.getConfig());
			nodeFromDB.get().getDatasinksConfigs().clear();
			nodeFromDB.get().getDatasinksConfigs().addAll(node.getDatasinksConfigs());
			return nodeRepository.save(nodeFromDB.get());
		} else {
			return null;
		}
 
	}

}
