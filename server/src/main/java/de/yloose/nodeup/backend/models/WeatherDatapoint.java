package de.yloose.nodeup.backend.models;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import de.yloose.nodeup.backend.Weatherdata.WeatherDatapointDto;

@Entity
@Table(name = "WEATHERDATA", indexes = @Index(name = "idx_node_counter", columnList = "node_id, counter", unique = true))
public class WeatherDatapoint {

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
	private long counter;
    @ManyToOne
    @JoinColumn(name="NODE_ID")
	private NodeEntity node;

	public WeatherDatapoint() {
		super();
	}

	public WeatherDatapoint(WeatherDatapointDto datapointDto, NodeEntity node, long index) {
		super();
		this.timestamp = datapointDto.getTimestamp();
		this.counter = index;
		this.humidity = datapointDto.getHumidity();
		this.node = node;
		this.pressure = datapointDto.getPressure();
		this.temperature = datapointDto.getTemperature();
		this.voltage = datapointDto.getVoltage();	
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

	public long getCounter() {
		return counter;
	}

	public void setCounter(long counter) {
		this.counter = counter;
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

	@Override
	public int hashCode() {
		return Objects.hash(counter, node);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeatherDatapoint other = (WeatherDatapoint) obj;
		return counter == other.counter && Objects.equals(node, other.node);
	}
}

