package rollerslam.environment;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Set;

import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.CompositeAction;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;
import rollerslam.infrastructure.settings.GeneralSettingsImpl;
import rollerslam.logging.EnvironmentStateLogEntry;

import com.parctechnologies.eclipse.EclipseConnection;
import com.parctechnologies.eclipse.EclipseEngineOptions;
import com.parctechnologies.eclipse.EmbeddedEclipse;

@SuppressWarnings("serial")
public class RollerslamEnvironmentAgent extends AutomataAgent {

	public EclipseConnection eclipse;

	public RollerslamEnvironmentAgent() throws Exception {
		initializeEclipseConnection();

		this.worldModel = new EnvironmentWorldModel();
		new World();
		this.sensor = ServerFacadeImpl.getInstance().getEnvironmentSensor();
		this.effector = ServerFacadeImpl.getInstance().getEnvironmentEffector();
		this.initializationComponent = new ModelInitializationComponent() {
			public void initialize(EnvironmentStateModel model) {
			}
		};

		this.interpretationComponent = new FluxActionInterpretationComponent(
				eclipse);

		this.ramificationComponent = new FluxRamificationComponent(eclipse);

		this.strategyComponent = new ModelBasedBehaviorStrategyComponent() {
			public Message computeAction(EnvironmentStateModel wout) {
				ServerFacade facade = ServerFacadeImpl.getInstance();
				try {
					World model = ((EnvironmentWorldModel)wout).getWorld();

					EnvironmentStateLogEntry envLog = new EnvironmentStateLogEntry(
							model.currentCycle,
							-1, model);

					facade.getLogRecordingService().addEntry(envLog);

					return new StateMessage(null, model);
				} catch (RemoteException e) {
					if (PrintTrace.TracePrint) {
						e.printStackTrace();
					}

				}
				return null;
			}
		};

		startSimulation();
	}

	public rollerslam.infrastructure.agent.Message getEnvironmentState()
			throws RemoteException {
		EnvironmentWorldModel wout = ((EnvironmentWorldModel)worldModel);
		wout.updateWorld();
		return new StateMessage(null, wout.getWorld());
	}
	
	protected void processCycle() throws Exception {	
		long bef = System.currentTimeMillis();
		super.processCycle();
		long aft = System.currentTimeMillis();
		
		System.out.println("ELAPSED TIME: " + (aft - bef));
	}

//	protected void think() {
//		ramificationComponent.processRamifications(worldModel);		
//	}
	
	protected void interpretPerceptions() throws RemoteException {		
		interpretationComponent.processAction(worldModel, new CompositeAction(sensor.getActions()));
	}
	
	private void initializeEclipseConnection() throws Exception {
		System.setProperty("eclipse.directory", (String) GeneralSettingsImpl.getInstance().getSetting(
				"ECLIPSE_HOME"));
		String folder = (String) GeneralSettingsImpl.getInstance().getSetting(
				"ENV_FLUX_CODE_HOME");

		EclipseEngineOptions eclipseEngineOptions = new EclipseEngineOptions();
		File eclipseProgram;

		eclipseEngineOptions.setUseQueues(false);
		eclipse = EmbeddedEclipse.getInstance(eclipseEngineOptions);

		eclipseProgram = new File(folder + "flux.pl");
		eclipse.compile(eclipseProgram);

		eclipseProgram = new File(folder + "fluent.chr");
		eclipse.compile(eclipseProgram);

		eclipseProgram = new File(folder + "rollerslam.pl");
		eclipse.compile(eclipseProgram);

		eclipseProgram = new File(folder + "util.pl");
		eclipse.compile(eclipseProgram);

		eclipseProgram = new File(folder + "ramification.pl");
		eclipse.compile(eclipseProgram);

		eclipseProgram = new File(folder + "referee.pl");
		eclipse.compile(eclipseProgram);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		ServerFacadeImpl.getInstance().getServerInitialization().init(
				new RollerslamEnvironmentAgent());
	}
}
