package rollerslam.environment.model;

import java.io.Serializable;
import java.util.Random;
import java.util.Vector;

import rollerslam.environment.model.visitor.Visitable;
import rollerslam.environment.model.visitor.Visitor;
import rollerslam.environment.model.utils.MathGeometry;

import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;

@SuppressWarnings("serial")
public class World extends EnvironmentStateModel implements Serializable, Visitable  {

	public int           currentCycle  = 0;
	
	public Vector<Message> actions           = new Vector<Message>();
	public Vector<Message> newActions        = new Vector<Message>(); // TODO ajeitar essa gambiarra!!
	
	public OutTrack     outTrack       = new OutTrack(this);
	public Ball         ball      	   = new Ball(this, 0, 0);
	public Player       playersA[] 	   = new Player[SimulationSettings.PLAYERS_PER_TEAM];
	public Player       playersB[] 	   = new Player[SimulationSettings.PLAYERS_PER_TEAM];
	public Goal         goalA          = new Goal(this, SimulationSettings.GOAL_A_X,
										               SimulationSettings.GOAL_A_Y);
	public Goal         goalB          = new Goal(this, -SimulationSettings.GOAL_A_X,
										 		        SimulationSettings.GOAL_A_Y);
	public Ramp         rampA          = new Ramp(this, SimulationSettings.GOAL_A_X,
										 	           SimulationSettings.GOAL_A_Y);
	public Ramp         rampB 		   = new Ramp(this, -SimulationSettings.GOAL_A_X,
												        SimulationSettings.GOAL_A_Y);
	public Trampoline   trampolineA    = new Trampoline(this, SimulationSettings.GOAL_A_X,
										 			         SimulationSettings.GOAL_A_Y);
	public Trampoline   trampolineB    = new Trampoline(this, -SimulationSettings.GOAL_A_X,
										 			          SimulationSettings.GOAL_A_Y);;        
    public Player       playerWithBall = null;

	private Random      random 		   = new Random();
	
	public World() {
		int wid = outTrack.width / 2 - 2 * SimulationSettings.PLAYER_WIDTH;
		int hei = outTrack.height / 2 - 2 * SimulationSettings.PLAYER_HEIGHT;

		int x;
		int y;

		for (int i = 0; i < playersA.length; ++i) {

			do {
				x = -(random.nextInt(wid) + SimulationSettings.PLAYER_WIDTH);
				y = (((random.nextInt(hei) + SimulationSettings.PLAYER_HEIGHT)) * 2) - hei;
			} while (!MathGeometry.calculePointIntoEllipse(outTrack.width,
					SimulationSettings.FOCUS1X, SimulationSettings.FOCUS1Y,
					SimulationSettings.FOCUS2X, SimulationSettings.FOCUS2Y, x,
					y));

			playersA[i] = new Player(this, x, y, PlayerTeam.TEAM_A);
		}

		for (int i = 0; i < playersB.length; ++i) {
			do {
				x = (random.nextInt(wid) + SimulationSettings.PLAYER_WIDTH);
				y = (((random.nextInt(hei) + SimulationSettings.PLAYER_HEIGHT)) * 2) - hei;
			} while (!MathGeometry.calculePointIntoEllipse(outTrack.width,
					SimulationSettings.FOCUS1X, SimulationSettings.FOCUS1Y,
					SimulationSettings.FOCUS2X, SimulationSettings.FOCUS2Y, x,
					y));

			playersB[i] = new Player(this, x, y, PlayerTeam.TEAM_B);
		}
	}

	public void accept(Visitor visitor) {
		visitor.visit((World) this);

		outTrack.accept(visitor);
		ball.accept(visitor);
		goalA.accept(visitor);
		goalB.accept(visitor);
		rampA.accept(visitor);
		rampB.accept(visitor);
		trampolineA.accept(visitor);
		trampolineB.accept(visitor);

		for (int i = 0; i < playersA.length; ++i) {
			playersA[i].accept(visitor);
		}

		for (int i = 0; i < playersB.length; ++i) {
			playersB[i].accept(visitor);
		}
	}
}
