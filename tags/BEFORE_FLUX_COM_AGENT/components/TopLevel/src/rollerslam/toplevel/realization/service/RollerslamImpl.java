package rollerslam.toplevel.realization.service;
import rollerslam.agent.communicative.specification.service.CommunicativeAgent;
import rollerslam.agent.communicative.specification.type.CommunicativeAgentID;
import rollerslam.common.datatype.PlayerTeam;
import rollerslam.common.objects.oid.PlayerOID;
import rollerslam.display.realization.service.DisplayAgent;
import rollerslam.gamephysics.realization.service.GamePhysicsAgent;
import rollerslam.infrastructure.realization.service.SimulationInfrastructureImpl;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.SimulationInfrastructure;
import rollerslam.infrastructure.specification.service.SimulationState;
import rollerslam.infrastructure.specification.type.AgentID;
import rollerslam.player.realization.service.PlayerAgent;
import rollerslam.toplevel.specification.service.Rollerslam;


public class RollerslamImpl extends Rollerslam {

	public SimulationInfrastructure infrastructure;
	public CommunicativeAgent		display;	
	public CommunicativeAgent		gamePhysics;	
	public CommunicativeAgent		player;
	
	public RollerslamImpl() {
			infrastructure = new SimulationInfrastructureImpl();
			
			AgentID displayID = new CommunicativeAgentID("display");
			AgentID gamePhysicsID = new CommunicativeAgentID("gamePhysics");
			AgentID playerID = new CommunicativeAgentID("player");
			
			Agent displayConnector = infrastructure.connectAgent(displayID);
			Agent gamePhysicsConnector = infrastructure.connectAgent(gamePhysicsID);						
			Agent playerConnector = infrastructure.connectAgent(playerID);
			
			display = new DisplayAgent(displayConnector, gamePhysicsID, 50);
			gamePhysics = new GamePhysicsAgent(gamePhysicsConnector, 50);
			player = new PlayerAgent(playerConnector, gamePhysicsID, new PlayerOID(PlayerTeam.TEAM_A, 1));
			
			infrastructure.simAdmin.setState(SimulationState.RUNNING);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new RollerslamImpl();
	}

}