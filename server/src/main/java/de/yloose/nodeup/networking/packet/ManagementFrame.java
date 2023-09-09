package de.yloose.nodeup.networking.packet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.zip.CRC32;

public class ManagementFrame {
	
	private byte[] frame_control;
	private byte[] duration;
	private byte[] da;
	private byte[] sa;
	private byte[] bssid;
	private byte[] seq_ctrl;
	
	private ActionFrame actionFrame;
	
	private byte[] fcs;
	
	public ManagementFrame(byte[] frame) {
		
		this.frame_control = Arrays.copyOfRange(frame, 0, 2);
		this.duration = Arrays.copyOfRange(frame, 2, 4);
		this.da = Arrays.copyOfRange(frame, 4, 10);
		this.sa = Arrays.copyOfRange(frame, 10, 16);
		this.bssid = Arrays.copyOfRange(frame, 16, 22);
		this.seq_ctrl = Arrays.copyOfRange(frame, 22, 24);
		
		this.actionFrame = ActionFrameFactory.createActionFrame(Arrays.copyOfRange(frame, 24, 289));
				
		if (frame.length > 285) {
			this.fcs = Arrays.copyOfRange(frame, frame.length - 4, frame.length);
		}
	}
	
	public ManagementFrame(int subtype, byte[] destination, byte[] source) {
		this.frame_control = new byte[] {(byte) (subtype << 4), 0};
		
		this.duration = new byte[] {0, 0};
		
		this.da = destination;
		this.sa = source;
		this.bssid = destination;
		
		this.seq_ctrl = new byte[2];
		this.seq_ctrl[0] = 32;
		this.seq_ctrl[1] = 0;
	}
	
	public static ManagementFrame createManagementFrame(byte[] destMac) {
		return new ManagementFrame(13, destMac, new byte[] { (byte) 0xce, 0x50, (byte) 0xe3, 0x26, 0x0b, (byte) 0xf7 });
	}
	
	public void calculateFcs() {
		CRC32 crc = new CRC32();
		crc.update(this.frame_control);
		crc.update(this.duration);
		crc.update(this.da);
		crc.update(this.sa);
		crc.update(this.bssid);
		crc.update(this.seq_ctrl);
		crc.update(this.bssid);
		crc.update(this.actionFrame.toByteArray());
		
		long crcInt = (int) crc.getValue();
		
		// CRC Int to byte[]
		byte[] crcBytes = new byte[] {
	            (byte)(crcInt >>> 24),
	            (byte)(crcInt >>> 16),
	            (byte)(crcInt >>> 8),
	            (byte)crcInt};
		
		this.fcs = crcBytes;
	}
	
	public byte[] toByteArray() {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		try {
			stream.write(this.frame_control);
			stream.write(this.duration);
			stream.write(this.da);
			stream.write(this.sa);
			stream.write(this.bssid);
			stream.write(this.seq_ctrl);
			stream.write(this.actionFrame.toByteArray());
			stream.write(this.fcs);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return stream.toByteArray();
	}

	public ActionFrame getActionFrame() {
		return actionFrame;
	}
	public void setActionFrame(ActionFrame actionFrame) {
		this.actionFrame = actionFrame;
	}
	public byte[] getSA() {
		return sa;
	}
}
