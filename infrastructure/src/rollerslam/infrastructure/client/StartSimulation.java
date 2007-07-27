package rollerslam.infrastructure.client;

import java.rmi.RemoteException;



public class StartSimulation {
	public static void main(String[] args) throws RemoteException {
		ClientFacadeImpl.getInstance().init(args[0]);
		ClientFacadeImpl.getInstance().getSimulationAdmin().run();
	}
}
