package rollerslam.infrastructure.client.communication;

import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;

public class SocketCommunicationFacade implements CommunicationFacade {

	private static CommunicationFacade instance;
	private SimulationObserver observer;

	public static CommunicationFacade getInstance() {
		if (instance == null) {
			instance = new SocketCommunicationFacade();
		}
		return instance;
	}

	public void connect(String host, final SimulationObserver obs)
			throws Exception {
		this.observer = obs;

		StreamConnection con = (StreamConnection) Connector.open("socket://"
				+ host + ":1100");

		final DataInputStream dis = con.openDataInputStream();

		new Thread(new Runnable() {

			public void run() {
				try {
					while (true) {
						int qtd = dis.readInt();
						final int[] data = new int[qtd];

						for (int i = 0; i < qtd; ++i) {
							data[i] = dis.readInt();
						}

						if (observer != null) {
							observer.notify(new SimulationState() {
								public Object getData() {
									return data;
								}
							});
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}).start();
	}

}
