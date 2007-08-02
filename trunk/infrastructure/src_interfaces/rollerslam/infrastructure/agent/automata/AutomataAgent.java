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

public class AutomataAgent implements Agent, SimulationStateProvider {
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
		
	public AutomataAgent() {
		new SimulationThread().start();	
	}
	
	private class SimulationThread extends Thread {
		public void run() {
			AutomataAgent.this.initialize();
			
			while (true) {
				while(state != SimulationState.RUNNING) {
					synchronized (AutomataAgent.this) {
						try {
							AutomataAgent.this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
						
				try {
					AutomataAgent.this.processCycle();
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					Thread.sleep(cycleDuration);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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

	protected void generateActions() {
		Message action = strategyComponent.computeAction(worldModel);
		
		try {
			effector.doAction(action);
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
	}

	protected void think() {
		ramificationComponent.processRamifications(worldModel);		
	}

	protected void interpretPerceptions() {
		Set<Message> actions;
		try {
			actions = sensor.getActions();

			for (Message message : actions) {
				interpretationComponent.processAction(worldModel, message);			
			}			
		} catch (RemoteException e) {
			e.printStackTrace();
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
