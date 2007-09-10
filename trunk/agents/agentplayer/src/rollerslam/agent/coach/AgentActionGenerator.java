package rollerslam.agent.coach;

import java.rmi.RemoteException;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.strategy.AgentRole;
import rollerslam.environment.model.strategy.DefineRoleFact;
import rollerslam.environment.model.strategy.Receivers;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.SimulationState;

public class AgentActionGenerator implements
		ModelBasedBehaviorStrategyComponent {

	public Agent remoteThis;

	public AgentActionGenerator(Agent remoteThis) {
		this.remoteThis = remoteThis;
	}

	public Message computeAction(EnvironmentStateModel w) {
		AgentWorldModel model = (AgentWorldModel) w;
		Message m = null;
		
		ClientFacade facade = ClientFacadeImpl.getInstance();
		SimulationState state = SimulationState.STOPPED;
		
		try {
			state = facade.getSimulationAdmin().getState();
		} catch (RemoteException e) {
			if (PrintTrace.TracePrint){
				e.printStackTrace();
			}
		}	
		
		if(state == SimulationState.STOPPED){
			return null;			
		}
		
		if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {
			//Do nothing
		}else if (model.currentGoal == AgentGoal.LISTENING) {
			m = listening(model);
		}
		
		return m;
	}
	
	public Message listening(AgentWorldModel model){
		if(model.lastPosition != -1){
			Fact f = new Fact();
			DefineRoleFact mesg = new DefineRoleFact();
			f.cycle = ((World)model.environmentStateModel).currentCycle;
			
			if(model.myTeam == PlayerTeam.TEAM_A)
				f.sender = Receivers.COACH_A.getValue();
			else
				f.sender = Receivers.COACH_B.getValue();

System.out.println("1# listening ag " + model.lastPlayers.toString());

			f.receiver = model.lastPlayers.toString();
			
			mesg.position = model.lastPosition;
			
			if(model.lastPosition == 0){
				mesg.role = AgentRole.GOALKEEPER;
			} else if(model.lastPosition >= 1 && model.lastPosition <= 6){
				mesg.role = AgentRole.BACKS;
			} else if(model.lastPosition >= 7 && model.lastPosition <= 13){
				mesg.role = AgentRole.MIDFIELDER;
			} else if(model.lastPosition >= 14 && model.lastPosition <= 19){
				mesg.role = AgentRole.FORWARDS;	
			}
			
			f.message = mesg;
			
			model.lastPlayers = -1;
			model.lastPosition = -1;
			
			return new SendMsgAction(f);
		} else {
			return null;
		}
		
	}
}
