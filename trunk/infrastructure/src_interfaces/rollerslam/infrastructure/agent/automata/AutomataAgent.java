package rollerslam.infrastructure.agent.automata;

import java.rmi.RemoteException;
import java.util.Set;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Effector;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.Sensor;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;
import rollerslam.infrastructure.server.SimulationState;
import rollerslam.infrastructure.server.SimulationStateProvider;

public abstract class AutomataAgent implements Agent, SimulationStateProvider {
	protected static long cycleDuration = 100;

	public ServerFacade                           facade = ServerFacadeImpl.getInstance();
	
	protected EnvironmentStateModel               worldModel;
	protected ModelInitializationComponent        initializationComponent;
	protected ActionInterpretationComponent       interpretationComponent;
	protected RamificationComponent               ramificationComponent;
	protected ModelBasedBehaviorStrategyComponent strategyComponent;

	protected Sensor 						      sensor;
	protected Effector							  effector;
	
	private SimulationState state = SimulationState.CREATED;
        
        private int id = -1;
        
        public int getID() throws RemoteException{
            return id;
        }
        
        public void setID(int id) throws RemoteException{
            this.id = id;
        }
        
	protected void startSimulation() {
		new SimulationThread().start();			
	}
	
	private class SimulationThread extends Thread {
		public void run() {
			boolean onError = false;
			
			AutomataAgent.this.initialize();
			
			while (!onError) {
				while(state != SimulationState.RUNNING) {
					synchronized (AutomataAgent.this) {
						try {
							AutomataAgent.this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
							onError = true;
						}
					}
				}
						
				try {
					AutomataAgent.this.processCycle();
				} catch (Exception e) {
					e.printStackTrace();
					onError = true;
				}

				try {
					Thread.sleep(cycleDuration);
				} catch (InterruptedException e) {
					e.printStackTrace();
					onError = true;
				}
			}
			
			if (onError) {
				System.err.println("ERROR DETECTED! SIMULATION ABORTED");
				System.exit(-1);
			}
		}
	};
	
	protected void initialize() {
		AutomataAgent.this.initializationComponent.initialize(worldModel);		
	}

	protected void processCycle() throws Exception {
		interpretPerceptions();		
		think();
		generateActions();
	}

	protected void generateActions() throws RemoteException {
		Message action = strategyComponent.computeAction(worldModel);
			
		if (action != null) {
			effector.doAction(action);
		}
	}

	protected void think() {
		ramificationComponent.processRamifications(worldModel);		
	}

	protected void interpretPerceptions() throws RemoteException {
		Set<Message> actions;
		actions = sensor.getActions();

		for (Message message : actions) {
			interpretationComponent.processAction(worldModel, message);
		}
	}

	public SimulationStateProvider getSimulationStateProvider()
			throws RemoteException {
		return this;
	}

	public rollerslam.infrastructure.agent.Message getEnvironmentState()
			throws RemoteException {
		return new StateMessage(null, worldModel);
	}

	public SimulationState getState() throws RemoteException {
		return state;
	}

	public void run() throws RemoteException {
		state = SimulationState.RUNNING;
		synchronized (this) {
			this.notifyAll();
		}
	}

	public void stop() throws RemoteException {
		state = SimulationState.STOPPED;
		synchronized (this) {
			this.notifyAll();
		}
	}
	
	public static long getCycleDuration() {
		return cycleDuration;
	}

	public static void setCycleDuration(long cycleDuration) {
		AutomataAgent.cycleDuration = cycleDuration;
	}
			
}
