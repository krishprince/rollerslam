package rollerslam.discoverer.util;

import java.net.InetAddress;

public class HostAddress {
	private InetAddress innetAddress;
	private int port;
	
	public HostAddress(InetAddress hostAddress, int port) {
		super();
		this.innetAddress = hostAddress;
		this.port = port;
	}

	public InetAddress getInnetAddress() {
		return innetAddress;
	}

	public void setInnetAddress(InetAddress hostAddress) {
		this.innetAddress = hostAddress;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	public String toString() {		
		return this.innetAddress.toString() + ":" + this.port ;
	}
}

