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
public class PlayerLogParametersPane extends JScrollPane  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2182775289310320465L;
	
	protected JTree agentOptionsTree;
    protected DefaultMutableTreeNode agentOptions = TraceParameters.getPlayerLogParametersAsTreeNode();
    protected CheckTreeManager checkTreeManager;
    
    public PlayerLogParametersPane() {
        agentOptionsTree = new JTree(agentOptions);;
        checkTreeManager = new CheckTreeManager(agentOptionsTree);
        this.setViewportView(agentOptionsTree);
    }
    
    public TreePath[] getSelectionPaths() {
        return checkTreeManager.getSelectionModel().getSelectionPaths();
    }

}
