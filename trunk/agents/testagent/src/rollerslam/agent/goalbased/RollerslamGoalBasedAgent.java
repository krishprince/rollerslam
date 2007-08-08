package rollerslam.agent.goalbased;

import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedAgent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalInitializationComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;

@SuppressWarnings("serial")
public class RollerslamGoalBasedAgent extends GoalBasedAgent {
	public ClientFacade          facade      = null;	
	public Agent                 remoteThis  = null;
	
	public RollerslamGoalBasedAgent(final PlayerTeam team) throws Exception {
		facade = ClientFacadeImpl.getInstance();
		remoteThis = (Agent) facade.getClientInitialization().exportObject(this);
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
		
		this.goalUpdateComponent = new AgentGoalUpdater(remoteThis);
		
		this.initializationComponent = new ModelInitializationComponent() {
			public void initialize(EnvironmentStateModel model) {
				((AgentWorldModel)model).gameStarted = false;				
			}
			
		};
		
		this.interpretationComponent = new ActionInterpretationComponent() {
			public void processAction(EnvironmentStateModel w, Message m) {			
				if (m instanceof StateMessage) {
					((AgentWorldModel)w).environmentStateModel = ((StateMessage)m).model;
				} else if (m instanceof GameStartedPerception) {
					((AgentWorldModel)w).gameStarted = true;
					((AgentWorldModel)w).myID = ((GameStartedPerception)m).playerID;
				}
			}			
		};
		
		this.ramificationComponent = new AgentRamificator();
		
		this.strategyComponent = new AgentActionGenerator(remoteThis);
		
		this.run();
		startSimulation();
	}

	public static void main(String... args) throws Exception {
		ClientFacadeImpl.getInstance().getClientInitialization().init("localhost");
		
		PlayerTeam team = PlayerTeam.TEAM_A;
		
		if(args.length > 0){
			if(args[0].equals("A"))
				team = PlayerTeam.TEAM_A;
			else if(args[0].equals("B"))
				team = PlayerTeam.TEAM_B;
		}
		
		new RollerslamGoalBasedAgent(team);
	}
}
