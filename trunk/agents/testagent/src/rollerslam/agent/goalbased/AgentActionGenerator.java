package rollerslam.agent.goalbased;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.leg.DashAction;
import rollerslam.environment.model.actions.arm.CatchAction;
import rollerslam.environment.model.actions.arm.TackleAction;
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
			
		} else if (model.currentGoal == AgentGoal.GO_TO_BALL) {
			Player me = getMeFromModel(model);
			
			return new DashAction(remoteThis, new Vector(me.world.ball.sx - me.sx, me.world.ball.sy - me.sy));
		} else if (model.currentGoal == AgentGoal.GO_TO_GOAL) {
			Player me = getMeFromModel(model);
			World world = (World)model.environmentStateModel;
									
			return new DashAction(remoteThis, new Vector(world.goalA.sx - me.sx, world.goalA.sy - me.sy));
		} else if(model.currentGoal == AgentGoal.CATCH_BALL) {
			return new CatchAction(remoteThis);
		} else if(model.currentGoal == AgentGoal.TACKLE_PLAYER){
                        return new TackleAction(remoteThis);
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
