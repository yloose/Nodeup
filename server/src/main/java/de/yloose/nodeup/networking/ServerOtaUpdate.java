package de.yloose.nodeup.networking;

import java.util.Arrays;

public class ServerOtaUpdate extends NodeupFrame {

	public ServerOtaUpdate(byte[] data) {
		super(Arrays.copyOfRange(data, 0, NODEUP_FRAME_HEADER_LENGTH + 1));
	}
	
	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

}
