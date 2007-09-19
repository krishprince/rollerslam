package rollerslam.agent.referee;

import java.io.File;

import com.parctechnologies.eclipse.EclipseConnection;
import com.parctechnologies.eclipse.EclipseEngineOptions;
import com.parctechnologies.eclipse.EmbeddedEclipse;

import rollerslam.environment.model.actions.UpdateScoreAction;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.client.ClientInitialization;
import rollerslam.infrastructure.server.PrintTrace;

public class Referee extends AutomataAgent {
	public ClientFacade facade = null;
    public Agent remoteThis = null;
    
    public EclipseConnection eclipse;

    public Referee() throws Exception {
    	initializeEclipseConnection();
    	
        facade = ClientFacadeImpl.getInstance();
        ClientInitialization clientInitialization = facade.getClientInitialization();
        clientInitialization.init();
        remoteThis = (Agent) clientInitialization.exportObject(this);
        
        facade.getAgentRegistry().register(remoteThis);

        this.sensor = facade.getAgentSensor(remoteThis);
        this.effector = facade.getAgentEffector(remoteThis);

        this.worldModel = new RefereeWorldModel(null);
        ((RefereeWorldModel)worldModel).setMessage(new UpdateScoreAction(remoteThis));

       this.initializationComponent = new ModelInitializationComponent() {

            public void initialize(EnvironmentStateModel model) {
                /*((RefereeWorldModel) model).gameStarted = false;*/
            }
        };

        this.interpretationComponent = new ActionInterpretationComponent() {

            public void processAction(EnvironmentStateModel w, Message m) {
                if (m instanceof StateMessage) {
                    ((RefereeWorldModel) w).setEnvironmentStateModel( ((StateMessage) m).model );
                }
            }
        };

        this.ramificationComponent = new RefereeRamificator(eclipse);

        this.strategyComponent = new ModelBasedBehaviorStrategyComponent() {
			public Message computeAction(EnvironmentStateModel w) {
				try {
					if(w instanceof RefereeWorldModel) {
						RefereeWorldModel refereeWorld = (RefereeWorldModel)w;
						return refereeWorld.getMessage();						
					}
					return null;
				} catch (Exception e) {
					if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
					
				}
				return null;
			}
		};

        this.run();
        startSimulation();
    }
    
    private void initializeEclipseConnection() throws Exception {
	    System.setProperty("eclipse.directory", "C:/Tools/ECLiPSe_5.10");
	    String folder = "C:/Projetos/rollerslam/referee/flux/";

	    EclipseEngineOptions eclipseEngineOptions = new EclipseEngineOptions();
	    File eclipseProgram;
	    
	    eclipseEngineOptions.setUseQueues(false);
	    eclipse = EmbeddedEclipse.getInstance(eclipseEngineOptions);	    
	    
	    eclipseProgram = new File(folder + "flux.pl");
	    eclipse.compile(eclipseProgram);
	    
	    eclipseProgram = new File(folder + "fluent.chr");
	    eclipse.compile(eclipseProgram);
	    
	    eclipseProgram = new File(folder + "referee.pl");
	    eclipse.compile(eclipseProgram);
	    
	}

    public static void main(String[] args) throws Exception {
    	new Referee();
    	
        /*PlayerTeam team = PlayerTeam.TEAM_A;

        String teamStr = "";
        String hostStr = "";

        if (args.length == 0) {
            JComboBox jcb = new JComboBox(new String[]{"A", "B"});
            JOptionPane.showMessageDialog(null, jcb, "Which team? [A | B]", JOptionPane.QUESTION_MESSAGE);
            teamStr = (String)jcb.getSelectedItem();
            hostStr = JOptionPane.showInputDialog("Host?", "auto");
        } else {
            teamStr = args[0].toUpperCase();
            hostStr = args[1];
        }

        if (teamStr.equals("A")) {
            team = PlayerTeam.TEAM_A;
        } else {
            team = PlayerTeam.TEAM_B;
        }

        if (hostStr.toLowerCase().equals("auto")) {
            ClientFacadeImpl.getInstance().getClientInitialization().init();
        } else {
            ClientFacadeImpl.getInstance().getClientInitialization().init(hostStr);
        }

        new RollerslamGoalBasedAgent(team);*/
    }

}
