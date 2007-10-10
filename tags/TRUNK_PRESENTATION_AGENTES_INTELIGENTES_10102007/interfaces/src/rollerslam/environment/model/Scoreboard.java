package rollerslam.environment.model;

import java.io.Serializable;

import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Scoreboard extends WorldObject implements Serializable {
	public int scoreTeamA = 0;
	public int scoreTeamB = 0;
	
	public Scoreboard(World w) {
		super(w, 0, 0, 0, 0);
	}

	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Scoreboard)this);
	}
}
