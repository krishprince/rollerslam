package rollerslam.environment;

import java.rmi.RemoteException;
import java.util.Set;

import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.EnvironmentAgent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;
import rollerslam.infrastructure.server.SimulationState;
import rollerslam.infrastructure.server.SimulationStateProvider;

@SuppressWarnings("serial")
public class RollerslamEnvironmentAgent implements EnvironmentAgent, SimulationStateProvider {
	protected static final long WAITING_TIME = 100;
	public ServerFacade               facade                        = ServerFacadeImpl.getInstance();
	public World 		              worldModel                    = new World();
	public RamificationComponent      ramificationsHandler          = new RamificationWorldVisitor();
	public ActionInterpretationComponent          actionInterpretationComponent = new JavaActionInterpretationComponent();
	
	private SimulationState state = SimulationState.CREATED;
	private boolean 	    running = false;

	private class SimulationThread extends Thread {
		public void run() {
			while (true) {
				while(!running) {
					synchronized (RollerslamEnvironmentAgent.this) {
						try {
							RollerslamEnvironmentAgent.this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
				
				System.out.println("ALOHA PROCESSING CYCLE: " + running);
				try {
					RollerslamEnvironmentAgent.this.processCycle();
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(WAITING_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	};
	
	public RollerslamEnvironmentAgent(int a) {
		new SimulationThread().start();
	}
	
	protected void processCycle() throws Exception {
		Set<Message> actions = facade.getEnvironmentSensor().getActions();
		
		for (Message message : actions) {
			actionInterpretationComponent.processAction(worldModel, message);			
		}
		
		ramificationsHandler.processRamifications(worldModel);		
		worldModel.accept(new DumpWorldVisitor());
	}

	public SimulationStateProvider getSimulationStateProvider()
			throws RemoteException {
		return this;
	}

	public rollerslam.infrastructure.agent.Message getEnvironmentState()
			throws RemoteException {
		return new StateMessage(worldModel);
	}

	public SimulationState getState() throws RemoteException {
		return state;
	}

	public void run() throws RemoteException {
		state = SimulationState.RUNNING;
		running = true;
		synchronized (this) {
			this.notifyAll();			
		}
	}

	public void stop() throws RemoteException {
		state = SimulationState.STOPPED;
		running = false;
		synchronized (this) {
			this.notifyAll();			
		}
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ServerFacadeImpl.getInstance().getServerInitialization().init(1099, new RollerslamEnvironmentAgent(2));		
	}	
}
