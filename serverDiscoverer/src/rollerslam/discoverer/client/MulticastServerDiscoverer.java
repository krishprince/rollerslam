package rollerslam.discoverer.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import rollerslam.discoverer.util.HostAddress;

public class MulticastServerDiscoverer implements ServerDiscoverer {
	private HostAddress multicastAddress;
	private HostAddress serverAddress;	
	private DatagramSocket socket = null;
	private int port;	
	
	public MulticastServerDiscoverer() throws IOException {
		this(new HostAddress(InetAddress.getByName("230.0.0.1"), 4446), 4447);
	}
	
	public MulticastServerDiscoverer(HostAddress multicastAddress) throws IOException {
		this(multicastAddress, multicastAddress.getPort());
	}
	
	public MulticastServerDiscoverer(HostAddress multicastAddress, int port) throws IOException {
		
		this.multicastAddress = multicastAddress;
		this.port = port;
		this.socket = new DatagramSocket(this.port);
		socket.setSoTimeout(20000);
	}
	
	public void findServer() throws IOException {
		serverAddress = null;
		
		try {
			System.out.println("Procurando Servidores...");
			System.out.print("Enviando Sinal...");
			
			sendPackage();
			System.out.println("ok");
			
			System.out.print("Aguardando Resposta..");
			
			receivePackage();
			
			System.out.println("ok");
			System.out.println("Servidor encontrado: " + serverAddress.toString() );
			System.out.println("Concluído!");
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {			
			if(this.socket != null)
				this.socket.close();
		}
	}
	
	private void sendPackage() throws IOException {
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length,
				multicastAddress.getInnetAddress(), multicastAddress.getPort());
		socket.send(packet);
	}
	
	private void receivePackage() throws IOException {
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		
		//obtendo dados do servidor
		serverAddress = new HostAddress(packet.getAddress(), packet.getPort());
	}

	public HostAddress getMulticastAddress() {
		return multicastAddress;
	}

	public void setMulticastAddress(HostAddress multicastAddress) {
		this.multicastAddress = multicastAddress;
	}

	public int getPort() {
		return port;
	}

	public HostAddress getServerAddress() {
		return serverAddress;
	}

	public int getTimeout() {
		int timeout = 0;
		try {
			timeout = this.socket.getSoTimeout();
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return timeout;
	}

	public void setTimeout(int timeout) {
		try {
			this.socket.setSoTimeout(timeout);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
