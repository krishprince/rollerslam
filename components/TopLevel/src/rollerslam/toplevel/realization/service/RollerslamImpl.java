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

	private static final int DISPLAY_CYCLE = 500;
	private static final int GAME_PHYSICS_CYCLE = 100;
	private static final int PLAYER_CYCLE = 500;
	
	private static final String TEAM_A = "TEAM_A";
	private static final String TEAM_B = "TEAM_B";
	private static final int NUMBER_PLAYERS = 4;
	
	private SimulationInfrastructure infrastructure;
	private CommunicativeAgent		display;
	private CommunicativeAgent		gamePhysics;
	private CommunicativeAgent		player;

	public SimulationInfrastructure getInfrastructure() {
		return infrastructure;
	}

	public void setInfrastructure(SimulationInfrastructure infrastructure) {
		this.infrastructure = infrastructure;
	}

	public CommunicativeAgent getDisplay() {
		return display;
	}

	public void setDisplay(CommunicativeAgent display) {
		this.display = display;
	}

	public CommunicativeAgent getGamePhysics() {
		return gamePhysics;
	}

	public void setGamePhysics(CommunicativeAgent gamePhysics) {
		this.gamePhysics = gamePhysics;
	}

	public CommunicativeAgent getPlayer() {
		return player;
	}

	public void setPlayer(CommunicativeAgent player) {
		this.player = player;
	}

	public RollerslamImpl() throws Exception {
			infrastructure = new SimulationInfrastructureImpl();

			AgentID displayID = new CommunicativeAgentID("display");
			AgentID gamePhysicsID = new FluxAgentID("gamePhysics");
			AgentID playerID = new FluxAgentID("player");
			
			
			

			Agent displayConnector = infrastructure.connectAgent(displayID);
			Agent gamePhysicsConnector = infrastructure.connectAgent(gamePhysicsID);
			Agent playerConnector = infrastructure.connectAgent(playerID);
			

			display = new DisplayAgent(displayConnector, gamePhysicsID, DISPLAY_CYCLE);
			gamePhysics = new GamePhysicsAgent(gamePhysicsConnector, GAME_PHYSICS_CYCLE, NUMBER_PLAYERS);
			//player = new PlayerAgent(playerConnector, PLAYER_CYCLE, TEAM_A, 1);
			for(int i=1; i<=NUMBER_PLAYERS;i++){		
				AgentID playerIDGen = new FluxAgentID("player"+i);
				Agent playerConnectorGen = infrastructure.connectAgent(playerIDGen);
				player = new PlayerAgent(playerConnectorGen, PLAYER_CYCLE,TEAM_A,i);

			}
			for(int i=1; i<=NUMBER_PLAYERS;i++){		
				AgentID playerIDGen = new FluxAgentID("player"+i);
				Agent playerConnectorGen = infrastructure.connectAgent(playerIDGen);
				player = new PlayerAgent(playerConnectorGen, PLAYER_CYCLE,TEAM_B,i);

			}
	
			infrastructure.getSimAdmin().setState(SimulationState.RUNNING);
	}

	public static void main(String[] args) throws Exception {
		new RollerslamImpl();
	}

}
