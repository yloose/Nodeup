package de.yloose.nodeup.datasinks;

import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.yloose.nodeup.datasinks.WeatherDatapoints.WeatherDatapoint;

@Component
public class MqttSink extends WeatherdataSink {

	@Autowired
	WeatherDatapointsPublisher datasinkPublisher;

	public UUID sinkId = UUID.fromString("44553b4e-4021-11ed-b878-0242ac120002");

	private IMqttClient mqttClient;
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
		
		if (!validateConfig(config)) {
			// TODO: Invalid configuration
			return;
		}

		try {
			String publisherId = UUID.randomUUID().toString();
			mqttClient = new MqttClient(config.get("host").toString(), publisherId);

			MqttConnectOptions options = new MqttConnectOptions();
			options.setAutomaticReconnect(true);
			options.setCleanSession(true);
			options.setConnectionTimeout(10);
			if (config.containsKey("username")) {
				options.setUserName(config.get("username").toString());
				options.setPassword(config.get("password").toString().toCharArray());
			}
			mqttClient.connect(options);
			
			for(WeatherDatapoint datapoint : datapoints.getDatapoints()) {
				MqttMessage msg =  new MqttMessage(mapper.writeValueAsBytes(datapoint));
		        msg.setQos(0);
		        msg.setRetained(false);
		        mqttClient.publish(config.get("topic").toString().replace("%node%", datapoints.getNode().getDisplayName()), msg);
			}
			
			mqttClient.close();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
		} catch (JsonProcessingException e) {
			// TODO: handle exception
		}
	}
}
