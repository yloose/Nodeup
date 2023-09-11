package de.yloose.nodeup.networking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NodeData extends NodeupFrame {
	
	private int sendCause;
	private int packetIndex;
	private int packetCount;
	private List<DatapointIn> datapoints = new ArrayList<DatapointIn>();

	public NodeData(byte[] data) {
		super(Arrays.copyOfRange(data, 0, NODEUP_FRAME_HEADER_LENGTH));
		
		this.sendCause = data[NODEUP_FRAME_HEADER_LENGTH];
		this.packetIndex = data[NODEUP_FRAME_HEADER_LENGTH + 1];
		this.packetCount = data[NODEUP_FRAME_HEADER_LENGTH + 2];
		int size = data[NODEUP_FRAME_HEADER_LENGTH + 3];
		for (int i = NODEUP_FRAME_HEADER_LENGTH + 4; i < NODEUP_FRAME_HEADER_LENGTH + size * DatapointIn.DATAPOINT_SIZE; i += DatapointIn.DATAPOINT_SIZE) {
			datapoints.add(new DatapointIn(Arrays.copyOfRange(data, i, i + DatapointIn.DATAPOINT_SIZE)));
		}
	}
	
	public void calculateTimestamps(int measurmentInterval, long baseTime) {
		int index = this.datapoints.size() - 1;
		for (DatapointIn datapoint : this.datapoints) {
			datapoint.setTimestamp(baseTime - (measurmentInterval * index));
			index--;
		}
	}
	
	public void calculateTimestamps(int measurementInterval) {
		this.calculateTimestamps(measurementInterval, System.currentTimeMillis());
	}
	
	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getSendCause() {
		return sendCause;
	}

	public int getPacketIndex() {
		return packetIndex;
	}

	public int getPacketCount() {
		return packetCount;
	}

	public List<DatapointIn> getDatapoints() {
		return datapoints;
	}

	public static class DatapointIn {

		private long timestamp;
		
		private float temperature;
		private float humidity;
		private float pressure;
		private float voltage;
		
		public static final int DATAPOINT_SIZE = 7;

		public DatapointIn(byte[] data) {
			this.temperature = (float) bytesToInt(Arrays.copyOfRange(data, 0, 2)) / 100 - 40;
			this.humidity = (float) bytesToInt(Arrays.copyOfRange(data, 2, 4)) / 100;
			this.pressure = (float) bytesToInt(Arrays.copyOfRange(data, 4, 6)) / 100 + 600;
			
			if (data[6] < 0) {
				this.voltage = (Integer.valueOf(256 + data[6]).floatValue() + 250) / 100;
			} else {
				this.voltage = (Integer.valueOf(data[6]).floatValue() + 250) / 100;
			}
		}

		// Changes endianess
		private int bytesToInt(byte[] bytes) {
			return ((bytes[1] & 0xff) << 8) | (bytes[0] & 0xff);
		}

		public float getTemperature() {
			return temperature;
		}

		public float getHumidity() {
			return humidity;
		}

		public float getPressure() {
			return pressure;
		}

		public float getVoltage() {
			return voltage;
		}

		public long getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(long timestamp) {
			this.timestamp = timestamp;
		}
	}

}
