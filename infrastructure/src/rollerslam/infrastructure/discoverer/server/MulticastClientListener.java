package rollerslam.infrastructure.discoverer.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import rollerslam.infrastructure.server.PrintTrace;

public class MulticastClientListener extends Thread {
	@SuppressWarnings("unused")
	private String multicastAddress;
	private int    multicastPort;
	private MulticastSocket socket = null;
	
	public MulticastClientListener() throws IOException {
		this("255.255.255.255", 14446);
	}

	public MulticastClientListener(String multicastAddress) throws IOException {
		this(multicastAddress, 14446);
	}

	public MulticastClientListener(String multicastAddress, int port)
			throws IOException {
		super("MulticastServerListener");
		this.multicastAddress = multicastAddress;
		this.multicastPort = port;
		this.socket = new MulticastSocket(multicastPort);
	}
	
	public void run() {
		
		try {			
			System.out.println("MULTICAST SERVER STARTED. WAITING...");
			
			while(true) {
				byte[] buf = new byte[256];
				
				DatagramPacket packet = new DatagramPacket(buf, buf.length);		
				socket.receive(packet);
				//System.out.println("DATAGRAM PACKET RECEIVED");
				
				//Informacoes do cliente
				InetAddress clientAddr = packet.getAddress();
				int clientPort = packet.getPort();
				
				packet = new DatagramPacket(buf, buf.length, clientAddr, clientPort);
				//System.out.println("SENDIND DATAGRAM PACKET TO CLIENT: " + clientAddr + ":" + clientPort);
				socket.send(packet);
			}
			
		} catch (IOException e) {
			if (PrintTrace.TracePrint){
				e.printStackTrace();
			}
		} finally {
			if(this.socket != null)
				this.socket.close();
		}		
	}
}
