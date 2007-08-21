import java.io.IOException;

import rollerslam.discoverer.server.MulticastClientListener;


public class ServerStarter {
	public static void main(String[] args) {
		try {
			new MulticastClientListener().start();
		} catch (IOException e) {	
			e.printStackTrace();
		}

	}
}
