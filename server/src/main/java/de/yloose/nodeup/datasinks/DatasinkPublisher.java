package de.yloose.nodeup.datasinks;

import java.util.concurrent.SubmissionPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatasinkPublisher<T extends Publishable> extends SubmissionPublisher<T> {
	
	private static Logger LOG = LoggerFactory.getLogger(DatasinkPublisher.class);

	public DatasinkPublisher() {
		super();
	}

	public void publish(T data) {
		LOG.debug("Publishing data to datasinks.");
		this.offer(data, (subscriber, dataOnError) -> {
			subscriber.onError(new RuntimeException("Data dropped: " + dataOnError.toString()));
			return true;
		});
	}
}
