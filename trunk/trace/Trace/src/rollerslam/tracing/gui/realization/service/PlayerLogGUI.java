/**
 * 
 */
package rollerslam.tracing.gui.realization.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JLabel;

import rollerslam.tracing.gui.realization.type.PlayerLogParametersPane;
import rollerslam.tracing.gui.specification.service.TraceGUI;

/**
 * @author Rafael Oliveira
 *
 */
public class PlayerLogGUI extends TraceGUI {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8638135794246567432L;
	
	JLabel jlAgentId = new JLabel("Player Id: 10 ");
    JLabel jlAgentTeam = new JLabel("Player Team: East ");
    JLabel jlAgentNumber = new JLabel("Number: 3 ");
    PlayerLogParametersPane playerLogParametersPane = new PlayerLogParametersPane();

    public PlayerLogGUI() {
        initComponents();
    }
    
    protected void initComponents() {
        super.initComponents();
        super.topPane.add(jlAgentId);
        super.topPane.add(jlAgentTeam);
        super.topPane.add(jlAgentNumber);
        
        super.centralPane.setLeftComponent(playerLogParametersPane);        
    }

	@Override
	public void updateOptionsSelection() {
		this.setParameters(this.getSelectionParameters(playerLogParametersPane.getSelectionPaths()));
		//JOptionPane.showMessageDialog(null, playerLogParametersPane.getSelectionPaths());
		
	}

}
