package rollerslam.environment;

import java.rmi.RemoteException;
import java.util.Hashtable;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.arm.Antitackle;
import rollerslam.environment.model.actions.ArmAction;
import rollerslam.environment.model.actions.arm.CatchAction;
import rollerslam.environment.model.actions.leg.DashAction;
import rollerslam.environment.model.actions.leg.HitAction;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.actions.leg.KickAction;
import rollerslam.environment.model.actions.leg.StandUpAction;
import rollerslam.environment.model.actions.LegAction;
import rollerslam.environment.model.actions.arm.ReleaseAction;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.actions.arm.TackleAction;
import rollerslam.environment.model.actions.arm.ThrowAction;
import rollerslam.environment.model.actions.VoiceAction;
import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.environment.model.utils.Vector;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;

public class JavaActionInterpretationComponent implements ActionInterpretationComponent {
	private static final int MAX_ACCELERATION = 500;

	public ServerFacade facade = ServerFacadeImpl.getInstance();

	public Hashtable<Agent, Player> playersMap                  = new Hashtable<Agent, Player>();
	public Hashtable<Player, Agent> idsMap                      = new Hashtable<Player, Agent>();
	public int 						  nextAgentID 				  = 0;

	
	private void dash(World w, Player p, Vector vet) {
		//TODO test if p is in w
		if(!p.inGround){					
			p.a = vet.limitModuloTo(MAX_ACCELERATION);
		}
	}
	
	private void hit(World w, Player p, Vector vet){
		//Body
	}

	private void kick(World w, Player p, Vector vet){
		//Body
	}
	
	private void antitackle(World w, Player p){
		//Body
	}
	
	private void catchA(World w, Player p){
		//Body
		if(!w.ball.withPlayer){
			p.hasBall = true;
			p.world.ball.withPlayer = true;
			p.world.playerWithBall = p;
		}
	}
	
	private void release(World w, Player p){
		//Body
	}
	
	private void tackle(World w, Player p){
		//Body
		if(w.playerWithBall != null){
			w.playerWithBall.inGround = true;
			w.playerWithBall.hasBall = false;
			w.ball.withPlayer = false;
			w.playerWithBall = null;
		}
	}
	
	private void throwA(World w, Player p, Vector a){
		//Body
	}
	
	private void sendMsg(World w, Agent agent, Fact f){
		//Body
		f.cycle = w.currentCycle;
		w.saidMessages.add(f);
	}	
	
	private void standUp(World w, Player p){
		//Body
		if(p.inGround){
			p.inGround = false;
		}
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
			if(m instanceof Antitackle){
				//Antitackle Action
				Antitackle mt = (Antitackle) m;
				this.antitackle((World)w, playersMap.get(mt.sender));
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
		}
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
				//TODO As mensagens não estavam sincronizadas. Os Agentes tavam recebendo o mesmo id, ou trocado.  
                agent.setID(body.id);
                facade.getEnvironmentEffector().doAction(new GameStartedPerception(null, body.id));
			} catch (RemoteException e) {
				e.printStackTrace();
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
