package de.yloose.nodeup.datasinks;

import java.util.concurrent.SubmissionPublisher;

public class DatasinkPublisher<T> extends SubmissionPublisher<T> {

	public DatasinkPublisher() {
		super();
	}

	public void publishToSinks(T data) {
		this.offer(data, (subscriber, dataOnError) -> {
			subscriber.onError(new RuntimeException("Data dropped"));
			return true;
		});
	}
}
