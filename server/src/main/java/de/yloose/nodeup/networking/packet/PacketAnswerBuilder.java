package de.yloose.nodeup.networking.packet;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.yloose.nodeup.models.NodeEntity.NodeConfig;

public class PacketAnswerBuilder {

	public static byte[] build(NodeConfig nodeConfig) {
		ByteBuffer buffer = ByteBuffer.allocate(18).order(ByteOrder.LITTLE_ENDIAN);
		
		buffer.putInt(nodeConfig.getSendDataInterval());
		buffer.putShort((short) nodeConfig.getMeasureInterval());
		buffer.put((byte) nodeConfig.getEspOperatingMode().getValue());
		buffer.put((byte) (nodeConfig.isUseULP() ? 1 : 0));
		buffer.putShort((short) (nodeConfig.getSendDataDeltas().getTemperature() * 100));
		buffer.putShort((short) (nodeConfig.getSendDataDeltas().getHumidity() * 100));
		buffer.putShort((short) (nodeConfig.getSendDataDeltas().getPressure() * 100));
		buffer.put((byte) (nodeConfig.getSendDataDeltas().getVoltage() * 100));
		buffer.put((byte) (nodeConfig.getSwCutoffVoltage() * 100));
		// TODO: Add real crc16-ccit
		buffer.putShort((short) 0);
		
		return buffer.array();
		
//		ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		try {
//			stream.write(convertSendDataInterval(nodeConfig.getSendDataInterval()));
//			stream.write(convertMeasureInterval(nodeConfig.getMeasureInterval()));
//			stream.write(convertESPOperatingMode(nodeConfig.getEspOperatingMode()));
//			stream.write(convertUseULP(nodeConfig.isUseULP()));
//			// Send data deltas START
//			stream.write(convertSendDataDeltaTemperature(nodeConfig.getSendDataDeltas().getTemperature()));
//			stream.write(convertSendDataDeltaHumidity(nodeConfig.getSendDataDeltas().getHumidity()));
//			stream.write(convertSendDataDeltaPressure(nodeConfig.getSendDataDeltas().getPressure()));
//			stream.write(convertSendDataDeltaVoltage(nodeConfig.getSendDataDeltas().getVoltage()));
//			// Send data deltas END
//			stream.write(convertSwCutoffVoltage(nodeConfig.getSwCutoffVoltage()));
//			// TODO: Add real crc16-ccit
//			stream.write(new byte[] {0, 0});
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		return stream.toByteArray();
	}

//	private static byte[] intToByteArray(int number, int length) {
//		return ByteBuffer.allocate(length).putInt(number).array();
//	}
//
//	// Converting methods
//
//	private static byte[] convertSendDataInterval(int sendDataInterval) {
//		return intToByteArray(sendDataInterval, 4);
//	}
//
//	private static byte[] convertMeasureInterval(int measureInterval) {
//		return intToByteArray(measureInterval, 2);
//	}
//	
//	private static byte convertESPOperatingMode(ESPOperatingMode espOperatingMode) {
//		return (byte) espOperatingMode.getValue();
//	}
//
//	private static byte convertUseULP(boolean useULP) {
//		return (byte) (useULP ? 1 : 0);
//	}
//	
//	private static byte[] convertSendDataDeltaTemperature(double temperature) {
//		return intToByteArray((int) (temperature * 100), 2);
//	}
//
//	private static byte[] convertSendDataDeltaHumidity(double humidity) {
//		return intToByteArray((int) (humidity * 100), 2);
//	}
//
//	private static byte[] convertSendDataDeltaPressure(double pressure) {
//		return intToByteArray((int) (pressure * 100), 2);
//	}
//
//	private static byte convertSendDataDeltaVoltage(double voltage) {
//		return (byte) (voltage * 100);
//	}
//
//	private static byte convertSwCutoffVoltage(double voltage) {
//		return (byte) (voltage * 100);
//	}
}
