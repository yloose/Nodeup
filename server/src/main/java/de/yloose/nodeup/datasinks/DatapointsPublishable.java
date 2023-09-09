package de.yloose.nodeup.datasinks;

import java.util.List;
import java.util.stream.Collectors;

import de.yloose.nodeup.models.NodeEntity;
import de.yloose.nodeup.networking.NodeData.DatapointIn;

public class DatapointsPublishable implements Publishable {

	private List<DatapointOut> datapoints;
	private NodeEntity node;
	
	public DatapointsPublishable() {
		super();
	}

	public DatapointsPublishable(List<DatapointOut> datapoints, NodeEntity node) {
		this.datapoints = datapoints;
		this.node = node;
	}
	
	public static DatapointsPublishable createFromDatapointIn(List<DatapointIn> datapointsIn, NodeEntity node) {
		return new DatapointsPublishable(
				datapointsIn.stream().map(dpIn -> new DatapointOut(dpIn.getTimestamp(), dpIn.getTemperature(), dpIn.getHumidity(), dpIn.getPressure(), dpIn.getVoltage())).collect(Collectors.toList()),
				node);
	}

	public List<DatapointOut> getDatapoints() {
		return datapoints;
	}

	public void setDatapoints(List<DatapointOut> datapoints) {
		this.datapoints = datapoints;
	}

	public NodeEntity getNode() {
		return node;
	}
		
	public void setNode(NodeEntity node) {
		this.node = node;
	}

	public static class DatapointOut {

		private long timestamp;
		private float temperature;
		private float humidity;
		private float pressure;
		private float voltage;

		public DatapointOut(long timestamp, float temperature, float humidity, float pressure, float voltage) {
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
