package de.yloose.nodeup.networking.packet;

public interface ActionFrame {

	public int getCategory();
	
	public byte[] toByteArray();
}
