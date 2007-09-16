package rollerslam.environment;

import java.rmi.RemoteException;
import java.util.Hashtable;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.arm.CountertackleAction;
import rollerslam.environment.model.actions.ArmAction;
import rollerslam.environment.model.actions.SentinelAction;
import rollerslam.environment.model.actions.UpdateScoreAction;
import rollerslam.environment.model.actions.arm.CatchAction;
import rollerslam.environment.model.actions.leg.DashAction;
import rollerslam.environment.model.actions.leg.HitAction;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.actions.leg.KickAction;
import rollerslam.environment.model.actions.leg.StandUpAction;
import rollerslam.environment.model.actions.LegAction;
import rollerslam.environment.model.actions.arm.ReleaseAction;
import rollerslam.environment.model.actions.sentinel.CheckAliveAction;
import rollerslam.environment.model.actions.sentinel.CreatePlayer;
import rollerslam.environment.model.actions.sentinel.KillPlayerAction;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.actions.arm.TackleAction;
import rollerslam.environment.model.actions.arm.ThrowAction;
import rollerslam.environment.model.actions.VoiceAction;
import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.environment.model.utils.MathGeometry;
import rollerslam.environment.model.utils.Vector;
import rollerslam.environment.model.SimulationSettings;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;

public class JavaActionInterpretationComponent implements ActionInterpretationComponent {
	public ServerFacade facade = ServerFacadeImpl.getInstance();

	public Hashtable<Agent, Player> playersMap                  = new Hashtable<Agent, Player>();
	public Hashtable<Player, Agent> idsMap                      = new Hashtable<Player, Agent>();
	public int 						nextAgentID 				= 0;

	
	private void dash(World w, Player p, Vector vet) {
		//TODO test if p is in w
		if(!p.inGround){					
			p.a = vet.limitModuloTo(p.maxA);
		}
	}
	
	private void hit(World w, Player p, Vector vet){
		//Body
		if(!w.ball.withPlayer && !p.inGround){
			if(MathGeometry.calculeDistancePoints(w.ball.s.x, p.s.x, w.ball.s.y, p.s.y) < SimulationSettings.MAX_DISTANCE){
				double error = Math.random();
				
				if(error > 0.3){
					error = 0.3;
				}
				
				w.ball.a = vet;

				if(error % 2 == 0)
					w.ball.a.x = (int)Math.floor(w.ball.a.x * error);
				else
					w.ball.a.y = (int)Math.floor(w.ball.a.y * error);
			}
		}
	}

	private void kick(World w, Player p, Vector vet){
		//Body
		if(p.hasBall && !p.inGround){
			double error = Math.random();
			
			if(error > 0.3){
				error = 0.3;
			}
			
			p.hasBall = false;
			w.playerWithBall = null;
			w.ball.withPlayer = false;
			
			w.ball.a = vet.multVector((1 + p.strength) * 1.25);
			
			if(error % 2 == 0)
				w.ball.a.x = (int)Math.floor(w.ball.a.x * error);
			else
				w.ball.a.y = (int)Math.floor(w.ball.a.y * error);
		}
	}
	
	private void countertackle(World w, Player p){
		//Body
		if(!p.inGround){
			p.counterTackle = true;
		}
	}
	
	private void catchA(World w, Player p){
		//Body
		if(!w.ball.withPlayer && !p.inGround){
			if(MathGeometry.calculeDistancePoints(w.ball.s.x, p.s.x, w.ball.s.y, p.s.y) < SimulationSettings.MAX_DISTANCE){

				p.hasBall = true;
				p.world.ball.withPlayer = true;
				p.world.playerWithBall = p;
			}
		}
	}
	
	private void release(World w, Player p){
		//Body
		if(p.hasBall && !p.inGround){
			p.hasBall = false;
			w.playerWithBall = null;
			w.ball.withPlayer = false;
		}
	}
	
	private void tackle(World w, Player p){
		//Body
		if(w.ball.withPlayer && !p.inGround){
			if(MathGeometry.calculeDistancePoints(w.playerWithBall.s.x, p.s.x, w.playerWithBall.s.y, p.s.y) < SimulationSettings.MAX_DISTANCE){
				if(!w.playerWithBall.counterTackle){
					w.playerWithBall.inGround = true;
					w.playerWithBall.hasBall = false;
					w.ball.withPlayer = false;
					w.playerWithBall = null;
				}else{
				}
			}
		}
	}
	
	private void throwA(World w, Player p, Vector a){
		//Body
		if(p.hasBall && !p.inGround){
			p.hasBall = false;
			w.playerWithBall = null;
			w.ball.withPlayer = false;

			double error = Math.random();
			
			if(error > 0.15){
				error = 0.15;
			}
	
			w.ball.a = a.multVector((1 + p.strength) * 0.75);
			//w.ball.v = p.v.sumVector(a.multVector((1 + p.strength) * 0.5));
			
			if(error % 2 == 0)
				w.ball.a.x = (int)Math.floor(w.ball.a.x * error);
			else
				w.ball.a.y = (int)Math.floor(w.ball.a.y * error);			
		}
	}
	
	private void sendMsg(World w, Agent agent, Fact f){
		//Body
		f.cycle = w.currentCycle;
	}	
	
	private void standUp(World w, Player p){
		//Body
		if(p.inGround){
			p.inGround = false;
		}
	}
	
	private void checkAlive(World w, Player p){
		//Body
		//if(){
			
		//}
		p.dead= true;
	}
	
	private void killPlayer(World w, Player p){
		//Body
		if (p.dead){
			p = null;
		}
		
	}
	
	private void createPlayer(World w, Player p){
		//Body
		
	}
	
	private void updateScore(World w, UpdateScoreAction action) {
		w.scoreboard.scoreTeamA += action.getScoreTeamA();
		w.scoreboard.scoreTeamB += action.getScoreTeamB();
	}
		
	public void processAction(EnvironmentStateModel w, Message m) {

		//Actions of Leg
		if (m instanceof LegAction) {
			if (m instanceof DashAction) {
				//Dash
				DashAction mt = (DashAction) m;
				this.dash((World)w, playersMap.get(mt.sender), mt.acceleration);
			} else if(m instanceof KickAction) {
				if(m instanceof HitAction){
					//Hit the ball
					HitAction mt = (HitAction) m;
					this.hit((World)w, playersMap.get(mt.sender), mt.acceleration);
				}else{
					//Kick the ball
					KickAction mt = (KickAction) m;
					this.kick((World)w, playersMap.get(mt.sender), mt.acceleration);
				}
			} else if (m instanceof StandUpAction) {
				StandUpAction mt = (StandUpAction) m;
				this.standUp((World)w, playersMap.get(mt.sender));
			}
		//Actions of Arm
		} else if (m instanceof ArmAction) {
			if(m instanceof CountertackleAction){
				//Countertackle Action
				CountertackleAction mt = (CountertackleAction) m;
				this.countertackle((World)w, playersMap.get(mt.sender));
			}else if(m instanceof CatchAction){
				//Catch the ball
				CatchAction mt = (CatchAction) m;
				this.catchA((World)w, playersMap.get(mt.sender));
			}else if(m instanceof ReleaseAction){
				//Release the ball
				ReleaseAction mt = (ReleaseAction) m;
				this.release((World)w, playersMap.get(mt.sender));
			}else if(m instanceof TackleAction){
				//Tackle another player
				TackleAction mt = (TackleAction) m;
				this.tackle((World)w, playersMap.get(mt.sender));
			}else if(m instanceof ThrowAction){
				//Throw the ball
				ThrowAction mt = (ThrowAction) m;

				this.throwA((World)w, playersMap.get(mt.sender), mt.acceleration);
			}
		//Actions of Voice
		} else if(m instanceof VoiceAction){
			if(m instanceof SendMsgAction){
				//Send the message ("Say")
				SendMsgAction mt = (SendMsgAction) m;
				this.sendMsg((World)w, mt.sender, mt.subject);
			}
		//Player Join to Game
		} else if (m instanceof JoinGameAction) {
			JoinGameAction mt = (JoinGameAction) m;
			this.joinWorld((World)w, mt.sender, mt.team);
		} else if (m instanceof SentinelAction){
			if (m instanceof CheckAliveAction){
				CheckAliveAction mt = (CheckAliveAction) m;
				this.checkAlive((World) w, playersMap.get(mt.sender));
			}else if (m instanceof KillPlayerAction){
				KillPlayerAction mt = (KillPlayerAction) m;
				this.killPlayer((World) w, playersMap.get(mt.sender));
			}else if (m instanceof CreatePlayer){
				CreatePlayer mt = (CreatePlayer) m;
				this.createPlayer((World) w, playersMap.get(mt.sender));
			}
		}
		
		if(m instanceof UpdateScoreAction) {
			this.updateScore((World)w, (UpdateScoreAction) m);			
		}

		//TODO: retirar essa gambiarra!
		if (((World)w).playerWithBall == null) {
			((World)w).ball.withPlayer = false;
		}
		
		// adds all actions to the world
		((World)w).newActions.add(m);
		
	}

	public void joinWorld(World worldModel, Agent agent, PlayerTeam playerTeam){
		
		Player body = playersMap.get(agent);
		
		if (body == null) {
			if (playerTeam == PlayerTeam.TEAM_A) {
				body = getBodyForAgent(worldModel.playersA);
			} else {
				body = getBodyForAgent(worldModel.playersB);
			}
		}

		if (body != null) {
			playersMap.put(agent, body);
			idsMap.put(body, agent);
                        
			try {
                facade.getEnvironmentEffector().doAction(new GameStartedPerception(null, agent, body.id));
			} catch (RemoteException e) {
				if (PrintTrace.TracePrint){
					e.printStackTrace();
				}
				
			}
		}
	}

	private Player getBodyForAgent(Player[] players) {
		for (int i = 0; i < players.length; ++i) {
			Agent id = idsMap.get(players[i]);
			if (id == null) {
				return players[i];
			}
		}
		return null;
	}

}
