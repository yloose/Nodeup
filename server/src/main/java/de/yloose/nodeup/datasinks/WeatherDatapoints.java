package de.yloose.nodeup.datasinks;

import java.util.List;

import de.yloose.nodeup.models.NodeEntity;

public class WeatherDatapoints {

	private List<WeatherDatapoint> datapoints;
	private NodeEntity node;
	
	public WeatherDatapoints() {
		super();
	}

	public WeatherDatapoints(List<WeatherDatapoint> datapoints, NodeEntity node) {
		super();
		this.datapoints = datapoints;
		this.node = node;
	}

	public List<WeatherDatapoint> getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(List<WeatherDatapoint> datapoints) {
		this.datapoints = datapoints;
	}

	public NodeEntity getNode() {
		return node;
	}
		
	public void setNode(NodeEntity node) {
		this.node = node;
	}

	public static class WeatherDatapoint {

		private long timestamp;
		private float temperature;
		private float humidity;
		private float pressure;
		private float voltage;

		public WeatherDatapoint(long timestamp, float temperature, float humidity, float pressure, float voltage) {
			super();
			this.timestamp = timestamp;
			this.temperature = temperature;
			this.humidity = humidity;
			this.pressure = pressure;
			this.voltage = voltage;
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
	}
}
