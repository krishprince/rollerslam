package rollerslam.agent.coach;

import java.rmi.RemoteException;
import java.util.Vector;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.strategy.InitializationFact;
import rollerslam.environment.model.strategy.Receivers;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.SimulationState;
import rollerslam.logging.GoalUpdateLogEntry;

public class CoachAgentGoalUpdater implements GoalUpdateComponent {
	String logMsg = "";
	int cycle = 0;
	int id = -5;

	public void updateGoal(GoalBasedEnvironmentStateModel goal) {
		CoachAgentWorldModel model = (CoachAgentWorldModel) goal;
		
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
		
		logMsg = "";
		
		if(state == SimulationState.STOPPED){
			//Stop reasoning			
		} else if (model.currentGoal == CoachAgentGoal.WAIT_JOIN_GAME) {
			waitJoinGame(model);
		} else if (model.currentGoal == CoachAgentGoal.LISTENING) {
			listening(model);
		} 
		
		try {
			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(cycle, id, logMsg);
			facade.getLogRecordingService().addEntry(envLog);
		} catch (RemoteException e) {
			if (PrintTrace.TracePrint){
				e.printStackTrace();
			}
		}

	}
	
	private void waitJoinGame(CoachAgentWorldModel model){
        if (model.gameStarted) {
        	model.currentGoal = CoachAgentGoal.LISTENING;
        } else {
        	if (model.environmentStateModel != null) {
        		model.gameStarted = true;
        	}
        }
	}
	
	private void listening(CoachAgentWorldModel model){
		cycle = ((World)model.environmentStateModel).currentCycle;
		
		if(model.myTeam == PlayerTeam.TEAM_A){
			id = -2;
		} else {
			id = -3;
		}
			
		Vector<Message> acts = ((World)model.environmentStateModel).actions;
		String me = null;
		
		if(model.myTeam == PlayerTeam.TEAM_A)
			me = Receivers.COACH_A.getValue();
		else
			me = Receivers.COACH_B.getValue();

		for(Message a : acts){
			if(a instanceof SendMsgAction){
				Fact f = ((SendMsgAction)a).subject;
				if(f.receiver.equals(me)){
					if(f.message instanceof InitializationFact){
						if(!model.playersToSetPosition.contains(Integer.parseInt(f.sender))){
							model.playersToSetPosition.add(Integer.parseInt(f.sender));
							
							logMsg += "\nRECEIVE INITIALIZE MESSAGE - AGENT " + Integer.parseInt(f.sender);
						}
					}
				}
			}
		}

		if(model.playersToSetPosition.size() > 0){
			if(model.playersPosition.containsKey(model.playersToSetPosition.get(0))){
				model.playersPosition.put(model.playersToSetPosition.get(0), model.playersPosition.get(model.playersToSetPosition.get(0)));
				model.lastPlayers = model.playersToSetPosition.get(0);
				model.lastPosition = model.playersPosition.get(model.playersToSetPosition.get(0));
				model.playersToSetPosition.remove(0); 
				
				logMsg += "\nSETTING AGENT POSITION - AGENT " + model.lastPlayers + "; POSITION " + model.lastPosition;
			}else {
				model.playersPosition.put(model.playersToSetPosition.get(0), model.positionToSet.get(0));
				model.lastPlayers = model.playersToSetPosition.get(0);
				model.lastPosition = model.positionToSet.get(0);
				model.playersToSetPosition.remove(0); 
				model.positionToSet.remove(0);
				
				logMsg += "\nSETTING AGENT POSITION - AGENT " + model.lastPlayers + "; POSITION " + model.lastPosition;
			}
			
			logMsg += "\nSENDING POSITION TO AGENT " + model.lastPlayers;
		}		
	}
	

}
