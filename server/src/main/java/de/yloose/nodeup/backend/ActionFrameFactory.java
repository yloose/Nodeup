package de.yloose.nodeup.backend;

public class ActionFrameFactory {

	public static ActionFrame createActionFrame(byte[] frame) {
		int categoryCode = frame[0];
		
		switch(categoryCode) {
		case 127:
			return ESPNowFrame.parseESPNowFrame(frame);
		default:
			throw new UnsupportedOperationException("Not implemented yet.");
		}
	}
}
