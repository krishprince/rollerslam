/**
 * 
 */
package rollerslam.tracing.gui.specification.service;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import rollerslam.infrastructure.specification.type.AgentID;
import rollerslam.tracing.gui.realization.service.TraceOutputImpl;

/**
 * @author Rafael Oliveira
 * 
 */
public abstract class TraceGUI extends JScrollPane {

	private Collection<AgentID> agentIDs = new ArrayList<AgentID>();
	private Collection<String> parameters = new ArrayList<String>();
	
    protected TraceOutputImpl driverOutput = new TraceOutputImpl();
    protected JFrame rtdgWindow = new JFrame("Trace Driver GUI");
    protected JPanel topPane = new JPanel();
    protected JSplitPane centralPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    protected JPanel southPanel = new JPanel();

    public TraceGUI() {
    }
    
    protected void initComponents() {
        rtdgWindow.getContentPane().add(topPane, BorderLayout.NORTH);
        
        rtdgWindow.getContentPane().add(centralPane, BorderLayout.CENTER);
       
        rtdgWindow.setSize(800, 600);

        centralPane.setRightComponent(driverOutput);
        
        JButton btnUpdateOptionsSelection = new JButton("Update Options Selection");
        btnUpdateOptionsSelection.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateOptionsSelection();
            }
        });
        
        JButton btnOutputClear = new JButton("Clear Output");
        btnOutputClear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                driverOutput.clear();
            }
        });        
        
        southPanel.setLayout(new FlowLayout());
        southPanel.add(btnUpdateOptionsSelection);
        southPanel.add(btnOutputClear);
        rtdgWindow.getContentPane().add(southPanel, BorderLayout.SOUTH);

        rtdgWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rtdgWindow.setVisible(true);
    }
    
    public void setOutput(String text){
    	driverOutput.setText(text);
    }
    
    public void appendOutput(String text){
    	driverOutput.append(text);
    }
    
    protected Collection<String> getSelectionParameters(TreePath[] selection){
    	Collection<String> results = new ArrayList<String>();
    	for (TreePath treePath : selection) {
    		DefaultMutableTreeNode tp = (DefaultMutableTreeNode)treePath.getPathComponent(treePath.getPathCount()- 1);
    		if (tp.isLeaf()){
    			results.add(treePath.getPathComponent(treePath.getPathCount()- 1).toString());
    		}
    		else if (!(tp.isRoot())){
    			Enumeration e = tp.children();
        		while (e.hasMoreElements()){
        			results.add(e.nextElement().toString());        			
        		}    			
    		}
    		else {
    			Enumeration e = tp.children();
        		while (e.hasMoreElements()){
        			DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.nextElement();
        			Enumeration leaves = node.children();
            		while (leaves.hasMoreElements()){
            			results.add(leaves.nextElement().toString());        			
            		}        			
        		}    			
    		}	
		}
    	return results;
    }
    
    public abstract void updateOptionsSelection();


	public Collection<String> getParameters() {
		return parameters;
	}

	public void setParameters(Collection<String> parameters) {
		this.parameters = parameters;
	}

	public Collection<AgentID> getAgentIDs() {
		return agentIDs;
	}

	public void setAgentIDs(Collection<AgentID> agentIDs) {
		this.agentIDs = agentIDs;
	}
}
