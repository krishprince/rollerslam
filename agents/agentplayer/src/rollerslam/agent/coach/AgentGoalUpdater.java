package rollerslam.agent.coach;

import java.rmi.RemoteException;
import java.util.Vector;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.strategy.AgentRole;
import rollerslam.environment.model.strategy.DefineRoleFact;
import rollerslam.environment.model.strategy.InitializationFact;
import rollerslam.environment.model.strategy.Receivers;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.SimulationState;

public class AgentGoalUpdater implements GoalUpdateComponent {

	public void updateGoal(GoalBasedEnvironmentStateModel goal) {
		AgentWorldModel model = (AgentWorldModel) goal;
		
		ClientFacade facade = ClientFacadeImpl.getInstance();
		SimulationState state = SimulationState.STOPPED;
		
		try {
			state = facade.getSimulationAdmin().getState();
		} catch (RemoteException e) {
			if (PrintTrace.TracePrint){
				if (PrintTrace.TracePrint){
					e.printStackTrace();
				}
			}
		}	
		
		if(state == SimulationState.STOPPED){
			//Stop reasoning			
		} else if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {
			waitJoinGame(model);
		} else if (model.currentGoal == AgentGoal.LISTENING) {
			listening(model);
		} 
	}
	
	private void waitJoinGame(AgentWorldModel model){
        if (model.gameStarted) {
        	model.currentGoal = AgentGoal.LISTENING;
        } else {
        	if (model.environmentStateModel != null) {
        		model.gameStarted = true;
        	}
        }
	}
	
	private void listening(AgentWorldModel model){
		Vector<Message> acts = ((World)model.environmentStateModel).actions;
		String me = null;
		
		if(model.myTeam == PlayerTeam.TEAM_A)
			me = Receivers.COACH_A.getValue();
		else
			me = Receivers.COACH_B.getValue();
		
System.out.println("0# listening gu " + acts.size());

		for(Message a : acts){
System.out.println("1# listening gu " + (a instanceof SendMsgAction));			
			if(a instanceof SendMsgAction){
				Fact f = ((SendMsgAction)a).subject;
System.out.println("2# listening gu " + f.receiver);			
				if(f.receiver.equals(me)){
					if(f.message instanceof InitializationFact){
						model.playersToSetPosition.add(Integer.parseInt(f.sender));
System.out.println("3# listening gu " + f.sender);		

					}
				}
			}
		}
		
		if(model.playersToSetPosition.size() > 0){
			model.playersPosition.put(model.playersToSetPosition.get(0), model.positionToSet.get(0));
			model.lastPlayers = model.playersToSetPosition.get(0);
			model.lastPosition = model.positionToSet.get(0);
			model.playersToSetPosition.remove(0); 
			model.positionToSet.remove(0);
		}		
	}
	

}
