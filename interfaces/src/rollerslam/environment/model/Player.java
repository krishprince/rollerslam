package rollerslam.environment.model;

import java.util.Random;
import rollerslam.environment.model.visitor.Visitor;
import rollerslam.environment.model.utils.Constants;

@SuppressWarnings("serial")
public class Player extends AnimatedObject {
	
	private static int id_gen = 0;
	
	public PlayerTeam team;	
	public int id = id_gen ++;
	
	public boolean hasBall = false;
        
    public boolean inGround = false;
	
	public double strength = 0;

	public Player() {}
	
	public Player(World w, int psx, int psy, PlayerTeam t) {
		super(w, psx, psy, SimulationSettings.PLAYER_WIDTH, SimulationSettings.PLAYER_HEIGHT);
		this.team = t;
		
		Random rand = new Random();
		
		strength = rand.nextDouble();
		
		if(strength < 0.3){
			strength = 1 - strength; 
		}
		
		maxV = (int)Math.floor(Constants.MAX_VELOCITY * (1 + strength));
		maxA = (int)Math.floor(Constants.MAX_ACCELERATION * (1 + strength));
		
	}	
	
	@Override
	public void accept(Visitor visitor) {
		visitor.visit((Player)this);
	}
}
