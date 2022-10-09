package de.yloose.nodeup.networking.packet;

import java.util.Arrays;

public class Frame {

	private RadiotapHeader radiotapHeader;
	private ManagementFrame managementFrame;

	public Frame(byte[] packet) {

		this.radiotapHeader = RadiotapHeader.parseRadiotapHeader(packet);
		this.managementFrame = new ManagementFrame(
				Arrays.copyOfRange(packet, this.radiotapHeader.getLength(), packet.length));
	}

	public ManagementFrame getManagementFrame() {
		return managementFrame;
	}
}
