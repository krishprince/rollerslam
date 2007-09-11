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
		
		ClientFacade facade = ClientFacadeImpl.getInstance();
		SimulationState state = SimulationState.STOPPED;
		
		try {
			state = facade.getSimulationAdmin().getState();
		} catch (RemoteException e) {
			if (PrintTrace.TracePrint){
				e.printStackTrace();
			}
		}	
		
		if(state == SimulationState.STOPPED){
			return null;			
		}
		
		if(model.currentGoal == AgentGoal.JOIN_GAME){
			m = joinGame(model);
		}else if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {
			//Do nothing
		}else if(model.currentGoal == AgentGoal.INITIALIZATION) {
			m = initialization(model);
		}else if(model.currentGoal == AgentGoal.SET_ROLES) {
			m = setRoles(model);
		} else if (model.currentGoal == AgentGoal.GO_TO_BALL) {
			Player me = model.getMe();

			m = new DashAction(me.world.ball.s.subtract(me.s));
		} else if (model.currentGoal == AgentGoal.GO_TO_INIT_COORD){
			m = goToInitCoord(model);
		} else if (model.currentGoal == AgentGoal.GO_TO_GOAL) {
			Player me = model.getMe();

			World world = (World) model.environmentStateModel;

			if (me.team == PlayerTeam.TEAM_A) {
				m = new DashAction(world.goalB.s.subtract(me.s));
			} else {
				m = new DashAction(world.goalA.s.subtract(me.s));
			}

		} else if (model.currentGoal == AgentGoal.CATCH_BALL) {
			m = new CatchAction();
		} else if (model.currentGoal == AgentGoal.TACKLE_PLAYER) {
			m = new TackleAction();
		} else if (model.currentGoal == AgentGoal.STAND_UP) {
			m = new StandUpAction();
		} else if (model.currentGoal == AgentGoal.THROW_BALL) {
			Player me = model.getMe();
			World world = (World) model.environmentStateModel;

			if (me.team == PlayerTeam.TEAM_A) {

				m = new ThrowAction(world.goalB.s.subtract(me.s));
			} else {			
				m = new ThrowAction(world.goalA.s.subtract(me.s));
			}
        } else if(model.currentGoal == AgentGoal.KICK_BALL){
        	Player me = model.getMe();
			World world = (World)model.environmentStateModel;
				        	
        	if(me.team == PlayerTeam.TEAM_A){
        		m = new KickAction(world.goalB.s.subtract(me.s));
			}else{
				m = new KickAction(world.goalA.s.subtract(me.s));
			}
        } else if(model.currentGoal == AgentGoal.COUNTER_TACKLE){
        	m = new CountertackleAction();			
        }

		try {
			int cycle = 0;
			if (m != null && (World) model.environmentStateModel != null && model.myID != -1){
				cycle = ((World) model.environmentStateModel).currentCycle;
				
				AgentActionLogEntry envLog = new AgentActionLogEntry(cycle, model.myID, m);

				facade.getLogRecordingService().addEntry(envLog);
			}

		} catch (RemoteException e) {
			if (PrintTrace.TracePrint){
				e.printStackTrace();
			}
			return m;
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

		return new DashAction(((World)model.environmentStateModel).ball.s.subtract(me.s));		
	}

}
