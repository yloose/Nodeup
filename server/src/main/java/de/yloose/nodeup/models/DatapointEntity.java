package de.yloose.nodeup.models;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import de.yloose.nodeup.networking.NodeData.DatapointIn;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "DATAPOINTS")
public class DatapointEntity {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	private long timestamp;

	private float temperature;
	private float humidity;
	private float pressure;
	private float voltage;

	@ManyToOne
	@JoinColumn(name = "NODE_ID")
	private NodeEntity node;

	public DatapointEntity() {
		super();
	}

	public DatapointEntity(DatapointIn datapointIn, NodeEntity node) {
		this.timestamp = datapointIn.getTimestamp();
		this.humidity = datapointIn.getHumidity();
		this.node = node;
		this.pressure = datapointIn.getPressure();
		this.temperature = datapointIn.getTemperature();
		this.voltage = datapointIn.getVoltage();
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

	public NodeEntity getNode() {
		return node;
	}

	public void setNode(NodeEntity node) {
		this.node = node;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public void setPressure(float pressure) {
		this.pressure = pressure;
	}

	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}

}
