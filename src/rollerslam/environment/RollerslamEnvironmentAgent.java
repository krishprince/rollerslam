package rollerslam.environment;

import java.io.File;
import java.rmi.RemoteException;

import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;
import rollerslam.logging.EnvironmentStateLogEntry;

import com.parctechnologies.eclipse.EclipseConnection;
import com.parctechnologies.eclipse.EclipseEngineOptions;
import com.parctechnologies.eclipse.EmbeddedEclipse;

@SuppressWarnings("serial")
public class RollerslamEnvironmentAgent extends AutomataAgent {
	
	public EclipseConnection eclipse;
	
	public RollerslamEnvironmentAgent() throws Exception {
		cycleDuration = 1000;
		
		initializeEclipseConnection();
		
		this.worldModel = new World();
		this.sensor = ServerFacadeImpl.getInstance().getEnvironmentSensor();
		this.effector = ServerFacadeImpl.getInstance().getEnvironmentEffector();
		this.initializationComponent = new ModelInitializationComponent() {
			public void initialize(EnvironmentStateModel model) {
			}
		};

		this.interpretationComponent = new FluxActionInterpretationComponent(eclipse);

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
	
	private void initializeEclipseConnection() throws Exception {
	    System.setProperty("eclipse.directory", "D:\\ECLiPSe 5.10");
	    String folder = "C:\\Temp\\maas\\1709\\rollerslam_workspace\\environment\\flux\\";

	    EclipseEngineOptions eclipseEngineOptions = new EclipseEngineOptions();
	    File eclipseProgram;
	    
	    eclipseEngineOptions.setUseQueues(false);
	    eclipse = EmbeddedEclipse.getInstance(eclipseEngineOptions);
	    
	    eclipseProgram = new File(folder+"flux.pl");
	    eclipse.compile(eclipseProgram);
	    
	    eclipseProgram = new File(folder+"fluent.chr");
	    eclipse.compile(eclipseProgram);
	    
	    eclipseProgram = new File(folder+"rollerslam.pl");
	    eclipse.compile(eclipseProgram);
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
