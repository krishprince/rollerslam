package rollerslam.environment;

import java.rmi.RemoteException;

import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;
import rollerslam.logging.EnvironmentStateLogEntry;

@SuppressWarnings("serial")
public class RollerslamEnvironmentAgent extends AutomataAgent {
	public RollerslamEnvironmentAgent() throws Exception {

		this.worldModel = new World();
		this.sensor = ServerFacadeImpl.getInstance().getEnvironmentSensor();
		this.effector = ServerFacadeImpl.getInstance().getEnvironmentEffector();
		this.initializationComponent = new ModelInitializationComponent() {
			public void initialize(EnvironmentStateModel model) {
			}
		};

		this.interpretationComponent = new JavaActionInterpretationComponent();

		this.ramificationComponent = new RamificationWorldVisitor();

		this.strategyComponent = new ModelBasedBehaviorStrategyComponent() {
			public Message computeAction(EnvironmentStateModel w) {
				ServerFacade facade = ServerFacadeImpl.getInstance();
				try {
					Message m = RollerslamEnvironmentAgent.this
					.getEnvironmentState();
					
					EnvironmentStateLogEntry envLog = new EnvironmentStateLogEntry(((World)((StateMessage)m).model).currentCycle, -1, ((World)((StateMessage)m).model).getSimpleWorld());

					facade.getLogRecordingService().addEntry(envLog);

					return m;
				} catch (RemoteException e) {
					if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
					
				}
				return null;
			}
		};
		
		startSimulation();
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ServerFacadeImpl.getInstance().getServerInitialization().init(1099, new RollerslamEnvironmentAgent());		
        }
}
