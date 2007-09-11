package rollerslam.agent.coach;

import java.rmi.RemoteException;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.strategy.AgentRole;
import rollerslam.environment.model.strategy.DefineRoleFact;
import rollerslam.environment.model.strategy.PlayerPosition;
import rollerslam.environment.model.strategy.PositionCoord;
import rollerslam.environment.model.strategy.Receivers;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.SimulationState;

public class CoachAgentActionGenerator implements
		ModelBasedBehaviorStrategyComponent {

	public Message computeAction(EnvironmentStateModel w) {
		CoachAgentWorldModel model = (CoachAgentWorldModel) w;
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
		
		if (model.currentGoal == CoachAgentGoal.WAIT_JOIN_GAME) {
			//Do nothing
		}else if (model.currentGoal == CoachAgentGoal.LISTENING) {
			m = listening(model);
		}
		
		return m;
	}
	
	public Message listening(CoachAgentWorldModel model){
		if(model.lastPosition != null){
			Fact f = new Fact();
			DefineRoleFact mesg = new DefineRoleFact();
			f.cycle = ((World)model.environmentStateModel).currentCycle;
			
			if(model.myTeam == PlayerTeam.TEAM_A)
				f.sender = Receivers.COACH_A.getValue();
			else
				f.sender = Receivers.COACH_B.getValue();

			f.receiver = model.lastPlayers.toString();
			
			mesg.position = model.lastPosition;
			
			if(model.lastPosition == PlayerPosition.GOALKEEPER){
				mesg.role = AgentRole.GOALKEEPER;
			} else if(model.lastPosition.getValue() >= 1 && model.lastPosition.getValue() <= 6){
				mesg.role = AgentRole.BACKS;
			} else if(model.lastPosition.getValue() >= 7 && model.lastPosition.getValue() <= 13){
				mesg.role = AgentRole.MIDFIELDER;
			} else if(model.lastPosition.getValue() >= 14 && model.lastPosition.getValue() <= 19){
				mesg.role = AgentRole.FORWARDS;	
			}
			
			mesg.posCoord = PositionCoord.getCoord(model.lastPosition);
			
			f.message = mesg;

			model.lastPlayers = null;
			model.lastPosition = null;
			
			return new SendMsgAction(f);
		} else {
			return null;
		}
		
	}
}
