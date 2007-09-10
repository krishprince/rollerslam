package rollerslam.agent.player;

import javax.swing.JOptionPane;

import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.SimulationSettings;
import rollerslam.infrastructure.client.ClientFacadeImpl;

public class RollerslamTeam {
	public static void main(String[] args) throws Exception {		
		ClientFacadeImpl.getInstance().getClientInitialization().init();
		mainAllInOne(args);
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void mainAllInOne(String[] args) throws Exception {

		PlayerTeam team = PlayerTeam.TEAM_A;

		String teamStr = JOptionPane.showInputDialog("Which team? [A | B]").toUpperCase();
				
		if (teamStr.equals("A"))
			team = PlayerTeam.TEAM_A;
		else
			team = PlayerTeam.TEAM_B;

		createAgentsInTeam(team);
	}
	
	private static void createAgentsInTeam(PlayerTeam team) throws Exception {
		for(int i=0;i<SimulationSettings.PLAYERS_PER_TEAM;++i) {
				new RollerslamPlayerAgent(team);
		}
	}

}
