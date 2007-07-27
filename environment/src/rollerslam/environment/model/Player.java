package rollerslam.environment.model;

public class Player extends AnimatedObject {
	PlayerTeam team;
	
	public static final int WIDTH  = 1000;
	public static final int HEIGHT = 1000;
		
	public Player(int psx, int psy, PlayerTeam t) {
		super(psx, psy, WIDTH, HEIGHT);
		this.team = t;
	}	
}
