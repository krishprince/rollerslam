package rollerslam.environment.model;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

public class World implements Updateable, Serializable {
	public OutTrack outTrack     = new OutTrack();
	public Ball ball      		 = new Ball(0, 0);
	public Player playersA[] 	 = new Player[2];
	public Player playersB[] 	 = new Player[2];

	public Vector<Updateable> allUpdateable = new Vector<Updateable>();
	
	private Random random 		 = new Random();
	
	public World() {
		int wid = outTrack.width  - 2*Player.WIDTH; 
		int hei = outTrack.height - 2*Player.HEIGHT; 
	
		allUpdateable.add(ball);
		
		for(int i=0;i<playersA.length;++i) {			
			playersA[i] = new Player(-(random.nextInt(wid) + Player.WIDTH), 
								     -(random.nextInt(hei) + Player.HEIGHT),
								     PlayerTeam.TEAM_A);
			allUpdateable.add(playersA[i]);
		}

		for(int i=0;i<playersB.length;++i) {			
			playersB[i] = new Player((random.nextInt(wid) + Player.WIDTH), 
								     (random.nextInt(hei) + Player.HEIGHT),
								      PlayerTeam.TEAM_B);
			allUpdateable.add(playersB[i]);
		}
	}

	public void update() {
		for (Updateable u : allUpdateable) {
			u.update();
		}
	}
}
