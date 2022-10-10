package de.yloose.nodeup.util;

public class CRC16CCIT {

	public static short calculate(byte[] bytes) {
		int crc = 0xFFFF;
		int polynomial = 0x1021;

		for (byte b : bytes) {
			for (int i = 0; i < 8; i++) {
				boolean bit = ((b >> (7 - i) & 1) == 1);
				boolean c15 = ((crc >> 15 & 1) == 1);
				crc <<= 1;
				if (c15 ^ bit)
					crc ^= polynomial;
			}
		}

		crc &= 0xffff;
		return (short) crc;
	}
}
