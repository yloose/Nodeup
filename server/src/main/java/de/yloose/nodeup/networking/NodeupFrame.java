package de.yloose.nodeup.networking;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Random;

public abstract class NodeupFrame {
	
	private NodeupFrameType type;
	private int version;
	private int size;
	private byte[] magic;
	
	public static final int NODEUP_FRAME_HEADER_LENGTH = 4;
	
	public NodeupFrame(byte[] data) {
		this.type = NodeupFrame.getFrameTypeFromData(data);
		this.version = (data[0] << 4) >> 4;
		this.size = data[1];
		this.magic = Arrays.copyOfRange(data, 2, 4);
	}
	
	public NodeupFrame(NodeupFrameType type, int version, int size) {
		this.type = type;
		this.version = version;
		this.size = NODEUP_FRAME_HEADER_LENGTH + size;
		this.magic = new byte[2];
		new Random().nextBytes(this.magic);
	}
	
	public static NodeupFrameType getFrameTypeFromData(byte[] data) {
		return NodeupFrameType.values()[data[0] >> 4];
	}
	
	public byte[] toByteArray() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		stream.write((byte) this.type.ordinal() << 4 | (byte) this.version);
		stream.write(this.size);
		stream.write(this.version);
		
		return stream.toByteArray();
	};

	public NodeupFrameType getType() {
		return type;
	}

	public int getVersion() {
		return version;
	}

	public int getSize() {
		return size;
	}

	public byte[] getMagic() {
		return magic;
	}
}
