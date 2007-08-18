package rollerslam.agent.goalbased;

import java.rmi.RemoteException;

import rollerslam.environment.RollerslamEnvironmentAgent;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.leg.DashAction;
import rollerslam.environment.model.actions.leg.StandUpAction;
import rollerslam.environment.model.actions.leg.KickAction;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.actions.JoinGameAction;
import rollerslam.environment.model.actions.arm.CatchAction;
import rollerslam.environment.model.actions.arm.TackleAction;
import rollerslam.environment.model.actions.arm.ThrowAction;
import rollerslam.environment.model.actions.arm.CountertackleAction;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;
import rollerslam.logging.AgentActionLogEntry;;

public class AgentActionGenerator implements
		ModelBasedBehaviorStrategyComponent {

	public Agent remoteThis;

	public AgentActionGenerator(Agent remoteThis) {
		this.remoteThis = remoteThis;
	}

	public Message computeAction(EnvironmentStateModel w) {
		AgentWorldModel model = (AgentWorldModel) w;
		Message m = null;

		if (model.currentGoal == AgentGoal.JOIN_GAME) {
			model.joinMessageSent = true;
			m = new JoinGameAction(remoteThis, model.myTeam);
		} else if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {

		} else if (model.currentGoal == AgentGoal.GO_TO_BALL) {
			Player me = model.getMe();

			m = new DashAction(remoteThis, me.world.ball.s.subtract(me.s));
		} else if (model.currentGoal == AgentGoal.GO_TO_GOAL) {
			Player me = model.getMe();

			World world = (World) model.environmentStateModel;

			if (me.team == PlayerTeam.TEAM_A) {
				m = new DashAction(remoteThis, world.goalB.s.subtract(me.s));
			} else {
				m = new DashAction(remoteThis, world.goalA.s.subtract(me.s));
			}

		} else if (model.currentGoal == AgentGoal.CATCH_BALL) {
			m = new CatchAction(remoteThis);
		} else if (model.currentGoal == AgentGoal.TACKLE_PLAYER) {
			m = new TackleAction(remoteThis);
		} else if (model.currentGoal == AgentGoal.STAND_UP) {
			m = new StandUpAction(remoteThis);
		} else if (model.currentGoal == AgentGoal.THROW_BALL) {
			Player me = model.getMe();
			World world = (World) model.environmentStateModel;

			if (me.team == PlayerTeam.TEAM_A) {
				m = new ThrowAction(remoteThis, world.goalB.s.subtract(me.s));
			} else {
				m = new ThrowAction(remoteThis, world.goalA.s.subtract(me.s));
			}
        } else if(model.currentGoal == AgentGoal.KICK_BALL){
        	Player me = model.getMe();
			World world = (World)model.environmentStateModel;
				        	
        	if(me.team == PlayerTeam.TEAM_A){
        		m = new KickAction(remoteThis, world.goalB.s.subtract(me.s));
			}else{
				m = new KickAction(remoteThis, world.goalA.s.subtract(me.s));
			}
        } else if(model.currentGoal == AgentGoal.COUNTER_TACKLE){
        	m = new CountertackleAction(remoteThis);			
        }

		try {
			ServerFacade facade = ServerFacadeImpl.getInstance();
			AgentActionLogEntry envLog = new AgentActionLogEntry(
					((World) model.environmentStateModel).currentCycle, model.myID, m);

			facade.getLogRecordingService().addEntry(envLog);

		} catch (RemoteException e) {
			e.printStackTrace();
			return m;
		}
		
		return m;
	}



}
