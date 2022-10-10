package de.yloose.nodeup.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ConversionTest {

	@Test
	void testMacStringToBytes_withNormalValues() {
		String macString = "2e:88:f3:40:94:61";
		byte[] expectedBytes = {(byte) 0x2e, (byte) 0x88, (byte) 0xf3, (byte) 0x40, (byte) 0x94, (byte) 0x61};
		
		assertArrayEquals(expectedBytes, Conversion.macStringToBytes(macString));
	}
	
	@Test
	void testMacBytesToString_withNormalValues() {
		byte[] macBytes = {(byte) 0xbc, (byte) 0xcb, (byte) 0xf9, (byte) 0xc8, (byte) 0x39, (byte) 0xce};
		String expectedString = "BC:CB:F9:C8:39:CE";
		
		assertEquals(expectedString, Conversion.macBytesToString(macBytes));
	}
}
