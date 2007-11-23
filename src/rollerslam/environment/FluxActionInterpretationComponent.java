package rollerslam.environment;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Hashtable;

import rollerslam.environment.model.Ball;
import rollerslam.environment.model.Fact;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.SimulationSettings;
import rollerslam.environment.model.World;
import rollerslam.environment.model.WorldObject;
import rollerslam.environment.model.actions.ArmAction;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.actions.LegAction;
import rollerslam.environment.model.actions.UpdateScoreAction;
import rollerslam.environment.model.actions.VoiceAction;
import rollerslam.environment.model.actions.arm.CatchAction;
import rollerslam.environment.model.actions.arm.CountertackleAction;
import rollerslam.environment.model.actions.arm.ReleaseAction;
import rollerslam.environment.model.actions.arm.TackleAction;
import rollerslam.environment.model.actions.arm.ThrowAction;
import rollerslam.environment.model.actions.leg.DashAction;
import rollerslam.environment.model.actions.leg.HitAction;
import rollerslam.environment.model.actions.leg.KickAction;
import rollerslam.environment.model.actions.leg.StandUpAction;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.environment.model.utils.Vector;
import rollerslam.environment.visitor.JavaPrologWorldVisitor;
import rollerslam.environment.visitor.PrologJavaWorldVisitor;
import rollerslam.environment.visitor.SampleJavaPrologWorldVisitor;
import rollerslam.environment.visitor.SamplePrologJavaWorldVisitor;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.CompoundTermImpl;
import com.parctechnologies.eclipse.EclipseConnection;

public class FluxActionInterpretationComponent implements
		ActionInterpretationComponent {
	public ServerFacade facade = ServerFacadeImpl.getInstance();

	private EclipseConnection eclipse;

	private JavaPrologWorldVisitor javaPrologVisitor;

	private PrologJavaWorldVisitor prologJavaVisitor;

	public Hashtable<Agent, Player> playersMap = new Hashtable<Agent, Player>();

	public Hashtable<Player, Agent> idsMap = new Hashtable<Player, Agent>();

	public int nextAgentID = 0;

	private  CompoundTerm action;

	private void dash(World w, Player p, Vector vet) {
		action =  new CompoundTermImpl("dash", getIDForObject(p) ,  new CompoundTermImpl("vector", vet.x ,
				vet.y));
	}

	private void hit(World w, Player p, Vector vet) {
		double error = Math.random();
		double result = error % 2;
		action =  new CompoundTermImpl("hit", getIDForObject(p) , error ,
				SimulationSettings.MAX_DISTANCE,  new CompoundTermImpl("vector", vet.x ,
				vet.y ), result);
	}

	private void kick(World w, Player p, Vector vet) {
		double error = Math.random();
		double result = error % 2;
		action =  new CompoundTermImpl("kick", getIDForObject(p) ,  error ,  new CompoundTermImpl("vector",
				vet.x, vet.y ), p.strength, result);
	}

	private void countertackle(World w, Player p) {
		action =  new CompoundTermImpl("counterTackle", getIDForObject(p) );
	}

	private void catchA(World w, Player p) {
		action =  new CompoundTermImpl("catchA", getIDForObject(p) ,
				SimulationSettings.MAX_DISTANCE);
	}

	private void release(World w, Player p) {
		action =  new CompoundTermImpl("release", getIDForObject(p));
	}

	private void tackle(World w, Player p) {
		action =  new CompoundTermImpl("tackle", getIDForObject(p),
				getIDForObject(w.playerWithBall),
				SimulationSettings.MAX_DISTANCE);
	}

	private void throwA(World w, Player p, Vector vet) {
		double error = Math.random();
		double result = error % 2;
		action =  new CompoundTermImpl("throwA", getIDForObject(p) , error ,  new CompoundTermImpl("vector",
				vet.x, vet.y ), p.strength , result );
	}

	private void updateScore(World w, int recentScoreA, int recentScoreB) {

		action =  new CompoundTermImpl("updateScore", recentScoreA , recentScoreB );
	}

	private void sendMsg(World w, Agent agent, Fact f) {
		// Body
		f.cycle = w.currentCycle;
	}

	private void standUp(World w, Player p) {
		action =  new CompoundTermImpl("standUp", getIDForObject(p) );
	}

	public void processAction(EnvironmentStateModel wout, Message m) {
		World w = ((EnvironmentWorldModel)wout).getWorld();
		action = null;

		// Actions of Leg
		if (m instanceof LegAction) {
			if (m instanceof DashAction) {
				// Dash
				DashAction mt = (DashAction) m;
				this
						.dash((World) w, playersMap.get(mt.sender),
								mt.acceleration);
			} else if (m instanceof KickAction) {
				if (m instanceof HitAction) {
					// Hit the ball
					HitAction mt = (HitAction) m;
					this.hit((World) w, playersMap.get(mt.sender),
							mt.acceleration);
				} else {
					// Kick the ball
					KickAction mt = (KickAction) m;
					this.kick((World) w, playersMap.get(mt.sender),
							mt.acceleration);
				}
			} else if (m instanceof StandUpAction) {
				StandUpAction mt = (StandUpAction) m;
				this.standUp((World) w, playersMap.get(mt.sender));
			}
			// Actions of Arm
		} else if (m instanceof ArmAction) {
			if (m instanceof CountertackleAction) {
				// Countertackle Action
				CountertackleAction mt = (CountertackleAction) m;
				this.countertackle((World) w, playersMap.get(mt.sender));
			} else if (m instanceof CatchAction) {
				// Catch the ball
				CatchAction mt = (CatchAction) m;
				this.catchA((World) w, playersMap.get(mt.sender));
			} else if (m instanceof ReleaseAction) {
				// Release the ball
				ReleaseAction mt = (ReleaseAction) m;
				this.release((World) w, playersMap.get(mt.sender));
			} else if (m instanceof TackleAction) {
				// Tackle another player
				TackleAction mt = (TackleAction) m;
				this.tackle((World) w, playersMap.get(mt.sender));
			} else if (m instanceof ThrowAction) {
				// Throw the ball
				ThrowAction mt = (ThrowAction) m;
				this.throwA((World) w, playersMap.get(mt.sender),
						mt.acceleration);
			}
			// Actions of Voice
		} else if (m instanceof VoiceAction) {
			if (m instanceof SendMsgAction) {
				// Send the message ("Say")
				SendMsgAction mt = (SendMsgAction) m;
				this.sendMsg((World) w, mt.sender, mt.subject);
			}
			// Player Join to Game
		} else if (m instanceof JoinGameAction) {
			JoinGameAction mt = (JoinGameAction) m;
			this.joinWorld((World) w, mt.sender, mt.team);
		} else if (m instanceof UpdateScoreAction) {
			UpdateScoreAction mt = (UpdateScoreAction) m;
			this.updateScore((World) w, mt.getScoreTeamA(), mt.getScoreTeamB());
			// ((World) w).scoreboard.scoreTeamA += mt.getScoreTeamA();
			// ((World) w).scoreboard.scoreTeamB += mt.getScoreTeamB();
		}
		;

		// adds all actions to the world
		((World) w).newActions.add(m);

		runFluxAction((EnvironmentWorldModel) wout);
	}

	public void joinWorld(World worldModel, Agent agent, PlayerTeam playerTeam) {

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
				facade.getEnvironmentEffector().doAction(
						new GameStartedPerception(null, agent, body.id));
			} catch (RemoteException e) {
				if (PrintTrace.TracePrint) {
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

	// FLUX
	public static CompoundTerm getIDForObject(WorldObject obj) {
		if (obj == null) {
			return new Atom("null");
		} else if (obj instanceof Ball) {
			return new Atom("ball");
		} else if (obj instanceof Player) {
			return  new CompoundTermImpl("player", ((Player) obj).id);
		} else {
			return  new CompoundTermImpl("obj", obj.hashCode());
		}

	}

	public FluxActionInterpretationComponent(EclipseConnection eclipse) {
		this.eclipse = eclipse;
		this.javaPrologVisitor = new SampleJavaPrologWorldVisitor();
		this.prologJavaVisitor = new SamplePrologJavaWorldVisitor();
	}

	public void runFluxAction(EnvironmentWorldModel world) {
		if (action != null) {
			long bef = System.currentTimeMillis();
			CompoundTerm query = new CompoundTermImpl("runAction",
													  world.getFluents(),
													  action,
													  null);
				
			CompoundTerm ret;
//			System.out.println("query: "+query);
			
			try {
				ret = eclipse.rpc(query);
				long af = System.currentTimeMillis();
				
				System.out.println("time elapsed: " + (af - bef));
//				prologJavaVisitor.updateWorldRepresentation((World) world, ret
//						.arg(3));
				world.setFluents((Collection)ret.arg(3));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
