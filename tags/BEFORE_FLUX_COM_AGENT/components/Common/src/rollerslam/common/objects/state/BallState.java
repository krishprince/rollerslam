package rollerslam.common.objects.state;

import rollerslam.common.DomainSettings;

@SuppressWarnings("serial")
public class BallState extends AnimatedObject {
    
	public BallState() {}
	
	public BallState(int psx, int psy) {
		super(0, 0, DomainSettings.BALL_WIDTH, DomainSettings.BALL_HEIGHT);
		maxA = DomainSettings.MAX_ACCELERATION * 7;
		maxV = DomainSettings.MAX_VELOCITY * 7;
	}

}
