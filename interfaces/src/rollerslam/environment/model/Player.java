package rollerslam.environment.model;

import rollerslam.environment.model.visitor.Visitor;

@SuppressWarnings("serial")
public class Player extends AnimatedObject {
	
	private static int id_gen = 0;
	
	public PlayerTeam team;	
	public int id = id_gen ++;
	
	public boolean hasBall = false;
        
    public boolean inGround = false;
	
	public Player() {}
	
	public Player(World w, int psx, int psy, PlayerTeam t) {
		super(w, psx, psy, SimulationSettings.PLAYER_WIDTH, SimulationSettings.PLAYER_HEIGHT);
		this.team = t;
	}	
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Player)this);
	}
}
