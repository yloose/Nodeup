package de.yloose.nodeup.networking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Weatherdata {

	private int sendCause;
	private int sequenceNumber;
	private int sequenceLength;
	private List<WeatherDatapointDto> weatherdataDatapoints = new ArrayList<WeatherDatapointDto>();
	private int crc;

	public Weatherdata(byte[] data) {

		this.sendCause = data[0];
		this.sequenceNumber = data[1];
		this.sequenceLength = data[2];
		for (int i = 0; i < 35 && !reachedEndOfData(data, i * 7 + 3); i++) {
			weatherdataDatapoints.add(new WeatherDatapointDto(Arrays.copyOfRange(data, i * 7 + 3, i * 7 + 10)));
		}
		this.crc = bytesToInt(Arrays.copyOfRange(data, 248, 250));
	}

	private boolean reachedEndOfData(byte[] data, int i) {
		return (data[i] | data[i + 1] | data[i + 2] | data[i + 3] | data[i + 4] | data[i + 5] | data[i + 6]) == 0;
	}

	public void calculateTimestamps(int measurmentInterval) {
		int index = 0;
		for (WeatherDatapointDto datapoint : this.weatherdataDatapoints) {
			datapoint.setTimestamp(System.currentTimeMillis() - (measurmentInterval * index));
			index++;
		}
	}

	// Changes endianess
	private int bytesToInt(byte[] bytes) {
		return ((bytes[1] & 0xff) << 8) | (bytes[0] & 0xff);
	}

	public int getSendCause() {
		return sendCause;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public int getSequenceLength() {
		return sequenceLength;
	}

	public List<WeatherDatapointDto> getWeatherdataDatapoints() {
		return weatherdataDatapoints;
	}

	public static class WeatherDatapointDto {

		private long timestamp;
		private float temperature;
		private float humidity;
		private float pressure;
		private float voltage;

		public WeatherDatapointDto(byte[] data) {
			this.temperature = Integer.valueOf(bytesToInt(Arrays.copyOfRange(data, 0, 2))).floatValue() / 100 - 40;
			this.humidity = Integer.valueOf(bytesToInt(Arrays.copyOfRange(data, 2, 4))).floatValue() / 100;
			this.pressure = Integer.valueOf(bytesToInt(Arrays.copyOfRange(data, 4, 6))).floatValue() / 100 + 600;
			this.voltage = (Integer.valueOf(data[6]).floatValue() + 250) / 100;
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
