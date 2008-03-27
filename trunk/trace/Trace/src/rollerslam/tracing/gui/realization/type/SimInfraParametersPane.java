/**
 * 
 */
package rollerslam.tracing.gui.realization.type;

import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import rollerslam.tracing.gui.util.CheckTreeManager;

/**
 * @author Rafael Oliveira
 *
 */
public class SimInfraParametersPane extends JScrollPane  {
	private static final long serialVersionUID = 2182775289310320465L;
	
	protected JTree agentOptionsTree;
    protected DefaultMutableTreeNode agentOptions = TraceParameters.getSimInfraParametersAsTreeNode();
    protected CheckTreeManager checkTreeManager;
    
    public SimInfraParametersPane() {
        agentOptionsTree = new JTree(agentOptions);
        checkTreeManager = new CheckTreeManager(agentOptionsTree);
        this.setViewportView(agentOptionsTree);
    }
    
    public TreePath[] getSelectionPaths() {
    	if (checkTreeManager.getSelectionModel().getSelectionCount() == 0)
    		return new TreePath[]{};
    	return checkTreeManager.getSelectionModel().getSelectionPaths();
    }	

}
