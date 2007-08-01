package rollerslam.environment;

import java.rmi.RemoteException;

import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.AutomataAgent;
import rollerslam.infrastructure.agent.EnvironmentStateModel;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.agent.ModelInitializationComponent;
import rollerslam.infrastructure.server.ServerFacadeImpl;
import rollerslam.infrastructure.server.SimulationStateProvider;

@SuppressWarnings("serial")
public class RollerslamEnvironmentAgent extends AutomataAgent {
	public RollerslamEnvironmentAgent() throws Exception {
		
		super(new World(), 
			
			  ServerFacadeImpl.getInstance().getEnvironmentSensor(),
			  
			  ServerFacadeImpl.getInstance().getEnvironmentEffector(),
				
 		      new ModelInitializationComponent() {
				public void initialize(EnvironmentStateModel model) {}
			  }, 
			  
			  new JavaActionInterpretationComponent(),
			  
			  new RamificationWorldVisitor(),
			  
			  new DefaultModelBasedBehaviorComponent());
		
		((DefaultModelBasedBehaviorComponent)strategyComponent).provider = this;
	}

	private static class DefaultModelBasedBehaviorComponent implements ModelBasedBehaviorStrategyComponent {

		public SimulationStateProvider provider;
		
		public Message computeAction(EnvironmentStateModel w) {
			try {
				return provider.getEnvironmentState();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
			return null;
		}
		
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		ServerFacadeImpl.getInstance().getServerInitialization().init(1099, new RollerslamEnvironmentAgent());		
	}	
}
