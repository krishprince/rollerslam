package rollerslam.agent.player;

import java.rmi.RemoteException;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.actions.arm.CatchAction;
import rollerslam.environment.model.actions.arm.CountertackleAction;
import rollerslam.environment.model.actions.arm.TackleAction;
import rollerslam.environment.model.actions.arm.ThrowAction;
import rollerslam.environment.model.actions.leg.DashAction;
import rollerslam.environment.model.actions.leg.KickAction;
import rollerslam.environment.model.actions.leg.StandUpAction;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.strategy.InitializationFact;
import rollerslam.environment.model.strategy.Receivers;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.SimulationState;
import rollerslam.logging.AgentActionLogEntry;

public class AgentActionGenerator implements
		ModelBasedBehaviorStrategyComponent {

	public AgentActionGenerator() {
	}

	public Message computeAction(EnvironmentStateModel w) {			
		AgentWorldModel model = (AgentWorldModel) w;
		Message m = null;
		
		if (model != null && model.changed) {
			ClientFacade facade = ClientFacadeImpl.getInstance();
			SimulationState state = SimulationState.STOPPED;

			try {
				state = facade.getSimulationAdmin().getState();
			} catch (RemoteException e) {
				if (PrintTrace.TracePrint) {
					e.printStackTrace();
				}
			}

			if (state == SimulationState.STOPPED) {
				return null;
			}

			if (model.currentGoal == AgentGoal.JOIN_GAME) {
				m = joinGame(model);
			} else if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {
				// Do nothing
			} else if (model.currentGoal == AgentGoal.INITIALIZATION) {
				m = initialization(model);
			} else if (model.currentGoal == AgentGoal.SET_ROLES) {
				m = setRoles(model);
			} else if (model.currentGoal == AgentGoal.GO_TO_BALL) {
				m = goToBall(model);
			} else if (model.currentGoal == AgentGoal.GO_TO_INIT_COORD) {
				m = goToInitCoord(model);
			} else if (model.currentGoal == AgentGoal.STOP) {
				m = stop(model);
			} else if (model.currentGoal == AgentGoal.GO_TO_GOAL) {
				m = goToGoal(model);
			} else if (model.currentGoal == AgentGoal.CATCH_BALL) {
				m = catchBall(model);
			} else if (model.currentGoal == AgentGoal.TACKLE_PLAYER) {
				m = tacklePlayer(model);
			} else if (model.currentGoal == AgentGoal.STAND_UP) {
				m = standUp(model);
			} else if (model.currentGoal == AgentGoal.THROW_BALL) {
				m = throwBall(model);
			} else if (model.currentGoal == AgentGoal.KICK_BALL) {
				m = kickBall(model);
			} else if (model.currentGoal == AgentGoal.COUNTER_TACKLE) {
				m = counterTackle(model);
			}

			model.changed = false;
			
			try {
				int cycle = 0;
				if (m != null && (World) model.environmentStateModel != null
						&& model.myID != -1) {
					cycle = ((World) model.environmentStateModel).currentCycle;

					AgentActionLogEntry envLog = new AgentActionLogEntry(cycle,
							model.myID, m);

					facade.getLogRecordingService().addEntry(envLog);
				}

			} catch (RemoteException e) {
				if (PrintTrace.TracePrint) {
					e.printStackTrace();
				}
				return m;
			}
		}		
		return m;
	}
	
	public Message joinGame(AgentWorldModel model){
		model.joinMessageSent = true;
		return new JoinGameAction(model.myTeam);		
	}
	
	public Message initialization(AgentWorldModel model){
		Fact f = new Fact();
		InitializationFact mesg = new InitializationFact();
		f.sender = String.valueOf(model.myID);
		f.cycle = ((World)model.environmentStateModel).currentCycle;
		
		if(model.myTeam == PlayerTeam.TEAM_A)
			f.receiver = Receivers.COACH_A.getValue();
		else
			f.receiver = Receivers.COACH_B.getValue();
		
		f.message = mesg;
		
		return new SendMsgAction(f);
	}
	
	public Message setRoles(AgentWorldModel model){
		return null;
	}
	
	private Message goToInitCoord(AgentWorldModel model){
		Player me = model.getMe();

		return new DashAction(model.posCoord.subtract(me.s));

	}
	
	public Message goToBall(AgentWorldModel model){
		Player me = model.getMe();
		return new DashAction(me.world.ball.s.subtract(me.s));
	}
	
	private Message stop(AgentWorldModel model){
		Player me = model.getMe();
		return new DashAction(me.v.multVector(-1));
	}
	
	public Message goToGoal(AgentWorldModel model){
		Player me = model.getMe();

		World world = (World) model.environmentStateModel;

		if (me.team == PlayerTeam.TEAM_A) {
			return new DashAction(world.goalB.s.subtract(me.s));
		} else {
			return new DashAction(world.goalA.s.subtract(me.s));
		}
	}
	
	public Message catchBall(AgentWorldModel model){
		return new CatchAction();
	}
	
	private Message tacklePlayer(AgentWorldModel model){
		return new TackleAction();
	}
	
	private Message counterTackle(AgentWorldModel model){
		return new CountertackleAction();
	}
	
	private Message kickBall(AgentWorldModel model){
    	Player me = model.getMe();
		World world = (World)model.environmentStateModel;
			        	
    	if(me.team == PlayerTeam.TEAM_A){
    		return new KickAction(world.goalB.s.subtract(me.s).multVector(10));
		}else{
			return new KickAction(world.goalA.s.subtract(me.s).multVector(10));
		}
	}
	
	private Message throwBall(AgentWorldModel model){
		Player me = model.getMe();
		World world = (World) model.environmentStateModel;

		if (me.team == PlayerTeam.TEAM_A) {
			return new ThrowAction(world.goalB.s.subtract(me.s).multVector(1.5));
		} else {			
			return new ThrowAction(world.goalA.s.subtract(me.s).multVector(1.5));
		}
	}
	
	private Message standUp(AgentWorldModel model){
		return new StandUpAction();
	}
}
