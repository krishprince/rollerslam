package rollerslam.environment.model;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;
import rollerslam.environment.model.utils.MathGeometry;

import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;

@SuppressWarnings("serial")
public class World extends EnvironmentStateModel implements Serializable, Visitable  {

	public int currentCycle 	     = 0;
	public Vector<Fact> saidMessages = new Vector<Fact>();
	
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
        
    public Player playerWithBall = null;

	public static final int WIDTH  = 188000;
	public static final int HEIGHT = 138000;
	
	public static final int FOCUS1X = -63835;
	public static final int FOCUS1Y = 0;
	
	public static final int FOCUS2X = 63835;
	public static final int FOCUS2Y = 0;
	
	private Random random 		 = new Random();
	
	public World() {
		int wid = outTrack.width/2  - 2*Player.WIDTH; 
		int hei = outTrack.height/2 - 2*Player.HEIGHT; 
                
                int x;
                int y;
                
		for(int i=0;i<playersA.length;++i) {			
                    
                    do{
                        x = -(random.nextInt(wid) + Player.WIDTH);
                        y = -(random.nextInt(hei) + Player.HEIGHT);
                    }while(!MathGeometry.calculePointIntoEllipse(this.WIDTH, 
                            this.FOCUS1X, this.FOCUS1Y, 
                            this.FOCUS2X, this.FOCUS2Y, x, y));
                        
                    playersA[i] = new Player(this, x, y, PlayerTeam.TEAM_A);
		}

		for(int i=0;i<playersB.length;++i) {	
                    do{
                        x = (random.nextInt(wid) + Player.WIDTH);
                        y = (random.nextInt(hei) + Player.HEIGHT);
                    }while(!MathGeometry.calculePointIntoEllipse(this.WIDTH, 
                            this.FOCUS1X, this.FOCUS1Y, 
                            this.FOCUS2X, this.FOCUS2Y, x, y));

                    playersB[i] = new Player(this, x, y, PlayerTeam.TEAM_B);
		}
		
		goalA = new Goal(this, -64350, 0);
		goalB = new Goal(this, 64350, 0);
		
		rampA = new Ramp(this, -64350, 0);
		rampB = new Ramp(this, 64350, 0);
		
		trampolineA = new Trampoline(this, -64350, 0);
		trampolineB = new Trampoline(this, 64350, 0);
		
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
