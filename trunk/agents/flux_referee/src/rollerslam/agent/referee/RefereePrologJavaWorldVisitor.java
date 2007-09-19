package rollerslam.agent.referee;

import java.util.Collection;

import com.parctechnologies.eclipse.CompoundTerm;

import rollerslam.environment.model.World;

public class RefereePrologJavaWorldVisitor {
	private enum TEAM {TEAM_A, TEAM_B};
	
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
		switch(getScoredTeam(world, (CompoundTerm)term.arg(2))) {
			case TEAM_A:
				refereeWorld.getMessage().setScoreTeamA(score);
				break;
			case TEAM_B:
				refereeWorld.getMessage().setScoreTeamB(score);
				break;
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
