package de.yloose.nodeup.util;

import java.nio.ByteBuffer;

public class Conversion {
	public static byte[] macStringToBytes(String mac) {
		ByteBuffer buffer = ByteBuffer.allocate(6);
		for (String macPart : mac.split(":")) {
			buffer.put((byte) Integer.parseInt(macPart, 16));
		}
		return buffer.array();
	}
	
	public static String macBytesToString(byte[] bytes) {
		String[] mac = new String[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			mac[i] = String.format("%02X", bytes[i]);
		}
		return String.join(":", mac);
	}
}
