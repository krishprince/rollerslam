package rollerslam.discoverer.server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import rollerslam.discoverer.util.HostAddress;

public class MulticastClientListener extends Thread {
	private HostAddress multicastAddress;
	private MulticastSocket socket = null;
	
	public MulticastClientListener() throws IOException {
		this("MulticastServerListener", 
				new HostAddress(InetAddress.getByName("230.0.0.1"), 4446));
	}

	public MulticastClientListener(HostAddress multicastAddress) throws IOException {
		this("MulticastServerListener", multicastAddress);
	}

	public MulticastClientListener(String name, HostAddress multicastAddress)
			throws IOException {
		super(name);
		this.multicastAddress = multicastAddress;
		this.socket = new MulticastSocket(this.multicastAddress.getPort());
	}
	
	public void run() {
		
		try {			
			socket.joinGroup(multicastAddress.getInnetAddress());
			
			while(true) {
				byte[] buf = new byte[256];
				
				DatagramPacket packet = new DatagramPacket(buf, buf.length);		
				System.out.println("Aguardando solicitacao...");
				socket.receive(packet);

				System.out.println("Pacote recebido!");
				
				//Informacoes do cliente
				InetAddress clientAddr = packet.getAddress();
				int clientPort = packet.getPort();
				
				packet = new DatagramPacket(buf, buf.length, clientAddr, clientPort);
				System.out.println("Enviando resposta para o cliente: " + clientAddr + ":" + clientPort);
				socket.send(packet);
				System.out.println("Pacote enviado: ");
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(this.socket != null)
				this.socket.close();
		}		
	}

	public HostAddress getMulticastAddress() {
		return multicastAddress;
	}

}
