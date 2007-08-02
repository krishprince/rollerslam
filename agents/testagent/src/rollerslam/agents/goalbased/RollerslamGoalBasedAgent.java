package rollerslam.agents.goalbased;

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
	
	public RollerslamGoalBasedAgent() throws Exception {
		facade = ClientFacadeImpl.getInstance();
		remoteThis = (Agent) facade.getClientInitialization().exportObject(this);
		facade.getAgentRegistry().register(remoteThis);

		this.sensor = facade.getAgentSensor(remoteThis);
		this.effector = facade.getAgentEffector(remoteThis);
		
		this.worldModel = new AgentWorldModel(null);
		
		this.goalInitializationComponent = new GoalInitializationComponent() {
			public void initialize(GoalBasedEnvironmentStateModel goal) {
				((AgentWorldModel)goal).currentGoal = AgentGoal.JOIN_GAME;
			}			
		};
		
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
				}
			}			
		};
		
		this.ramificationComponent = new AgentRamificator();
		
		this.strategyComponent = new AgentActionGenerator();
	}

	public static void main(String[] args) throws Exception {
		ClientFacadeImpl.getInstance().getClientInitialization().init("localhost");		
	}
}
