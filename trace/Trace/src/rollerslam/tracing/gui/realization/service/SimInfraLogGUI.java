/**
 * 
 */
package rollerslam.tracing.gui.realization.service;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JLabel;

import rollerslam.tracing.gui.realization.type.SimInfraParametersPane;
import rollerslam.tracing.gui.specification.service.TraceGUI;

/**
 * @author Rafael Oliveira
 *
 */
public class SimInfraLogGUI extends TraceGUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1964932363601864186L;
	
	JLabel jlCommInfo = new JLabel("Simulation Infrastructure");	
	SimInfraParametersPane simInfraParametersPane = new SimInfraParametersPane();

    public SimInfraLogGUI() {
        initComponents();
    }         
    
    protected void initComponents() {
        super.initComponents();
        super.topPane.add(jlCommInfo);
        
        super.centralPane.setLeftComponent(simInfraParametersPane);
    }
    
    public void updateOptionsSelection() {        
    	this.setParameters(this.getSelectionParameters(simInfraParametersPane.getSelectionPaths()));
    }

}
