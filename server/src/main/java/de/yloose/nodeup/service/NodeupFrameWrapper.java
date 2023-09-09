package de.yloose.nodeup.service;

import de.yloose.nodeup.networking.NodeupFrame;

public class NodeupFrameWrapper {

	private NodeupFrame nodeupFrame;
	private String sourceMac;
	
	public NodeupFrameWrapper(NodeupFrame nodeupFrame, String sourceMac) {
		this.nodeupFrame = nodeupFrame;
		this.sourceMac = sourceMac;
	}

	public NodeupFrame getNodeupFrame() {
		return nodeupFrame;
	}

	public String getSourceMac() {
		return sourceMac;
	}
	
}
