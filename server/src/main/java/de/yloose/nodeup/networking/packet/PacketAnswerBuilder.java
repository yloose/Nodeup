package de.yloose.nodeup.networking.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import de.yloose.nodeup.models.NodeEntity;
import de.yloose.nodeup.models.NodeEntity.NodeConfig;
import de.yloose.nodeup.util.CRC16CCIT;
import de.yloose.nodeup.util.Conversion;

public class PacketAnswerBuilder {
	
	public static byte[] build(NodeEntity node) {
		RadiotapHeader radioTapHeader = RadiotapHeader.createRadiotapHeader();
		ManagementFrame managementFrame = new ManagementFrame(13, Conversion.macStringToBytes(node.getMac()),
				new byte[] { (byte) 0xce, 0x50, (byte) 0xe3, 0x26, 0x0b, (byte) 0xf7 });
		managementFrame.setActionFrame(ESPNowFrame.createESPNowFrame(PacketAnswerBuilder.buildESPNowData(node.getConfig())));
		managementFrame.calculateFcs();

		byte[] radiotapHeaderBytes = radioTapHeader.toByteArray();
		byte[] managementFrameBytes = managementFrame.toByteArray();
		byte[] packet = Arrays.copyOf(radiotapHeaderBytes, radiotapHeaderBytes.length + managementFrameBytes.length);
		System.arraycopy(managementFrameBytes, 0, packet, radiotapHeaderBytes.length, managementFrameBytes.length);
		
		return packet;
	}

	public static byte[] buildESPNowData(NodeConfig nodeConfig) {
		ByteBuffer buffer = ByteBuffer.allocate(18).order(ByteOrder.LITTLE_ENDIAN);
		
		buffer.putInt(nodeConfig.getSendDataInterval());
		buffer.putShort((short) nodeConfig.getMeasureInterval());
		buffer.put((byte) nodeConfig.getEspOperatingMode().getValue());
		buffer.put((byte) (nodeConfig.isUseULP() ? 1 : 0));
		buffer.putShort((short) (nodeConfig.getSendDataDeltas().getTemperature() * 100));
		buffer.putShort((short) (nodeConfig.getSendDataDeltas().getHumidity() * 100));
		buffer.putShort((short) (nodeConfig.getSendDataDeltas().getPressure() * 100));
		buffer.put((byte) (nodeConfig.getSendDataDeltas().getVoltage() * 100));
		buffer.put((byte) (nodeConfig.getSwCutoffVoltage() * 100 - 2500));
		// Set crc field to 0 to calculate the checksum and replace it
		buffer.putShort((short) 0);
		byte[] crcData = buffer.array();
		buffer.position(buffer.position() - 2);
		buffer.putShort(CRC16CCIT.calculate(crcData));
		
		return buffer.array();
	}
}
