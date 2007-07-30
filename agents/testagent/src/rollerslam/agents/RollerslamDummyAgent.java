package rollerslam.agents;

import java.rmi.RemoteException;

import rollerslam.agent.RollerslamAgent;
import rollerslam.environment.RollerslamEnvironment;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;

public class RollerslamDummyAgent implements RollerslamAgent, Runnable {
	public ClientFacade          facade      = null;	
	public RollerslamEnvironment environment = null;
	public Agent                 remoteThis  = null;

	public int agentID = -1;
	
	public RollerslamDummyAgent(RollerslamEnvironment environment) throws Exception {
		this.environment = environment;
		
		facade = ClientFacadeImpl.getInstance();
		remoteThis = facade.exportAgent(this, RollerslamDummyAgent.class);
	}

	@Override
	public void gameStarted(int yourAgentID) throws RemoteException {
		agentID = yourAgentID;

		synchronized (this) {
			this.notifyAll();			
		}
	}

	@Override
	public void run() {
		try {
			environment.joinWorld(remoteThis, PlayerTeam.TEAM_A);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		}
		
		while(agentID == -1) {
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		try {
			environment.dash(agentID, 900, 500);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		ClientFacade facade = ClientFacadeImpl.getInstance();
		facade.init(args[0]);		

		RollerslamDummyAgent realAgent = new RollerslamDummyAgent((RollerslamEnvironment) facade
				.getProxiedEnvironment(RollerslamEnvironment.class));
						
		new Thread(realAgent).run();		
	}
}
