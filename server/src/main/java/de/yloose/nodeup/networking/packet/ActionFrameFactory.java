package de.yloose.nodeup.networking.packet;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionFrameFactory {

	private static Logger LOG = LoggerFactory.getLogger(ActionFrameFactory.class);

	
	public static ActionFrame createActionFrame(byte[] data) {
		
		switch ((int) data[0]) {
		case 127:
			ESPNowFrame f = new ESPNowFrame(data);
			if (f.isESPNow())
				return f;
			
			LOG.debug("Parsed packet did not originate with Espressifs OUI. Received OUI: {}",
					String.join(":", Arrays.asList(f.getOui()).stream().map(b -> String.format("%02X", b)).collect(Collectors.toList())));
			return null;
		default:
			return null;
		}
	}
}
