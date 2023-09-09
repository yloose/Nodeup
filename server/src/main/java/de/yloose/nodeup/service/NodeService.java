package de.yloose.nodeup.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.yloose.nodeup.models.NodeEntity;
import de.yloose.nodeup.networking.ServerConfiguration;
import de.yloose.nodeup.networking.packet.Frame;
import de.yloose.nodeup.repository.NodeRepository;
import jakarta.transaction.Transactional;

@Service
public class NodeService {
	
	@Autowired
	private NodeRepository nodeRepository;
	
	@Autowired
	private NetworkService networkService;
	
	private static Logger LOG = LoggerFactory.getLogger(NodeService.class);
	
	
	public void sendServerConfigToNode(NodeEntity node) {
		
		LOG.info("Sending configuration response to node {} with mac {}.", node.getDisplayName(), node.getMac());
		
		ServerConfiguration serverConfiguration = new ServerConfiguration(node.getConfig());
		networkService.sendPacket(Frame.createFromNodeupFrame(serverConfiguration, node.getMac()).toByteArray());
	}
	
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
	
	@Transactional
	public NodeEntity findByMacOrCreate(String mac) {
		Optional<NodeEntity> nodeOpt = nodeRepository.findByMac(mac);
		NodeEntity node = nodeOpt.orElseGet(() -> NodeEntity.createNewNodeWithDefaultConfig(mac));
		return nodeRepository.save(node);
	}
}
