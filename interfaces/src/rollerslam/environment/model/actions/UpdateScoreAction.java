package rollerslam.environment.model.actions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

@SuppressWarnings("serial")
public class UpdateScoreAction extends Message {
	private int scoreTeamA;
	private int scoreTeamB;
	
	public UpdateScoreAction() { }
	
	public UpdateScoreAction(Agent sender) {
		super(sender);
	}
	
	public int getScoreTeamA() {
		return scoreTeamA;
	}
	public void setScoreTeamA(int scoreTeamA) {
		this.scoreTeamA = scoreTeamA;
	}
	public int getScoreTeamB() {
		return scoreTeamB;
	}
	public void setScoreTeamB(int scoreTeamB) {
		this.scoreTeamB = scoreTeamB;
	}



}
