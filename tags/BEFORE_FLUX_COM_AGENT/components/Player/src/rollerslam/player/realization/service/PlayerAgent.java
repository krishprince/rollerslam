package rollerslam.player.realization.service;


import rollerslam.agent.communicative.realization.service.CommunicativeAgentImpl;
import rollerslam.agent.communicative.specification.type.action.AskAction;
import rollerslam.common.actions.DashAction;
import rollerslam.common.objects.Ball;
import rollerslam.common.objects.Player;
import rollerslam.common.objects.oid.BallOID;
import rollerslam.common.objects.oid.PlayerOID;
import rollerslam.common.objects.state.BallState;
import rollerslam.common.objects.state.PlayerState;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.Message;
import rollerslam.infrastructure.specification.type.AgentID;

public class PlayerAgent extends CommunicativeAgentImpl {

	private AgentID gamePhysics;
	private BallOID ballOID = new BallOID();
	private PlayerOID myBody;
	private boolean senseCycle;
	
	public PlayerAgent(Agent port, AgentID gamePhysics, PlayerOID playerBodyOID) {
		super(port, 50);
		
		this.gamePhysics = gamePhysics;
		this.myBody = playerBodyOID;
		senseCycle = true;
	}

	protected Message computeNextAction() {
		Message ret = null;
		
		if (senseCycle) {
			AskAction askAction = new AskAction();
			askAction.oids.add(ballOID);
			askAction.oids.add(myBody);
			askAction.receiver.add(gamePhysics);
			ret = askAction;
		} else {
			Ball ball = (Ball) getKnowledgeForAgent(gamePhysics).objects.get(ballOID);
			Player me = (Player) getKnowledgeForAgent(gamePhysics).objects.get(myBody);			
			
			if (ball != null && me != null) {
				BallState bs = ((BallState)ball.state);
				PlayerState ps = (PlayerState) me.state;
				
				DashAction dashAction = new DashAction(myBody, ps.s.subtract(bs.s));
				dashAction.receiver.add(gamePhysics);
				ret = dashAction;
			}
		}
		
		if (ret != null) {
			ret.sender = agent.getAgentID();
		}
		senseCycle = !senseCycle;
		return ret;
	}
}
