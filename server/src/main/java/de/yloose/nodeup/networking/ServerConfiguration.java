package de.yloose.nodeup.networking;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

import de.yloose.nodeup.models.NodeEntity.NodeConfig;
import de.yloose.nodeup.models.NodeEntity.NodeConfig.NodeOperatingMode;
import de.yloose.nodeup.models.NodeEntity.NodeConfig.SendDataDeltas;
import de.yloose.nodeup.util.Conversion;

public class ServerConfiguration extends NodeupFrame {

	private NodeOperatingMode operatingMode;
	private boolean commenceOta;
	private boolean requestInfo;
	private boolean enableUlp;
	private int measurementCycles;
	private int measurementInterval;
	private SendDataDeltas sendDataDeltas;
	private int cutoffVoltage;

	public static final int SERVER_CONFIGURATION_LENGTH = 17;

	public ServerConfiguration(byte[] data) {
		super(Arrays.copyOfRange(data, 0, NODEUP_FRAME_HEADER_LENGTH + 1));
	}

	public ServerConfiguration(NodeConfig config, boolean commenceOta, boolean requestInfo) {
		super(NodeupFrameType.SERVER_CONFIGURATION, 1, SERVER_CONFIGURATION_LENGTH);

		this.operatingMode = config.getOperatingMode();
		this.commenceOta = commenceOta;
		this.requestInfo = requestInfo;
		this.enableUlp = config.isUseULP();
		this.measurementCycles = config.getSendDataInterval() / config.getMeasureInterval();
		this.measurementInterval = config.getMeasureInterval();
		this.sendDataDeltas = config.getSendDataDeltas();
		this.cutoffVoltage = (int) (config.getSwCutoffVoltage() * 10);
	}

	public ServerConfiguration(NodeConfig config) {
		this(config, false, false);
	}

	@Override
	public byte[] toByteArray() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		try {
			stream.write(super.toByteArray());
			
			stream.write((byte) this.operatingMode.ordinal());
			stream.write(Conversion.booleanToByte(this.commenceOta) << 2
					| Conversion.booleanToByte(this.requestInfo) << 1 | Conversion.booleanToByte(this.enableUlp));
			stream.write((short) this.measurementCycles);
			stream.write((int) this.measurementInterval);

			stream.write((short) this.sendDataDeltas.getTemperature() * 10);
			stream.write((short) this.sendDataDeltas.getHumidity());
			stream.write((short) this.sendDataDeltas.getPressure());
			stream.write((byte) this.sendDataDeltas.getVoltage() * 10);

			stream.write((short) this.cutoffVoltage);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return stream.toByteArray();
	}

}
