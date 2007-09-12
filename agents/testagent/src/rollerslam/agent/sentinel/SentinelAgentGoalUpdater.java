package rollerslam.agent.sentinel;

import java.rmi.RemoteException;

import rollerslam.environment.model.Player;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.logging.GoalUpdateLogEntry;

public class SentinelAgentGoalUpdater implements GoalUpdateComponent {
	@SuppressWarnings("unused")
	private Agent remoteThis;
    
    public SentinelAgentGoalUpdater(Agent remoteThis){
        this.remoteThis = remoteThis;
    }
    
    public void updateGoal(GoalBasedEnvironmentStateModel goal) {
		SentinelAgentWorldModel model = (SentinelAgentWorldModel) goal;
		
		ClientFacade facade = ClientFacadeImpl.getInstance();

		if (model.currentGoal == SentinelAgentGoal.CHECK_ALIVE) {
			if (model.gameStarted) {
            	Player me = model.getMe();
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "The player " + me.id + " is out");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			if (PrintTrace.TracePrint){
    					e.printStackTrace();
    				}
        		}
            	model.currentGoal = SentinelAgentGoal.CREATE_PLAYER;
            }
			
		} else if (model.currentGoal == SentinelAgentGoal.KILL_PLAYER) {
            if (model.gameStarted) {
            	Player me = model.getMe();
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "The player " + me.id + " is out");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			if (PrintTrace.TracePrint){
    					e.printStackTrace();
    				}
        		}
            	model.currentGoal = SentinelAgentGoal.CREATE_PLAYER;
            }			
		} else if (model.currentGoal == SentinelAgentGoal.CREATE_PLAYER) { 
			Player me = model.getMe();
			try {
    			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "New Player in the game:" + me.id);
    			facade.getLogRecordingService().addEntry(envLog);
    		} catch (RemoteException e) {
    			if (PrintTrace.TracePrint){
					e.printStackTrace();
				}
    		}
        	model.currentGoal = SentinelAgentGoal.CHECK_ALIVE;
        }
             
        
	}

}
