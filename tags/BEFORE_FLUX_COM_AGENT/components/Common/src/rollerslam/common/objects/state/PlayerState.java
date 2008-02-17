package rollerslam.common.objects.state;

import java.util.Random;

import rollerslam.common.DomainSettings;

@SuppressWarnings("serial")
public class PlayerState extends AnimatedObject {
		        
    public boolean inGround = false;	
	public double  strength = 0;	
	public boolean counterTackle = false;	
	public boolean dead = false;
	public boolean withBall = false;

	public PlayerState() {}
	
	public PlayerState(int psx, int psy) {
		super(psx, psy, DomainSettings.PLAYER_WIDTH, DomainSettings.PLAYER_HEIGHT);
		
		Random rand = new Random();
		
		strength = rand.nextDouble();
		
		if(strength < 0.3){
			strength = 1 - strength; 
		}
		
		maxV = (int)Math.floor(DomainSettings.MAX_VELOCITY * (1 + strength));
		maxA = (int)Math.floor(DomainSettings.MAX_ACCELERATION * (1 + strength));		
	}	

}
