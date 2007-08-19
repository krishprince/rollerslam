import java.rmi.RemoteException;
import rollerslam.repeater.server.RepeaterServer;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {		
		RepeaterServer server = RepeaterServer.getInstance();
		try {
			server.init("192.168.1.101");
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
