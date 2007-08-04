package rollerslam.agent.goalbased;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.DashAction;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.utils.Vector;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;

public class AgentActionGenerator implements
		ModelBasedBehaviorStrategyComponent {

	public Agent remoteThis;
	
	public AgentActionGenerator(Agent remoteThis) {
		this.remoteThis = remoteThis;
	}
	
	public Message computeAction(EnvironmentStateModel w) {
		AgentWorldModel model = (AgentWorldModel) w;
		if (model.currentGoal == AgentGoal.JOIN_GAME) {
			model.joinMessageSent = true;
			return new JoinGameAction(remoteThis, PlayerTeam.TEAM_A);			
		} else if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {
			
		} else if (model.currentGoal == AgentGoal.GO_TO_CENTER) {
			Player me = getMeFromModel(model);
			
			return new DashAction(remoteThis, new Vector(0 - me.sx, 0 - me.sy));
		} else if (model.currentGoal == AgentGoal.GO_TO_GOAL) {
			Player me = getMeFromModel(model);
			World world = (World)model.environmentStateModel;
									
			return new DashAction(remoteThis, new Vector(world.goalA.sx - me.sx, world.goalA.sy - me.sy));
		}
		return null;
	}

	private Player getMeFromModel(AgentWorldModel model) {
		for (Player player : ((World)model.environmentStateModel).playersA) {
			if (player.id == model.myID) {
				return player;
			}
		}

		for (Player player : ((World)model.environmentStateModel).playersB) {
			if (player.id == model.myID) {
				return player;
			}
		}

		return null;
	}

}
