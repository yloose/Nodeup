package de.yloose.nodeup.networking.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import de.yloose.nodeup.networking.NodeupFrame;
import de.yloose.nodeup.util.Conversion;

public class Frame {

	private RadiotapHeader radiotapHeader;
	private ManagementFrame managementFrame;
	
	public Frame() {
		super();
	}

	public Frame(byte[] packet) {

		this.radiotapHeader = RadiotapHeader.parseRadiotapHeader(packet);
		this.managementFrame = new ManagementFrame(
				Arrays.copyOfRange(packet, this.radiotapHeader.getLength(), packet.length));
	}
	
	
	public static Frame createFromNodeupFrame(NodeupFrame nodeupFrame, String destMac) {
		Frame f = new Frame();
		f.setRadiotapHeader(RadiotapHeader.createRadiotapHeader());
		f.setManagementFrame(ManagementFrame.createManagementFrame(Conversion.macStringToBytes(destMac)));
		f.getManagementFrame().setActionFrame(ESPNowFrame.createESPNowFrame());
		((ESPNowFrame) f.getManagementFrame().getActionFrame()).setNodeupFrame(nodeupFrame);
		((ESPNowFrame) f.getManagementFrame().getActionFrame()).calculateLength();
		f.getManagementFrame().calculateFcs();
		return f;
	}
	
	public byte[] toByteArray() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stream.write(this.radiotapHeader.toByteArray());
			stream.write(this.managementFrame.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stream.toByteArray();
	}

	public ManagementFrame getManagementFrame() {
		return managementFrame;
	}

	public RadiotapHeader getRadiotapHeader() {
		return radiotapHeader;
	}

	public void setRadiotapHeader(RadiotapHeader radiotapHeader) {
		this.radiotapHeader = radiotapHeader;
	}

	public void setManagementFrame(ManagementFrame managementFrame) {
		this.managementFrame = managementFrame;
	}
}
