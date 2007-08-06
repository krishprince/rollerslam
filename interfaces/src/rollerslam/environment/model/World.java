package rollerslam.environment.model;

import java.io.Serializable;
import java.util.Random;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;

@SuppressWarnings("serial")
public class World extends EnvironmentStateModel implements Serializable, Visitable  {
	public OutTrack outTrack     = new OutTrack(this);
	public Ball ball      		 = new Ball(this, 0, 0);
	public Player playersA[] 	 = new Player[2];
	public Player playersB[] 	 = new Player[2];
	public Goal goalA;
	public Goal goalB;
	public Ramp rampA;
	public Ramp rampB;
	public Trampoline trampolineA;
	public Trampoline trampolineB;

	public static final int WIDTH  = 188000;
	public static final int HEIGHT = 138000;
	
	private Random random 		 = new Random();
	
	public World() {
		int wid = outTrack.width/2  - 2*Player.WIDTH; 
		int hei = outTrack.height/2 - 2*Player.HEIGHT; 
	
		for(int i=0;i<playersA.length;++i) {			
			playersA[i] = new Player(this, -(random.nextInt(wid) + Player.WIDTH), 
								     -(random.nextInt(hei) + Player.HEIGHT),
								     PlayerTeam.TEAM_A);
		}

		for(int i=0;i<playersB.length;++i) {			
			playersB[i] = new Player(this, (random.nextInt(wid) + Player.WIDTH), 
								     (random.nextInt(hei) + Player.HEIGHT),
								      PlayerTeam.TEAM_B);
		}
		
		goalA = new Goal(this, -64350, 0);
		goalB = new Goal(this, 64350, 0);
		
		rampA = new Ramp(this, -64350, 0);
		rampB = new Ramp(this, 64350, 0);
		
		trampolineA = new Trampoline(this, -64350, 0);
		trampolineB = new Trampoline(this, 64350, 0);
		
	}
	
	public boolean calculePointIntoEllipse(int px, int py){
		int dist1 = calculeDistancePoints(-64350, px, 0, py);
		int dist2 = calculeDistancePoints(64350, px, 0, py);
		
		if(dist1 + dist2 > 188700)
			return false;
		else
			return true;
		
	}
	
	public int calculeDistancePoints(int px1, int px2, int py1, int py2){
		int dx = px1 - px2;
		int dy = py1 - py2;
		return (int)Math.abs(Math.sqrt(dx*dx + dy*dy));
	}

	public void accept(Visitor visitor) {
		visitor.visit((World)this);

		outTrack.accept(visitor);
		ball.accept(visitor);
		for(int i=0;i<playersA.length;++i) {
			playersA[i].accept(visitor);
		}

		for(int i=0;i<playersB.length;++i) {			
			playersB[i].accept(visitor);
		}
	}
}
