package de.yloose.nodeup.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.yloose.nodeup.datasinks.DatapointPublisher;
import de.yloose.nodeup.datasinks.DatapointsPublishable;
import de.yloose.nodeup.models.NodeEntity;
import de.yloose.nodeup.networking.NodeData;


@Service
public class NodeDataHandler implements NodeupFrameHandler {
	
	@Autowired
	private NodeService nodeService;
	
	@Autowired
	private DatapointService datapointService;
	
	@Autowired
	private DatapointPublisher datapointPublisher;
	
	private static Logger LOG = LoggerFactory.getLogger(NodeDataHandler.class);

	@Override
	public void handleFrame(NodeupFrameWrapper nodeupFrameWrapper) {
		LOG.info("Received data mac {}", nodeupFrameWrapper.getSourceMac());
		
		// Get node from database or create new one
		NodeEntity node = nodeService.findByMacOrCreate(nodeupFrameWrapper.getSourceMac());
		
		// Respond to node with configuration data
		nodeService.sendServerConfigToNode(node);
		
		// Unpack the packet with datapoints. Calculate the timestamps, save them to the db and publish them
		NodeData nodeData = (NodeData) nodeupFrameWrapper.getNodeupFrame();
		nodeData.calculateTimestamps(node.getConfig().getMeasureInterval());
		
		datapointService.saveDatapoints(nodeData.getDatapoints(), node);
		
		datapointPublisher.publish(DatapointsPublishable.createFromDatapointIn(nodeData.getDatapoints(), node));
	}

}
