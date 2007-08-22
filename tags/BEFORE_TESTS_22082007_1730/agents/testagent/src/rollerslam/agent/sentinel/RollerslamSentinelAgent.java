package rollerslam.agent.sentinel;

import java.util.Set;

import javax.swing.JOptionPane;

import rollerslam.agent.dummy.RollerslamDummyAgent;
import rollerslam.agent.goalbased.AgentActionGenerator;
import rollerslam.agent.goalbased.AgentGoal;
import rollerslam.agent.goalbased.AgentGoalUpdater;
import rollerslam.agent.goalbased.AgentRamificator;
import rollerslam.agent.goalbased.AgentWorldModel;
import rollerslam.agent.goalbased.RollerslamGoalBasedAgent;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.actions.leg.DashAction;
import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.environment.model.utils.Vector;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalInitializationComponent;
import rollerslam.infrastructure.agent.sentinel.SentinelAgent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;

@SuppressWarnings("serial")
public class RollerslamSentinelAgent extends SentinelAgent {
	public ClientFacade facade = null;

	public Agent remoteThis = null;

	public boolean gameStarted = false;

	private int id = -1;

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public RollerslamSentinelAgent(final PlayerTeam teamA,
			final PlayerTeam teamB) throws Exception {
		facade = ClientFacadeImpl.getInstance();
		remoteThis = (Agent) facade.getClientInitialization()
				.exportObject(this);
		facade.getAgentRegistry().register(remoteThis);

		this.sensor = facade.getAgentSensor(remoteThis);
		this.effector = facade.getAgentEffector(remoteThis);

		this.worldModel = new SentinelAgentWorldModel(null);

		this.goalInitializationComponent = new GoalInitializationComponent() {
			public void initialize(GoalBasedEnvironmentStateModel goal) {
				((SentinelAgentWorldModel) goal).currentGoal = SentinelAgentGoal.CHECK_ALIVE;
				((SentinelAgentWorldModel) goal).myTeamA = teamA;
				((SentinelAgentWorldModel) goal).myTeamB = teamB;
			}
		};

		this.initializationComponent = new ModelInitializationComponent() {
			public void initialize(EnvironmentStateModel model) {
				((SentinelAgentWorldModel) model).gameStarted = false;
			}

		};

		this.interpretationComponent = new ActionInterpretationComponent() {
			public void processAction(EnvironmentStateModel w, Message m) {
				if (m instanceof StateMessage) {
					((SentinelAgentWorldModel) w).environmentStateModel = ((StateMessage) m).model;
				} else if (m instanceof GameStartedPerception) {
					if (((GameStartedPerception) m).receiver.equals(remoteThis)) {
						((AgentWorldModel) w).gameStarted = true;
						((AgentWorldModel) w).myID = ((GameStartedPerception) m).playerID;
					}
				}
			}
		};

		// this.ramificationComponent = new AgentRamificator();

		// this.strategyComponent = new AgentActionGenerator(remoteThis);

		this.run();
	}

	public static void main(String[] args) throws Exception {
		ClientFacadeImpl.getInstance().getClientInitialization().init(
				"localhost");

		PlayerTeam teamA = PlayerTeam.TEAM_A;

		PlayerTeam teamB = PlayerTeam.TEAM_B;

		new RollerslamSentinelAgent(teamA, teamB);
	}
}
