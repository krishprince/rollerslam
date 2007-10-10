package rollerslam.environment;

import java.rmi.RemoteException;
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

import com.parctechnologies.eclipse.CompoundTerm;
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

	private String action;

	private void dash(World w, Player p, Vector vet) {
		action = "dash(" + getIDForObject(p) + ", vector(" + vet.x + ","
				+ vet.y + "))";
	}

	private void hit(World w, Player p, Vector vet) {
		double error = Math.random();
		double result = error % 2;
		action = "hit(" + getIDForObject(p) + "," + error + ","
				+ SimulationSettings.MAX_DISTANCE + ", vector(" + vet.x + ","
				+ vet.y + ")," + result + ")";
	}

	private void kick(World w, Player p, Vector vet) {
		double error = Math.random();
		double result = error % 2;
		action = "kick(" + getIDForObject(p) + "," + error + ", vector("
				+ vet.x + "," + vet.y + ")," + p.strength + "," + result + ")";
	}

	private void countertackle(World w, Player p) {
		action = "counterTackle(" + getIDForObject(p) + ")";
	}

	private void catchA(World w, Player p) {
		action = "catchA(" + getIDForObject(p) + ","
				+ SimulationSettings.MAX_DISTANCE + ")";
	}

	private void release(World w, Player p) {
		action = "release(" + getIDForObject(p) + ")";
	}

	private void tackle(World w, Player p) {
		action = "tackle(" + getIDForObject(p) + ","
				+ getIDForObject(w.playerWithBall) + ","
				+ SimulationSettings.MAX_DISTANCE + ")";
	}

	private void throwA(World w, Player p, Vector vet) {
		double error = Math.random();
		double result = error % 2;
		action = "throwA(" + getIDForObject(p) + "," + error 
				+ ", vector(" + vet.x + "," + vet.y + ")," + p.strength + ","
				+ result + ")";
	}

	private void sendMsg(World w, Agent agent, Fact f) {
		// Body
		f.cycle = w.currentCycle;
	}

	private void standUp(World w, Player p) {
		action = "standUp(" + getIDForObject(p) + ")";
	}

	public void processAction(EnvironmentStateModel w, Message m) {
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
		} else if (m instanceof UpdateScoreAction){
			UpdateScoreAction mt = (UpdateScoreAction) m;
			 ((World)w).scoreboard.scoreTeamA += mt.getScoreTeamA();
			 ((World)w).scoreboard.scoreTeamB += mt.getScoreTeamB();
		};

		// adds all actions to the world
		((World) w).newActions.add(m);

		runFluxAction((World) w);
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
	public static String getIDForObject(WorldObject obj) {
		if (obj == null) {
			return "null";
		} else if (obj instanceof Ball) {
			return "ball";
		} else if (obj instanceof Player) {
			return "player(" + ((Player) obj).id + ")";
		} else {
			return "obj(" + obj.hashCode() + ")";
		}

	}
	

	public FluxActionInterpretationComponent(EclipseConnection eclipse) {
		this.eclipse = eclipse;
		this.javaPrologVisitor = new SampleJavaPrologWorldVisitor();
		this.prologJavaVisitor = new SamplePrologJavaWorldVisitor();
	}

	public void runFluxAction(EnvironmentStateModel world) {
		if (action != null) {
			String query = "runAction(["
					+ javaPrologVisitor.getPrologRepresentation((World) world)
					+ "]," + action + ", " + "R)";

			CompoundTerm ret;
			try {
				ret = eclipse.rpc(query);

				prologJavaVisitor.updateWorldRepresentation((World) world, ret
						.arg(3));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
