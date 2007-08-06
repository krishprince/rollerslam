package rollerslam.environment.model;

import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Player extends AnimatedObject {
	
	private static int id_gen = 0;
	
	public PlayerTeam team;	
	public int id = id_gen ++;
	
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