package rollerslam.agent.sentinel;

import java.rmi.RemoteException;

import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.VoiceAction;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.logging.AgentActionLogEntry;

public class SentinelAgentActionGenerator implements
		ModelBasedBehaviorStrategyComponent {

	public Agent remoteThis;

	public SentinelAgentActionGenerator(Agent remoteThis) {
		this.remoteThis = remoteThis;
	}

	public Message computeAction(EnvironmentStateModel w) {
		SentinelAgentWorldModel model = (SentinelAgentWorldModel) w;
		Message m = null;

		if ((model.currentGoal == SentinelAgentGoal.CHECK_ALIVE) || 
				(model.currentGoal == SentinelAgentGoal.KILL_PLAYER) ||
						(model.currentGoal == SentinelAgentGoal.CREATE_PLAYER))
		{
			m = new VoiceAction(remoteThis);
		} 
		try {
			ClientFacade facade = ClientFacadeImpl.getInstance();
			
			int cycle = 0;
			if ((World) model.environmentStateModel != null)
				cycle = ((World) model.environmentStateModel).currentCycle;
				
			AgentActionLogEntry envLog = new AgentActionLogEntry(cycle, model.myID, m);

			facade.getLogRecordingService().addEntry(envLog);

		} catch (RemoteException e) {
			e.printStackTrace();
			return m;
		}
		
		return m;
	}



}