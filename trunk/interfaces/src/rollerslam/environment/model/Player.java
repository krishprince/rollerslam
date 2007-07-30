package rollerslam.environment.model;

import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Player extends AnimatedObject {
	public PlayerTeam team;
	
	public static final int WIDTH  = 1000;
	public static final int HEIGHT = 1000;
		
	public Player() {}
	
	public Player(int psx, int psy, PlayerTeam t) {
		super(psx, psy, WIDTH, HEIGHT);
		this.team = t;
	}	
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Player)this);
	}
}
