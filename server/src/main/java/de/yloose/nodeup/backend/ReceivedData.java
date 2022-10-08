package de.yloose.nodeup.backend;

public class ReceivedData<T> {

	private T data;
	private String mac;
	
	
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
}
