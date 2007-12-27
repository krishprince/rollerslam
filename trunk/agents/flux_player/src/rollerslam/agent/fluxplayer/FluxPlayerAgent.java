package rollerslam.agent.fluxplayer;


import java.io.File;

import javax.swing.JOptionPane;

import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.SimpleAgent;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.agent.automata.RamificationComponent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedAgent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalInitializationComponent;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.eclipse.EclipsePrologHandlerImpl;
import rollerslam.infrastructure.settings.GeneralSettingsImpl;

import com.parctechnologies.eclipse.CompoundTermImpl;
import com.parctechnologies.eclipse.EclipseConnection;

@SuppressWarnings("serial")

public class FluxPlayerAgent extends GoalBasedAgent {

    public ClientFacade facade = null;
    public Agent remoteThis = null;

    public EclipseConnection eclipse;
   
    public FluxPlayerAgent(final PlayerTeam team) throws Exception {
        facade = ClientFacadeImpl.getInstance();
		remoteThis = (Agent) facade.getClientInitialization().exportObject(new SimpleAgent());
		this.setName(remoteThis.getName());

		facade.getAgentRegistry().register(remoteThis);

		this.sensor = facade.getAgentSensor(remoteThis);
		this.effector = facade.getAgentEffector(remoteThis);
		
        this.worldModel = new FluxPlayerWorldModel(null);

        initializeEclipseConnection();

        this.goalInitializationComponent = new GoalInitializationComponent() {
            public void initialize(GoalBasedEnvironmentStateModel goal) {
				((FluxPlayerWorldModel)goal).goalIsJoinGame = true;
				((FluxPlayerWorldModel)goal).myTeam = team;
            }
        };

		this.goalUpdateComponent = new GoalUpdateComponent() {

			public void updateGoal(GoalBasedEnvironmentStateModel goal) {
				
			}
			
		};

		this.initializationComponent = new ModelInitializationComponent() {
			public void initialize(EnvironmentStateModel model) {
				((FluxPlayerWorldModel)model).gameStarted = false;				
			}
			
		};
		
		this.interpretationComponent = new ActionInterpretationComponent() {

			public void processAction(EnvironmentStateModel w, Message m) {
				if (m instanceof StateMessage) {
					((FluxPlayerWorldModel)w).environmentStateModel = ((StateMessage)m).model;
					((FluxPlayerWorldModel)w).changed = true;
				} else if (m instanceof GameStartedPerception) {
					if (((GameStartedPerception)m).receiver.equals(remoteThis)) {
						((FluxPlayerWorldModel)w).myID = ((GameStartedPerception)m).playerID;
						
						try {
							eclipse.rpc(new CompoundTermImpl("init",
									((FluxPlayerWorldModel)w).myID,
									team.toString()));
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						((FluxPlayerWorldModel)w).gameStarted = true;
						((FluxPlayerWorldModel)w).goalIsJoinGame = false;
					}
					((FluxPlayerWorldModel)w).changed = true;					
					
				}				
			}
        	
        };
        
        this.ramificationComponent = new RamificationComponent() {

			public void processRamifications(EnvironmentStateModel world) {
				
			}
        	
        };

        
        this.strategyComponent = new ModelBasedBehaviorStrategyComponent() {
        	private ModelBasedBehaviorStrategyComponent strategy = new FluxPlayerModelBasedBehaviorStrategyComponent(eclipse);
        	
			public Message computeAction(EnvironmentStateModel w) {
				if (w instanceof FluxPlayerWorldModel) {
					FluxPlayerWorldModel fpwm = (FluxPlayerWorldModel) w;
					
					if (fpwm.goalIsJoinGame) {
						fpwm.joinMessageSent = true;
						return new JoinGameAction(team);								
					} else {
						Message msg = strategy.computeAction(w);
						if (msg != null) {
							msg.sender = remoteThis;					
						}
						return msg;						
					}
				}
				return null;
			}
        	
        };
        	
        	
        	

        this.run();
        startSimulation();
    }
    
    private void initializeEclipseConnection() throws Exception {
        String folder = (String)GeneralSettingsImpl.getInstance().getSetting("PLAYER_FLUX_CODE_HOME"); 
        
        eclipse = new EclipsePrologHandlerImpl().getEclipseConnection();

        File eclipseProgram = null;
        
        eclipseProgram = new File(folder + "flux.pl");
		eclipse.compile(eclipseProgram);

		eclipseProgram = new File(folder + "fluent.chr");
		eclipse.compile(eclipseProgram);
		
        eclipseProgram = new File(folder + "util.pl");
		eclipse.compile(eclipseProgram);
        
        eclipseProgram = new File(folder + "player.pl");
        eclipse.compile(eclipseProgram);
    }
    
	public static void main(String... args) throws Exception {
		
		PlayerTeam team = PlayerTeam.TEAM_A;
		
		String teamStr = "";
		String hostStr = "";
		
		if(args.length == 0) { 
			teamStr = JOptionPane.showInputDialog("Which team? [A | B]").toUpperCase();
			hostStr = JOptionPane.showInputDialog("Host?", "localhost");
		} else  {
			teamStr = args[0].toUpperCase();
			hostStr = args[1];
		}
		
		if (teamStr.equals("A"))
			team = PlayerTeam.TEAM_A;
		else
			team = PlayerTeam.TEAM_B;
		
		if (hostStr.toLowerCase().equals("auto")) {
			ClientFacadeImpl.getInstance().getClientInitialization().init();			
		} else {
			ClientFacadeImpl.getInstance().getClientInitialization().init(hostStr);
		}

		new FluxPlayerAgent(team);
	}

}
