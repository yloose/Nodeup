package de.yloose.nodeup.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import de.yloose.nodeup.networking.NodeupFrame;
import de.yloose.nodeup.networking.NodeupFrameType;
import de.yloose.nodeup.networking.packet.ESPNowFrame;
import de.yloose.nodeup.networking.packet.Frame;
import de.yloose.nodeup.util.Conversion;
import jakarta.annotation.PostConstruct;

@Service
public class NetworkService {
	
	@Autowired
	private ApplicationContext context;
	
	private static Logger LOG = LoggerFactory.getLogger(NetworkService.class);
	
	private Long deviceHandle = null;
	
	@Value("${networkInterface.name:wlan1}")
	private String networkInterface;
	
	private native long initNetworkInterface(String device);
	private native int startListeningLoop(long deviceHandle);
	private native int sendPacket(long deviceHandle, byte packet[], int length);
			
	@PostConstruct
	public void init() {
		if (this.initNetwork(this.networkInterface)) {
			new Thread(this::start).start();
		}
	}
	
	public boolean initNetwork(String interfaceName) {
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
			LOG.error("Not all commands needed to initialize networking where found.");
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
		
		if (frame.getManagementFrame().getActionFrame() instanceof ESPNowFrame) {
			
			NodeupFrame nodeupFrame = ((ESPNowFrame) frame.getManagementFrame().getActionFrame()).getNodeupFrame();
			NodeupFrameWrapper nodeupFrameWrapper = new NodeupFrameWrapper(nodeupFrame, Conversion.macBytesToString(frame.getManagementFrame().getSA()));

			context.getBean(NodeupFrameType.getFrameHandlerByFrameClass(nodeupFrame.getClass())).handleFrame(nodeupFrameWrapper);
		}
	}
	
	public int sendPacket(byte packet[]) {
		// TODO: Maybe validate packet
		if (this.networkRunning())
			return this.sendPacket(deviceHandle, packet, packet.length);
		
		return 0;
	}
	
	public boolean networkRunning() {
		return this.deviceHandle != null;
	}

}
