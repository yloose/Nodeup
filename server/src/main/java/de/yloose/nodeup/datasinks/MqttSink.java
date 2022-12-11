package de.yloose.nodeup.datasinks;

import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.yloose.nodeup.datasinks.WeatherDatapoints.WeatherDatapoint;
import de.yloose.nodeup.service.WeatherdataService;

@Component
public class MqttSink extends WeatherdataSink {

	@Autowired
	WeatherDatapointsPublisher datasinkPublisher;

	public UUID sinkId = UUID.fromString("44553b4e-4021-11ed-b878-0242ac120002");
	
	private static Logger LOG = LoggerFactory.getLogger(WeatherdataService.class);

	ObjectMapper mapper = new ObjectMapper();

	public MqttSink() {
		super();
	}

	@PostConstruct
	private void subscribe() {
		this.datasinkPublisher.subscribe(this);
	}
	
	private boolean validateConfig(Map<String, Object> config) {
		boolean isValid = true;
		if (
			!(config.get("host") instanceof String) &&
			!(config.get("topic") instanceof String)
			) {
			isValid = false;
		}
		return isValid;
	}

	@Override
	public void handleData(WeatherDatapoints datapoints, Map<String, Object> config) {
		
		LOG.debug("Sending datapoints over MQTT.");
		
		if (!validateConfig(config)) {
			// TODO: Invalid configuration
			LOG.debug("MQTT config validation failed.");
			return;
		}

	    IMqttClient mqttClient = null;

		try {
			String publisherId = UUID.randomUUID().toString();
			mqttClient = new MqttClient(config.get("host").toString(), publisherId);
			LOG.debug("Mqtt client {} created.", mqttClient);
			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			if (config.containsKey("username")) {
				options.setUserName(config.get("username").toString());
				options.setPassword(config.get("password").toString().toCharArray());
			}
			mqttClient.connect(options);
			LOG.debug("Mqtt client connected with options {}.", options);
			LOG.debug("{} weather datapoints to publish.", datapoints.getDatapoints().size());
			
			
			for(WeatherDatapoint datapoint : datapoints.getDatapoints()) {
				MqttMessage msg =  new MqttMessage(mapper.writeValueAsBytes(datapoint));
				LOG.debug("Mqtt message {} created.", msg);

		        msg.setQos(0);
		        msg.setRetained(false);
		        mqttClient.publish(config.get("topic").toString().replace("%node%", datapoints.getNode().getDisplayName()), msg);
				LOG.debug("Mqtt message {} published to topic {}.", msg, config.get("topic").toString().replace("%node%", datapoints.getNode().getDisplayName()));
			}
			
			LOG.debug("Successfully send datapoints over MQTT sink.");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			LOG.error("Failed to sent mqtt packet.", e);
		} catch (JsonProcessingException e) {
			// TODO: handle exception
			LOG.debug("Could not parse MQTT sink configuration: {}", e.getMessage());
		} finally {
			try {
				mqttClient.disconnect();
				mqttClient.close();
			} catch (MqttException e) {
				LOG.error("Error closing Mqtt client.", e);
			}

		}
	}

	@Override
	UUID getSinkId() {
		return this.sinkId;
	}
}
