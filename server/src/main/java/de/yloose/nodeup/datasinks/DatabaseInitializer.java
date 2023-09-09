package de.yloose.nodeup.datasinks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import de.yloose.nodeup.models.DatasinkEntity;
import de.yloose.nodeup.repository.DatasinkRepository;
import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

	@Autowired
	private DatasinkRepository datasinkRepository;

	@Autowired
	private MqttSink mqttSink;

	private static Logger LOG = LoggerFactory.getLogger(DatabaseInitializer.class);

	@PostConstruct
	private void init() {
		try {
			datasinkRepository.save(new DatasinkEntity(mqttSink.getSinkId(), "MQTT"));
		} catch (DataIntegrityViolationException e) {
			LOG.info("Datasink already added.");
			// TODO: Log exception
		}
	}
}
