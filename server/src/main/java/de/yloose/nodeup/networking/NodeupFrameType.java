package de.yloose.nodeup.networking;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.yloose.nodeup.service.NodeDataHandler;
import de.yloose.nodeup.service.NodeupFrameHandler;

public enum NodeupFrameType {
	NODE_DATA(NodeData.class, NodeDataHandler.class),
	NODE_OTA_STATUS(NodeOtaStatus.class, null),
	NODE_INFO(NodeInfo.class, null),

	SERVER_CONFIGURATION(ServerConfiguration.class, null),
	SERVER_OTA_UPDATE(ServerOtaUpdate.class, null);
	
	private final Class<? extends NodeupFrame> nodeupFrameClass;
	private final Class<? extends NodeupFrameHandler> nodeupFrameHandler;
	
	private static final Map<Class<? extends NodeupFrame>, Class<? extends NodeupFrameHandler>> LOOKUP_MAP;
	
	static {
		Map<Class<? extends NodeupFrame>, Class<? extends NodeupFrameHandler>> map = new HashMap<>();
		for (NodeupFrameType frameType : NodeupFrameType.values()) {
			map.put(frameType.getNodeupFrameClass(), frameType.getNodeupFrameHandler());
		}
		LOOKUP_MAP = Collections.unmodifiableMap(map);
	}
	
	private NodeupFrameType(Class<? extends NodeupFrame> nodeupFrameClass, Class<? extends NodeupFrameHandler> nodeupFrameHandler) {
		this.nodeupFrameClass = nodeupFrameClass;
		this.nodeupFrameHandler = nodeupFrameHandler;
	}
	
	public static Class<? extends NodeupFrameHandler> getFrameHandlerByFrameClass(Class<? extends NodeupFrame> frameClass) {
		return LOOKUP_MAP.get(frameClass);
	}

	public Class<? extends NodeupFrame> getNodeupFrameClass() {
		return nodeupFrameClass;
	}

	public Class<? extends NodeupFrameHandler> getNodeupFrameHandler() {
		return nodeupFrameHandler;
	}
}
