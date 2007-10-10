package rollerslam.infrastructure.discoverer.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

import rollerslam.infrastructure.server.PrintTrace;
	
public class MulticastServerDiscoverer implements ServiceDiscoverer {
	private String multicastAddress;
	private int    multicastPort;
	private Set<InetAddress> serverAddress = new HashSet<InetAddress>();	
	private DatagramSocket socket = null;
	private int port;	
	
	public MulticastServerDiscoverer() throws IOException {		
		this("255.255.255.255", 14446, 14447);
	}
	
	public MulticastServerDiscoverer(String multicastAddress, int multicastPort) throws IOException {
		this(multicastAddress, multicastPort, 14447);
	}
	
	public MulticastServerDiscoverer(String multicastAddress, int multicastPort, int listenPort) throws IOException {
		this.multicastAddress = multicastAddress;
		this.multicastPort    = multicastPort;
		
		tryPort(listenPort);

		System.out.println("OPEN SOCKET TO PORT " + this.port);
		socket.setSoTimeout(5000);
	}
	
	private void tryPort(int listenPort) {
		int chances = 100;
		this.port = listenPort;
		
		while(true) {
			try {
				--chances;
				this.socket = new DatagramSocket(this.port);
				break;
			} catch (SocketException e) {
				this.port ++;
			}
			
			if (chances < 0) {
				break;
			}				
		}
		
	}
	public void findServer() throws IOException {
		clearCache();
		
		new Thread() {
			public void run() {
				long started = System.currentTimeMillis();
								
				while (System.currentTimeMillis() - started < 30 * 1000) {
					//System.out.println("SEARCHING SERVERS...");
					//System.out.print("SENDIND SIGNAL...");
					
					try {
						sendPackage();
						//System.out.println("ok");
					} catch (IOException e2) {
						if (PrintTrace.TracePrint){
							e2.printStackTrace();
						}
						break;
					}					
					
					byte[] buf = new byte[256]; 
					DatagramPacket packet = new DatagramPacket(buf, buf.length);

					try {
						//System.out.print("WAITING...");
						
						socket.receive(packet);

						// obtendo dados do servidor
						synchronized (serverAddress) {
							serverAddress.add(packet.getAddress());
						}
						
						//System.out.println("SERVER FOUND: "
								//+ serverAddress.toString());
						//System.out.println("OK!");
					} catch (Exception e1) {
						if (PrintTrace.TracePrint){
							e1.printStackTrace();
						}
					}

					
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						if (PrintTrace.TracePrint){
							e.printStackTrace();
						}
					}
				}
			}
		}.start();
	}
	
	private void sendPackage() throws IOException {
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length,
				InetAddress.getByName(multicastAddress), multicastPort);
		socket.send(packet);
		//System.out.println("SENT " + packet.getAddress() + ":" + packet.getPort());
	}

	public int getTimeout() {
		int timeout = 0;
		try {
			timeout = this.socket.getSoTimeout();
		} catch (SocketException e) {
			if (PrintTrace.TracePrint){
				e.printStackTrace();
			}
		}
		
		return timeout;
	}

	public void setTimeout(int timeout) {
		try {
			this.socket.setSoTimeout(timeout);
		} catch (SocketException e) {
			if (PrintTrace.TracePrint){
				e.printStackTrace();
			}
		}
	}

	public void clearCache() {
		serverAddress.clear();
	}

	public Set<InetAddress> getFoundAddresses() {
		synchronized (serverAddress) {
			return new HashSet<InetAddress>(serverAddress);
		}
	}
}
