package rollerslam.agent.fluxplayer;

import java.io.File;

import javax.swing.JOptionPane;

import com.parctechnologies.eclipse.EclipseConnection;

import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.SimulationSettings;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.settings.GeneralSettingsImpl;

public class FluxPlayerTeam {
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
        String folder = (String)GeneralSettingsImpl.getInstance().getSetting("PLAYER_FLUX_CODE_HOME"); 

        EclipseConnection eclipse = ClientFacadeImpl.getInstance().getClientInitialization().getEclipseConnection();
        File eclipseProgram = null;
        
        eclipseProgram = new File(folder + "flux.pl");
		eclipse.compile(eclipseProgram);

		eclipseProgram = new File(folder + "fluent.chr");
		eclipse.compile(eclipseProgram);
		
        eclipseProgram = new File(folder + "util.pl");
		eclipse.compile(eclipseProgram);
        
        eclipseProgram = new File(folder + "player.pl");
        eclipse.compile(eclipseProgram);

		for(int i=0;i<SimulationSettings.PLAYERS_PER_TEAM;++i) {
			new FluxPlayerAgent(team);
		}
	}

}
