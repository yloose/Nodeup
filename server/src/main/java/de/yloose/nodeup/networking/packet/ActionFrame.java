package de.yloose.nodeup.networking.packet;

public abstract class ActionFrame {

	private byte category;

	public ActionFrame(byte category) {
		this.category = category;
	}
		
	public byte[] toByteArray() {
		return new byte[] {this.category};
	}
	
	public byte getCategory() {
		return category;
	}
}
