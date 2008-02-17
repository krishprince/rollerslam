package rollerslam.gamephysics.realization.service;


import java.util.Random;
import java.util.Set;

import rollerslam.agent.communicative.realization.service.CommunicativeAgentImpl;
import rollerslam.agent.communicative.specification.type.fluent.FluentObject;
import rollerslam.common.DomainSettings;
import rollerslam.common.actions.DashAction;
import rollerslam.common.datatype.PlayerTeam;
import rollerslam.common.objects.Ball;
import rollerslam.common.objects.Player;
import rollerslam.common.objects.oid.BallOID;
import rollerslam.common.objects.oid.PlayerOID;
import rollerslam.common.objects.state.AnimatedObject;
import rollerslam.common.objects.state.BallState;
import rollerslam.common.objects.state.PlayerState;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.Message;
import rollerslam.utils.MathGeometry;
import rollerslam.utils.Vector;

public class GamePhysicsAgent extends CommunicativeAgentImpl {

	private Random random = new Random();
	
	public GamePhysicsAgent(Agent port, final long cycleLength) {
		super(port, cycleLength);		
		
		generateFluentObjects();
		
	}

	private void generateFluentObjects() {		
		Ball ball = new Ball(new BallOID(), new BallState(0, 0));
		this.kb.objects.put(ball.oid, ball);

		// team A
		for(int i=1;i<DomainSettings.PLAYERS_PER_TEAM;++i) {
			Player p = new Player(new PlayerOID(PlayerTeam.TEAM_A, i), new PlayerState(0, 0));
			setPlayersRandomStartingPosition(p);
			this.kb.objects.put(p.oid, p);			
		}
		
		// team B
		for(int i=1;i<DomainSettings.PLAYERS_PER_TEAM;++i) {
			Player p = new Player(new PlayerOID(PlayerTeam.TEAM_B, i), new PlayerState(0, 0));
			setPlayersRandomStartingPosition(p);
			this.kb.objects.put(p.oid, p);			
		}		
	}

	private void setPlayersRandomStartingPosition(Player p) {
		int wid = DomainSettings.OUTTRACK_WIDTH / 2 - 2 * DomainSettings.PLAYER_WIDTH;
		int hei = DomainSettings.OUTTRACK_HEIGHT / 2 - 2 * DomainSettings.PLAYER_HEIGHT;

		int x = 0;
		int y = 0;

		do {
			x = (random.nextInt(wid) + DomainSettings.PLAYER_WIDTH);				

			if (((PlayerOID)p.oid).team == PlayerTeam.TEAM_A) {
				x = -x;				
			}
			
			y = (((random.nextInt(hei) + DomainSettings.PLAYER_HEIGHT)) * 2) - hei;
		} while (!MathGeometry.calculePointIntoEllipse(DomainSettings.OUTTRACK_WIDTH,
				DomainSettings.FOCUS1X, DomainSettings.FOCUS1Y,
				DomainSettings.FOCUS2X, DomainSettings.FOCUS2Y, x,
				y));
		
		((PlayerState)p.state).s = new Vector(x,y);
	}

	protected void processSpecificMessage(Message message) {
		if (message instanceof DashAction) {
			DashAction dashAction = (DashAction) message;
			
			PlayerState ps = (PlayerState) kb.objects.get(dashAction.actor).state;
			ps.a = dashAction.acceleration;
		}
	}
	
	protected Set<Message> processCycle(Set<Message> perceptions) {		
		Set<Message> ret = super.processCycle(perceptions);
		processRamifications();
		return ret;
	}

	protected void processRamifications() {
		for (FluentObject object : kb.objects.values()) {
			AnimatedObject anim = (AnimatedObject) object.state;
			
			anim.v = anim.v.limitModuloTo(anim.maxV);
			anim.a = anim.a.limitModuloTo(anim.maxA);
			
			anim.v = anim.v.sumVector(anim.a);
			anim.v = anim.v.limitModuloTo(anim.maxV);

			anim.s = anim.s.sumVector(anim.v);
		}
	}
}
