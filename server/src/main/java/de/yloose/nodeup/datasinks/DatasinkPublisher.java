package de.yloose.nodeup.datasinks;

import java.util.concurrent.SubmissionPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.yloose.nodeup.service.WeatherdataService;

public class DatasinkPublisher<T> extends SubmissionPublisher<T> {
	
	private static Logger LOG = LoggerFactory.getLogger(WeatherdataService.class);

	public DatasinkPublisher() {
		super();
	}

	public void publishToSinks(T data) {
		LOG.debug("Publishing data to datasinks.");
		this.offer(data, (subscriber, dataOnError) -> {
			subscriber.onError(new RuntimeException("Data dropped: " + dataOnError.toString()));
			return true;
		});
	}
}
