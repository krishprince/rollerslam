package rollerslam.agent.referee;

import java.rmi.RemoteException;
import java.util.Collection;

import com.parctechnologies.eclipse.CompoundTerm;

import rollerslam.agent.referee.facts.GoalFact;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.logging.LogRecordingService;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.logging.AgentActionLogEntry;

public class RefereePrologJavaWorldVisitor {
	private enum TEAM {TEAM_A, TEAM_B};
	private LogRecordingService logger;
	
	@SuppressWarnings("unchecked")
	public void updateWorldRepresentation(RefereeWorldModel refereeWorld, Object worldState) {
		if (worldState instanceof Collection) {
			Collection stateCol = (Collection) worldState;
			
			for (Object object : stateCol) {
				if (object instanceof CompoundTerm) {
					CompoundTerm term = (CompoundTerm) object;
					if(term.functor().equals("score")) {
						updateScore(refereeWorld, term);
					}
				}
			}
		}
	}
	
	private void updateScore(RefereeWorldModel refereeWorld, CompoundTerm term) {
		World world = (World)refereeWorld.getEnvironmentStateModel();
		int score = Integer.parseInt(term.arg(1).toString());
		
		PlayerTeam goalTeam = null;
		
		switch(getScoredTeam(world, (CompoundTerm)term.arg(2))) {
			case TEAM_A:
				if (refereeWorld.getMessage().getScoreTeamA() != score) {
					goalTeam = PlayerTeam.TEAM_A;
				}
				refereeWorld.getMessage().setScoreTeamA(score);
				break;
			case TEAM_B:
				if (refereeWorld.getMessage().getScoreTeamB() != score) {
					goalTeam = PlayerTeam.TEAM_B;
				}
				refereeWorld.getMessage().setScoreTeamB(score);
				break;
		}

		if (goalTeam != null) {
			refereeWorld.needsToSayGoal = true;
			refereeWorld.goalFact = new GoalFact(((World)refereeWorld.getEnvironmentStateModel()).currentCycle, goalTeam);
			newLogentry(refereeWorld.goalFact);
		}		
	}
	
	 private void newLogentry(GoalFact goal) {
			try {			
				//TODO pegar id do juiz
				AgentActionLogEntry envLog = new AgentActionLogEntry(goal.cycle, -1, (Message)goal.message);

				ClientFacadeImpl.getInstance().getLogRecordingService().addEntry(envLog);
			

			} catch (RemoteException e) {
				if (PrintTrace.TracePrint){
					e.printStackTrace();
				}
			}
		}
	
	private TEAM getScoredTeam(World world, CompoundTerm term) {
		int teamId = Integer.parseInt(term.arg(1).toString());
		if(world.goalA.hashCode() == teamId)
			return TEAM.TEAM_A;
		else
			return TEAM.TEAM_B;
	}

}
