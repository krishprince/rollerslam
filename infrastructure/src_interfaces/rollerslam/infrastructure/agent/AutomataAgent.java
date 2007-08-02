package rollerslam.infrastructure.agent;

import java.rmi.RemoteException;
import java.util.Set;

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
		
	public AutomataAgent(EnvironmentStateModel worldModel,
							Sensor sensor,
							Effector effector,
							ModelInitializationComponent initializationComponent,
							ActionInterpretationComponent interpretationComponent,
							RamificationComponent         ramificationComponent,
							ModelBasedBehaviorStrategyComponent strategyComponent) {
		this.worldModel              = worldModel;
		this.sensor				     = sensor;
		this.effector 				 = effector;
		this.initializationComponent = initializationComponent;
		this.interpretationComponent = interpretationComponent;
		this.ramificationComponent   = ramificationComponent;
		this.strategyComponent       = strategyComponent;

		new SimulationThread().start();	
	}
	
	private class SimulationThread extends Thread {
		public void run() {
			AutomataAgent.this.initializationComponent.initialize(worldModel);

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
	
	protected void processCycle() throws Exception {
		Set<Message> actions = sensor.getActions();
		
		for (Message message : actions) {
			interpretationComponent.processAction(worldModel, message);			
		}
		
		ramificationComponent.processRamifications(worldModel);
		
		Message action = strategyComponent.computeAction(worldModel);
		
		effector.doAction(action);
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
