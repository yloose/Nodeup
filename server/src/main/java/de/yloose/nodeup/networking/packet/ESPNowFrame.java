package de.yloose.nodeup.networking.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

import de.yloose.nodeup.networking.NodeupFrame;
import de.yloose.nodeup.networking.NodeupFrameFactory;

public class ESPNowFrame extends ActionFrame {

	private byte[] oui;
	private byte element_id;
	private byte length;
	private byte[] oui_vsc;
	private byte type;
	private byte version;
	
	NodeupFrame nodeupFrame;
	
	public static final byte[] ESPRESSIF_OUI = new byte[] { 0x18, (byte) 0xfe, 0x34 };

	public ESPNowFrame(byte category, byte[] oui, byte element_id, byte length, byte[] oui_vsc, byte type, byte version) {
		super(category);
		this.oui = oui;
		this.element_id = element_id;
		this.length = length;
		this.oui_vsc = oui_vsc;
		this.type = type;
		this.version = version;
	}

	public ESPNowFrame(byte[] frame) {
		super(frame[0]);
		this.oui = Arrays.copyOfRange(frame, 1, 4);
		this.element_id = frame[8];
		this.length = frame[9];
		this.oui_vsc = Arrays.copyOfRange(frame, 10, 13);
		this.type = frame[13];
		this.version = frame[14];
		this.nodeupFrame = NodeupFrameFactory.createNodeupFrame(Arrays.copyOfRange(frame, 15, frame.length));
	}

	public static ESPNowFrame createESPNowFrame() {
		// @formatter:off
		return new ESPNowFrame(
				// Category = 127 indicating vendor specific content
				(byte) 127,
				// Organizationally unique identifier from espressif
				new byte[] {24, (byte) 254, 52},
				// Element id = 221 indicating vendor specific content
				(byte) 221,
				// Length = OUI + Type + Version
				(byte) (3 + 1 + 1),
				// Organizationally unique identifier (inside vendor specific content) from espressif
				new byte[] {24, (byte) 254, 52},
				// Type = 4 indicating ESP-NOW
				(byte) 4,
				// Version = ESP-NOW version
				(byte) 1
			);
		// @formatter:on
	}
	
	public void calculateLength() {
		// Length = OUI + Type + Version + Body
		this.length = (byte) (3 + 1 + 1 + nodeupFrame.toByteArray().length);
	}
	
	public boolean isESPNow() {
		return Arrays.equals(oui, ESPRESSIF_OUI);
	}

	@Override
	public byte[] toByteArray() {
		// Generate 4 random bytes
		byte[] randomBytes = new byte[4];
		new Random().nextBytes(randomBytes);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stream.write(super.toByteArray());
			stream.write(this.oui);
			stream.write(randomBytes);
			stream.write(this.element_id);
			stream.write(this.length);
			stream.write(this.oui_vsc);
			stream.write(this.type);
			stream.write(this.version);
			stream.write(this.nodeupFrame.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stream.toByteArray();
	}
	
	public byte[] getOui() {
		return oui;
	}

	public NodeupFrame getNodeupFrame() {
		return nodeupFrame;
	}

	public void setNodeupFrame(NodeupFrame nodeupFrame) {
		this.nodeupFrame = nodeupFrame;
	}
}
