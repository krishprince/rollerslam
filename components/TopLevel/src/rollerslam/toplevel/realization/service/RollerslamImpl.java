package rollerslam.toplevel.realization.service;
import rollerslam.agent.communicative.specification.service.CommunicativeAgent;
import rollerslam.agent.communicative.specification.type.CommunicativeAgentID;
import rollerslam.display.realization.service.DisplayAgent;
import rollerslam.fluxcommunicativeagent.realization.type.FluxAgentID;
import rollerslam.gamephysics.realization.service.GamePhysicsAgent;
import rollerslam.infrastructure.realization.service.SimulationInfrastructureImpl;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.SimulationInfrastructure;
import rollerslam.infrastructure.specification.service.SimulationState;
import rollerslam.infrastructure.specification.type.AgentID;
import rollerslam.player.realization.service.PlayerAgent;
import rollerslam.toplevel.specification.service.Rollerslam;


public class RollerslamImpl extends Rollerslam {

	private static final String TEAM_A = "TEAM_A";
	public SimulationInfrastructure infrastructure;
	public CommunicativeAgent		display;
	public CommunicativeAgent		gamePhysics;
	public CommunicativeAgent		player;

	public RollerslamImpl() throws Exception {
			infrastructure = new SimulationInfrastructureImpl();

			AgentID displayID = new CommunicativeAgentID("display");
			AgentID gamePhysicsID = new FluxAgentID("gamePhysics");
			AgentID playerID = new FluxAgentID("player");

			Agent displayConnector = infrastructure.connectAgent(displayID);
			Agent gamePhysicsConnector = infrastructure.connectAgent(gamePhysicsID);
			Agent playerConnector = infrastructure.connectAgent(playerID);

			display = new DisplayAgent(displayConnector, gamePhysicsID, 50);
			gamePhysics = new GamePhysicsAgent(gamePhysicsConnector, 1, 50);
			player = new PlayerAgent(playerConnector, 1, TEAM_A, 50);

			infrastructure.getSimAdmin().setState(SimulationState.RUNNING);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		new RollerslamImpl();
	}

}
