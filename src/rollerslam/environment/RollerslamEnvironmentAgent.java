package rollerslam.environment;

import java.net.Inet4Address;
import java.rmi.RemoteException;

import com.parctechnologies.eclipse.EclipseConnection;
import com.parctechnologies.eclipse.RemoteEclipse;

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
	
	public EclipseConnection eclipse;
	
	public RollerslamEnvironmentAgent() throws Exception {

		eclipse = new RemoteEclipse(Inet4Address.getByName("localhost"), 1023); 

		this.worldModel = new World();
		this.sensor = ServerFacadeImpl.getInstance().getEnvironmentSensor();
		this.effector = ServerFacadeImpl.getInstance().getEnvironmentEffector();
		this.initializationComponent = new ModelInitializationComponent() {
			public void initialize(EnvironmentStateModel model) {
			}
		};

		this.interpretationComponent = new JavaActionInterpretationComponent();

		this.ramificationComponent = new FluxRamificationComponent(eclipse);

		this.strategyComponent = new ModelBasedBehaviorStrategyComponent() {
			public Message computeAction(EnvironmentStateModel w) {
				ServerFacade facade = ServerFacadeImpl.getInstance();
				try {
					Message m = RollerslamEnvironmentAgent.this
					.getEnvironmentState();

					EnvironmentStateLogEntry envLog = new EnvironmentStateLogEntry(((World)((StateMessage)m).model).currentCycle, -1, (World)((StateMessage)m).model);

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
		ServerFacadeImpl.getInstance().getServerInitialization().init(1099,
				new RollerslamEnvironmentAgent());
	}
}
