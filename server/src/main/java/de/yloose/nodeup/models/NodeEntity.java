package de.yloose.nodeup.models;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "NODES")
public class NodeEntity {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	@Column(unique = true)
	private String mac;
	private String displayName = "Unnamed";
	@Embedded
	private NodeConfig config;
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "NODE_ID", nullable = false)
	private List<NodeDatasinkConfigLinker> datasinksConfigs;

	@Embeddable
	public static class NodeConfig {
		private int sendDataInterval;
		private int measureInterval;
		private ESPOperatingMode espOperatingMode;
		private boolean useULP;
		@Embedded
		private SendDataDeltas sendDataDeltas;
		private double swCutoffVoltage;

		public NodeConfig() {
			super();
		}

		public NodeConfig(int sendDataInterval, int measureInterval, ESPOperatingMode espOperatingMode, boolean useULP,
				SendDataDeltas sendDataDeltas, double swCutoffVoltage) {
			super();
			this.sendDataInterval = sendDataInterval;
			this.measureInterval = measureInterval;
			this.espOperatingMode = espOperatingMode;
			this.useULP = useULP;
			this.sendDataDeltas = sendDataDeltas;
			this.swCutoffVoltage = swCutoffVoltage;
		}

		public enum ESPOperatingMode {
			ESP_MODE_HIBERNATE(0), ESP_MODE_DEEPSLEEP(1);

			private final int value;

			private ESPOperatingMode(int value) {
				this.value = value;
			}

			public int getValue() {
				return value;
			}
		}

		@Embeddable
		public static class SendDataDeltas {
			private double temperature;
			private double humidity;
			private double pressure;
			private double voltage;

			public SendDataDeltas() {
				super();
			}

			public SendDataDeltas(double temp, double humid, double press, double volt) {
				super();
				this.temperature = temp;
				this.humidity = humid;
				this.pressure = press;
				this.voltage = volt;
			}

			public double getTemperature() {
				return temperature;
			}

			public void setTemperature(double temperature) {
				this.temperature = temperature;
			}

			public double getHumidity() {
				return humidity;
			}

			public void setHumidity(double humidity) {
				this.humidity = humidity;
			}

			public double getPressure() {
				return pressure;
			}

			public void setPressure(double pressure) {
				this.pressure = pressure;
			}

			public double getVoltage() {
				return voltage;
			}

			public void setVoltage(double voltage) {
				this.voltage = voltage;
			}
		}

		private static int defaultSendDataInterval = 600;
		private static int defaultMeasureInterval = 600;
		private static ESPOperatingMode defaultEspOperatingMode = ESPOperatingMode.ESP_MODE_HIBERNATE;
		private static boolean defaultUseULP = false;
		private static SendDataDeltas defaultSendDataDeltas = new SendDataDeltas(2, 5, 10, 0.1);
		private static double defaultSwCutoffVoltage = 3.0;

		public static NodeConfig getDefaultConfig() {
			return new NodeConfig(defaultSendDataInterval, defaultMeasureInterval, defaultEspOperatingMode,
					defaultUseULP, defaultSendDataDeltas, defaultSwCutoffVoltage);
		}

		public int getSendDataInterval() {
			return sendDataInterval;
		}

		public void setSendDataInterval(int sendDataInterval) {
			this.sendDataInterval = sendDataInterval;
		}

		public int getMeasureInterval() {
			return measureInterval;
		}

		public void setMeasureInterval(int measureInterval) {
			this.measureInterval = measureInterval;
		}

		public ESPOperatingMode getEspOperatingMode() {
			return espOperatingMode;
		}

		public void setEspOperatingMode(ESPOperatingMode espOperatingMode) {
			this.espOperatingMode = espOperatingMode;
		}

		public boolean isUseULP() {
			return useULP;
		}

		public void setUseULP(boolean useULP) {
			this.useULP = useULP;
		}

		public SendDataDeltas getSendDataDeltas() {
			return sendDataDeltas;
		}

		public void setSendDataDeltas(SendDataDeltas sendDataDeltas) {
			this.sendDataDeltas = sendDataDeltas;
		}

		public double getSwCutoffVoltage() {
			return swCutoffVoltage;
		}

		public void setSwCutoffVoltage(double swCutoffVoltage) {
			this.swCutoffVoltage = swCutoffVoltage;
		}
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public NodeConfig getConfig() {
		return config;
	}

	public void setConfig(NodeConfig config) {
		this.config = config;
	}

	public List<NodeDatasinkConfigLinker> getDatasinksConfigs() {
		return datasinksConfigs;
	}

	public void setDatasinksConfigs(List<NodeDatasinkConfigLinker> datasinksConfigs) {
		this.datasinksConfigs = datasinksConfigs;
	}

	@Override
	public int hashCode() {
		return Objects.hash(mac);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		NodeEntity other = (NodeEntity) obj;
		return Objects.equals(mac, other.mac);
	}
}
