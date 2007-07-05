package rollerslam.infrastructure.client;

import java.rmi.RemoteException;



public class StartSimulation {
	public static void main(String[] args) throws RemoteException {
		ClientFacade.init(args[0]);
		ClientFacade.getInstance().getSimulationAdmin().run();
	}
}
