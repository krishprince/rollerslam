package rollerslam.display.gui;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;

import javax.swing.JOptionPane;

import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.display.Display;


/**
 * @author Marcos Aurélio
 *
 */
public class RollerslamMobileDisplay {

	private static World currentWorld = null;
	
	private static class ClientProcessor implements Runnable {
		private Socket s;
		
		public ClientProcessor(Socket s) {
			this.s = s;
		}

		public void run() {
			DataOutputStream dos = null;
			
			try {
				dos = new DataOutputStream(s.getOutputStream());
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
			
			while(true) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				try {
					sendData(dos);
				} catch (IOException e) {
					e.printStackTrace();
					break;
				}
			}
		}

		private void sendData(DataOutputStream dos) throws IOException {
			if (currentWorld != null) {
				int[] data = getWorldRepresentation();
				
				dos.writeInt(data.length);
				for(int i=0;i<data.length;++i) {
					dos.writeInt(data[i]);
				}
			}
		}

		private int[] getWorldRepresentation() {
			int[] data = new int[2 + 1 + currentWorld.playersA.length*2 + 1 + currentWorld.playersB.length*2];
			
			int pos = 0;
			
			data[pos++] = currentWorld.ball.s.x;
			data[pos++] = currentWorld.ball.s.y;
			
			data[pos++] = currentWorld.playersA.length;
			
			for(int i=0;i<currentWorld.playersA.length;++i) {
				data[pos++] = currentWorld.playersA[i].s.x;
				data[pos++] = currentWorld.playersA[i].s.y;
			}

			data[pos++] = currentWorld.playersB.length;

			for(int i=0;i<currentWorld.playersB.length;++i) {
				data[pos++] = currentWorld.playersB[i].s.x;
				data[pos++] = currentWorld.playersB[i].s.y;
			}
			
			return data;
		}
		
		
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	@SuppressWarnings("serial")
	public static void main(String[] args) throws Exception {
		ClientFacade facade = ClientFacadeImpl.getInstance(); 
		facade.getClientInitialization().init(
				JOptionPane.showInputDialog("Simulation host", "localhost"));
		facade.getDisplayRegistry().register(
				(Display) facade.getClientInitialization().exportObject(new Display() {
					public void update(Message m) throws RemoteException {
						if (m instanceof StateMessage) {
							currentWorld = (World) ((StateMessage) m).model;
						}
					}
				}));
		
		ServerSocket server = new ServerSocket(1100);
		while(true) {
			System.out.println("WAITING FOR CONNECTION...");
			Socket s = server.accept();
			System.out.println("CONNECTION ACCEPTED! " + s);
			new Thread(new ClientProcessor(s)).start();
		}		
	}

}
