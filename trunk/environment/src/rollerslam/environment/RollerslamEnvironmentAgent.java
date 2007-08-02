package rollerslam.environment;

import java.rmi.RemoteException;

import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.server.ServerFacadeImpl;

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
				try {
					return RollerslamEnvironmentAgent.this
							.getEnvironmentState();
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				return null;
			}
		};
	}
	
	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ServerFacadeImpl.getInstance().getServerInitialization().init(1099, new RollerslamEnvironmentAgent());		
	}	
}
