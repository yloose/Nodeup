package de.yloose.nodeup.networking;

import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.Executors;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.yloose.nodeup.networking.packet.Frame;
import de.yloose.nodeup.util.Conversion;

public class NetworkService<T> extends SubmissionPublisher<T> {
	
	private static Logger LOG = LoggerFactory.getLogger(NetworkService.class);
	
	private long deviceHandle;

	private Function<Frame, T> parseDataFunction;
	
	private native long initNetworkInterface(String device);
	private native int startListeningLoop(long deviceHandle);
	private native int sendPacket(long deviceHandle, byte packet[], int length);
			
	public NetworkService() {
		super(Executors.newFixedThreadPool(10), 5);
	}
	
	public boolean init(String interfaceName) {
		long ret = initNetworkInterface(interfaceName);
		if (ret == -1) {
			LOG.error("Failed to intialize network interface.");
			return false;
		}
		else if (ret == -2) {
			LOG.error("Failed to open network interface.");
			return false;
		}
		else if (ret == 1) {
			LOG.error("Could not find network interface.");
			return false;
		}
		else if (ret == -3) {
			LOG.error("Not all commands needed to initialize network where found.");
			return false;
		} else {
			deviceHandle = ret;
			return true;
		}
	}
	
	public int start() {
		return startListeningLoop(deviceHandle);
	}
	
	public void nativeRecvPacketCallback(byte[] packet) {
		LOG.info("Received packet. Length: {}", packet.length);

		Frame frame = new Frame(packet);
		
		if (frame.getManagementFrame().getActionFrame().getCategory() != 127) {
			// TODO: Maybe log mismatching packet
			return;			
		}
		
		T data = parseDataFunction.apply(frame);
		if (data == null) {
			LOG.info("A packet has been discarded by parsing function. Sender: " + Conversion.macBytesToString(frame.getManagementFrame().getSA()));
			LOG.trace(frame.toString());
			return;
		}
		
		this.offer(data, (subscriber, weatherData) -> {
			subscriber.onError(new RuntimeException("Weatherdata dropped."));
			return true;
		});					
	}
	
	public int sendPacket(byte packet[]) {
		// TODO: Maybe validate packet
		return this.sendPacket(deviceHandle, packet, packet.length);
	}
	
	public void setParseDataFunction(Function<Frame, T> parseDataFunction) {
		this.parseDataFunction = parseDataFunction;
	}

}
