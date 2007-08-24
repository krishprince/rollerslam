package rollerslam.agent.sentinel;


import rollerslam.environment.model.actions.sentinel.CheckAliveAction;
import rollerslam.environment.model.actions.sentinel.KillPlayerAction;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;

public class SentinelAgentActionGenerator implements
		ModelBasedBehaviorStrategyComponent {

	public Agent remoteThis;

	public SentinelAgentActionGenerator(Agent remoteThis) {
		this.remoteThis = remoteThis;
	}

	public Message computeAction(EnvironmentStateModel w) {
		SentinelAgentWorldModel model = (SentinelAgentWorldModel) w;
		Message m = null;

		if (model.currentGoal == SentinelAgentGoal.CHECK_ALIVE){
			m = new CheckAliveAction(remoteThis);
		}else if (model.currentGoal == SentinelAgentGoal.KILL_PLAYER){
			m = new KillPlayerAction(remoteThis);
		}else if (model.currentGoal == SentinelAgentGoal.CREATE_PLAYER){
			//TODO:: Replication
		}	
		return m;
	}



}
