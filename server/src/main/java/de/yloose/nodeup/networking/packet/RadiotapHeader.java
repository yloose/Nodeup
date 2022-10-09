package de.yloose.nodeup.networking.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class RadiotapHeader {

	private byte version;
	private byte[] length;
	private byte[] presentFlags;
	private byte[] flags;
	
	public RadiotapHeader(byte version, byte[] length, byte[] presentFlags) {
		this.version = version;
		this.length = length;
		this.presentFlags = presentFlags;
	}
	
	public RadiotapHeader(byte version, byte[] length, byte[] presentFlags, byte[] flags) {
		this.version = version;
		this.length = length;
		this.presentFlags = presentFlags;
		this.flags = flags;
	}
	
	public static RadiotapHeader parseRadiotapHeader(byte[] packet) {
		return new RadiotapHeader(packet[0], Arrays.copyOfRange(packet, 2, 4), Arrays.copyOfRange(packet, 4, 8));
	}
	
	public static RadiotapHeader createRadiotapHeader() {
		return new RadiotapHeader((byte) 0, new byte[] {0x0a, 0}, new byte[] {0, 0,(byte) 0x80, 0}, new byte[] {0x18, 0});
	}
	
	public byte[] toByteArray() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stream.write(version);
			// Padding
			stream.write((byte) 0);
			stream.write(this.length);
			stream.write(this.presentFlags);
			stream.write(this.flags);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stream.toByteArray();
	}

	public int getVersion() {
		return version;
	}

	public int getLength() {
		return ((this.length[1] & 0xff) << 8) | (this.length[0] & 0xff);
	}

	public byte[] getPresentFLags() {
		return presentFlags;
	}

}
