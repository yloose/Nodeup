package de.yloose.nodeup.datasinks;


import java.util.UUID;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import de.yloose.nodeup.backend.models.DatasinkEntity;
import de.yloose.nodeup.backend.repository.DatasinkRepository;

@Component
public class DatabaseInitializer {
	
	@Autowired
	private DatasinkRepository datasinkRepository;

	private static Logger LOG = LoggerFactory.getLogger(DatabaseInitializer.class);
	
	@PostConstruct
	private void init() {
		try {
			datasinkRepository.save(new DatasinkEntity(UUID.fromString("44553b4e-4021-11ed-b878-0242ac120002"), "MQTT"));
		} catch (DataIntegrityViolationException e) {
			LOG.info("Datasink already added.");
			// TODO: Log exception
		}
	}
}
