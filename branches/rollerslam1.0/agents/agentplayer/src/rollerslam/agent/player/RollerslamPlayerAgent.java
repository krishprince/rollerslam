package rollerslam.agent.player;

import javax.swing.JOptionPane;

import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.SimpleAgent;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.agent.automata.RamificationComponent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedAgent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalInitializationComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;

public class RollerslamPlayerAgent extends GoalBasedAgent {
	public ClientFacade          facade      = null;	
	public Agent                 remoteThis  = null;
	
	public RollerslamPlayerAgent(final PlayerTeam team) throws Exception {
		facade = ClientFacadeImpl.getInstance();
		//remoteThis = (Agent) facade.getClientInitialization().exportObject(this);
		remoteThis = (Agent) facade.getClientInitialization().exportObject(new SimpleAgent());
		this.setName(remoteThis.getName());

		facade.getAgentRegistry().register(remoteThis);

		this.sensor = facade.getAgentSensor(remoteThis);
		this.effector = facade.getAgentEffector(remoteThis);

		this.worldModel = new AgentWorldModel(null);
		
		this.goalInitializationComponent = new GoalInitializationComponent() {
			public void initialize(GoalBasedEnvironmentStateModel goal) {
				((AgentWorldModel)goal).currentGoal = AgentGoal.JOIN_GAME;
				((AgentWorldModel)goal).myTeam = team;
			}			
		};
		
		//this.goalUpdateComponent = new AgentGoalUpdater(remoteThis);
		this.goalUpdateComponent = new AgentGoalUpdater();
		
		this.initializationComponent = new ModelInitializationComponent() {
			public void initialize(EnvironmentStateModel model) {
				((AgentWorldModel)model).gameStarted = false;				
			}
			
		};
		
		this.interpretationComponent = new ActionInterpretationComponent() {
			public void processAction(EnvironmentStateModel w, Message m) {
				if (m instanceof StateMessage) {
					((AgentWorldModel)w).environmentStateModel = ((StateMessage)m).model;
					((AgentWorldModel)w).changed = true;
				} else if (m instanceof GameStartedPerception) {
					if (((GameStartedPerception)m).receiver.equals(remoteThis)) {
						((AgentWorldModel)w).myID = ((GameStartedPerception)m).playerID;
					}
					((AgentWorldModel)w).changed = true;
				}
			}			
		};
		
		this.ramificationComponent = new RamificationComponent() {
			public void processRamifications(EnvironmentStateModel world) {
				
			}

		};
		
		this.strategyComponent = new AgentActionGenerator();
		
		this.run();
		startSimulation();
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

		new RollerslamPlayerAgent(team);
	}

}
