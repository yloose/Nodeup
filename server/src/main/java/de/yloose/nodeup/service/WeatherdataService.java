package de.yloose.nodeup.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.yloose.nodeup.datasinks.WeatherDatapoints;
import de.yloose.nodeup.datasinks.WeatherDatapointsPublisher;
import de.yloose.nodeup.models.NodeEntity;
import de.yloose.nodeup.models.WeatherDatapoint;
import de.yloose.nodeup.networking.NetworkService;
import de.yloose.nodeup.networking.ReceivedData;
import de.yloose.nodeup.networking.Weatherdata;
import de.yloose.nodeup.networking.Weatherdata.WeatherDatapointDto;
import de.yloose.nodeup.networking.packet.ESPNowFrame;
import de.yloose.nodeup.networking.packet.Frame;
import de.yloose.nodeup.networking.packet.PacketAnswerBuilder;
import de.yloose.nodeup.repository.NodeRepository;
import de.yloose.nodeup.repository.WeatherdataRepository;
import de.yloose.nodeup.util.Conversion;

@Service
public class WeatherdataService implements Flow.Subscriber<ReceivedData<Weatherdata>> {

	@Autowired
	private NodeRepository nodeRepository;

	@Autowired
	private WeatherdataRepository weatherdataRepository;

	@Autowired
	private WeatherDatapointsPublisher weatherDatapointPublisher;
	
	@Value("${networkInterface.name:wlan1}")
	private String networkInterface;

	private NetworkService<ReceivedData<Weatherdata>> networkService;

	Flow.Subscription subscription = null;

	private static Logger LOG = LoggerFactory.getLogger(WeatherdataService.class);

	private void handleWeatherdata(ReceivedData<Weatherdata> recvData) {
		LOG.info("Received data mac {}", recvData.getMac());

		Optional<NodeEntity> nodeOpt = nodeRepository.findByMac(recvData.getMac());

		NodeEntity node = nodeOpt.orElseGet(() -> {
			NodeEntity newNode = new NodeEntity();
			newNode.setMac(recvData.getMac());
			newNode.setConfig(NodeEntity.NodeConfig.getDefaultConfig());
			return newNode;
		});

		node = nodeRepository.save(node);

		// Send answer to node
		networkService.sendPacket(PacketAnswerBuilder.build(node));
		
		// Update datapoints in db and create datapoints ready to send to datasinks
		List<WeatherDatapoint> datapointsInDB = this.weatherdataRepository.findAllByNodeOrderByTimestampDesc(node);
		long nextIndex = this.weatherdataRepository.findFirstByNodeOrderByTimestampDesc(node)
				.map(wdp -> (wdp.getCounter() + 1) % 10).orElse(Long.valueOf(0));

		Collections.reverse(recvData.getData().getWeatherdataDatapoints());
		recvData.getData().calculateTimestamps(node.getConfig().getMeasureInterval());
		Collections.reverse(recvData.getData().getWeatherdataDatapoints());
		Set<WeatherDatapoint> datapoints = new HashSet<>();
		List<de.yloose.nodeup.datasinks.WeatherDatapoints.WeatherDatapoint> datapointListToSent = new ArrayList<>();
		for (WeatherDatapointDto datapoint : recvData.getData().getWeatherdataDatapoints()) {
			datapoints.add(new WeatherDatapoint(datapoint, node, (nextIndex++) % 10));
			datapointListToSent.add(new de.yloose.nodeup.datasinks.WeatherDatapoints.WeatherDatapoint(
					datapoint.getTimestamp(), datapoint.getTemperature(), datapoint.getHumidity(),
					datapoint.getPressure(), datapoint.getVoltage()));
		}

		datapoints.forEach(datapoint -> {
			Optional<WeatherDatapoint> existingDatapointOpt = datapointsInDB.stream().filter(dp -> dp.equals(datapoint))
					.findFirst();
			if (existingDatapointOpt.isPresent()) {
				existingDatapointOpt.get().setTimestamp(datapoint.getTimestamp());
				existingDatapointOpt.get().setTemperature(datapoint.getTemperature());
				existingDatapointOpt.get().setHumidity(datapoint.getHumidity());
				existingDatapointOpt.get().setPressure(datapoint.getPressure());
				existingDatapointOpt.get().setVoltage(datapoint.getVoltage());
				weatherdataRepository.save(existingDatapointOpt.get());
			} else {
				weatherdataRepository.save(datapoint);
			}
		});

		LOG.info("Number of datapoints received: " + datapoints.size());

		WeatherDatapoints datapointsToSent = new WeatherDatapoints(datapointListToSent, node);
		weatherDatapointPublisher.publishToSinks(datapointsToSent);

	}

	private ReceivedData<Weatherdata> parseFrame(Frame frame) {

		ReceivedData<Weatherdata> receivedData = new ReceivedData<Weatherdata>();

		ESPNowFrame espNowFrame = (ESPNowFrame) frame.getManagementFrame().getActionFrame();
		byte[] senderOui = espNowFrame.getOui();
		if ((senderOui[0] & 0xFF) != 0x18 || (senderOui[1] & 0xFF) != 0xfe || (senderOui[2] & 0xFF) != 0x34) {
			LOG.debug("Received packet did not originate with Espressifs OUI. Received OUI: {}",
					String.join(":", Arrays.asList(senderOui).stream().map(b -> String.format("%02X", b)).collect(Collectors.toList()))
			);
			return null;
		}
		
		Weatherdata weatherdata = new Weatherdata(espNowFrame.getVariable_data());		

		receivedData.setData(weatherdata);
		receivedData.setMac(Conversion.macBytesToString(frame.getManagementFrame().getSA()));

		return receivedData;
	}

	@PostConstruct
	private void init() {

		this.networkService = new NetworkService<ReceivedData<Weatherdata>>();
		this.networkService.setParseDataFunction(frame -> parseFrame(frame));
		ExecutorService execService = Executors.newSingleThreadExecutor();

		if (!this.networkService.init(networkInterface)) {
			LOG.warn("Failed to initialize network service. Procceding without capturing of data");
		} else {
			execService.submit(() -> this.networkService.start());
			this.networkService.subscribe(this);
		}
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(Throwable throwable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onNext(ReceivedData<Weatherdata> recvData) {
		handleWeatherdata(recvData);
		subscription.request(10);
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		subscription.request(10);
	}
}
