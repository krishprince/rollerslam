import java.io.IOException;

import rollerslam.discoverer.client.MulticastServerDiscoverer;


public class ClientTest {
	public static void main(String[] args) {
		try {
			new MulticastServerDiscoverer().findServer();
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
}
