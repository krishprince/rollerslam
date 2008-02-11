package rollerslam.agent.referee.facts;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.PlayerTeam;

@SuppressWarnings("serial")
public class GoalFact extends Fact {

	public GoalFact(int cycle, PlayerTeam team) {
		sender = "refereee";
		receiver = "all";
		message = "GOOAAALLL!!!! " + team + " ON CYCLE " + cycle;
	}
}
