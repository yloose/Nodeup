package de.yloose.nodeup.networking.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class ESPNowFrame implements ActionFrame {

	private byte category;

	private byte[] oui;
	private byte element_id;
	private byte length;
	private byte[] oui_vsc;
	private byte type;
	private byte version;
	private byte[] variable_data;

	public ESPNowFrame(byte category, byte[] oui, byte element_id, byte length, byte[] oui_vsc, byte type, byte version,
			byte[] variable_data) {
		super();
		this.category = category;
		this.oui = oui;
		this.element_id = element_id;
		this.length = length;
		this.oui_vsc = oui_vsc;
		this.type = type;
		this.version = version;
		this.variable_data = variable_data;
	}

	public static ESPNowFrame parseESPNowFrame(byte[] frame) {
		// @formatter:off
		return new ESPNowFrame(
				frame[0],
				Arrays.copyOfRange(frame, 1, 4),
				frame[8],
				frame[9],
				Arrays.copyOfRange(frame, 10, 13),
				frame[13],
				frame[14],
				Arrays.copyOfRange(frame, 15, frame.length)
			);
		// @formatter:on
	}

	public static ESPNowFrame createESPNowFrame(byte[] data) {
		// @formatter:off
		return new ESPNowFrame(
				// Category = 127 indicating vendor specific content
				(byte) 127,
				// Organizationally unique identifier from espressif
				new byte[] {24, (byte) 254, 52},
				// Element id = 221 indicating vendor specific content
				(byte) 221,
				// Length = OUI + Type + Version + Body
				(byte) (3 + 1 + 1 + data.length),
				// Organizationally unique identifier (inside vendor specific content) from espressif
				new byte[] {24, (byte) 254, 52},
				// Type = 4 indicating ESP-NOW
				(byte) 4,
				// Version = ESP-NOW version
				(byte) 1,
				data
			);
		// @formatter:on
	}

	public byte[] toByteArray() {
		// Generate 4 random bytes
		byte[] randomBytes = new byte[4];
		new Random().nextBytes(randomBytes);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stream.write(this.category);
			stream.write(this.oui);
			stream.write(randomBytes);
			stream.write(this.element_id);
			stream.write(this.length);
			stream.write(this.oui_vsc);
			stream.write(this.type);
			stream.write(this.version);
			stream.write(this.variable_data);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stream.toByteArray();
	}

	public int getCategory() {
		return (int) category;
	}

	public byte[] getOui() {
		return oui;
	}

	public byte[] getVariable_data() {
		return variable_data;
	}
}
