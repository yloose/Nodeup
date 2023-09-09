package de.yloose.nodeup.networking;

public class NodeupFrameFactory {

	public static NodeupFrame createNodeupFrame(byte[] data) {
		
		NodeupFrameType type = NodeupFrame.getFrameTypeFromData(data);
		
		switch (type) {
		case NODE_DATA:
			return new NodeData(data);
		case NODE_OTA_STATUS:
			return new NodeOtaStatus(data);
		case NODE_INFO:
			return new NodeInfo(data);
		case SERVER_CONFIGURATION:
			return new ServerConfiguration(data);
		case SERVER_OTA_UPDATE:
			return new ServerOtaUpdate(data);
		default:
			return null;
		}
	}
}
