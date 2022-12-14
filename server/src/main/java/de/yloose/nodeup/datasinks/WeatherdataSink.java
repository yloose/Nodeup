package de.yloose.nodeup.datasinks;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Flow;
import java.util.concurrent.Flow.Subscription;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.yloose.nodeup.models.NodeDatasinkConfigLinker;
import de.yloose.nodeup.models.NodeEntity;
import de.yloose.nodeup.service.WeatherdataService;

public abstract class WeatherdataSink implements Flow.Subscriber<WeatherDatapoints> {

	private Flow.Subscription subscription;
	
	private static Logger LOG = LoggerFactory.getLogger(WeatherdataSink.class);

	public abstract void handleData(WeatherDatapoints datapoints, Map<String, Object> config);

	private Optional<Map<String, Object>> getSinkConfig(NodeEntity node) {
		Optional<Map<String, Object>> sinkConfig = Optional.empty();

		for (NodeDatasinkConfigLinker linker : node.getDatasinksConfigs()) {
			if (linker.getDatasink().getSinkId().equals(this.getSinkId())) {
				sinkConfig = Optional.of(linker.getDatasinkConfig());
				return sinkConfig;
			}
		}
		return sinkConfig;
	}

	abstract UUID getSinkId();
	
	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onError(Throwable throwable) {
		LOG.error("Failed to publish data to weatherdata sink: {}", throwable.getMessage());
	}

	@Override
	public void onNext(WeatherDatapoints data) {
		Optional<Map<String, Object>> configOpt = getSinkConfig(data.getNode());
		if (configOpt.isPresent()) {
			handleData(data, configOpt.get());
		}
		subscription.request(1);
	}

	@Override
	public void onSubscribe(Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}
}
