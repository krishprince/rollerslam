/**
 * 
 */
package rollerslam.tracing.gui.test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import rollerslam.infrastructure.realization.service.SimInfraUtil;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.type.AgentID;
import rollerslam.tracing.gui.realization.service.PlayerLogGUI;
import rollerslam.tracing.gui.realization.service.SimInfraLogGUI;
import rollerslam.tracing.gui.realization.service.TraceUtil;
import rollerslam.tracing.gui.specification.service.TraceGUI;

/**
 * @author Rafael Oliveira
 *
 */
public class TraceAdmin  extends JScrollPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4678559626843133383L;
	protected static JFrame rtdgWindow = new JFrame("Rollerslam - Trace Simulator");
    protected static JPanel southPanel = new JPanel();
	protected static JPanel pane = new JPanel();
	protected static JList agentsList = new JList();
	protected static ArrayList<String> agents = new ArrayList<String>();
	protected static HashMap<String, Agent> agentsMap = new HashMap<String, Agent>();
	protected static TraceGUI traceGUI;
	 

	public TraceAdmin() { 
		initComponents();
	}

	

	protected static void initComponents(){
       
        agentsList.setLayoutOrientation(JList.VERTICAL);
        agentsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        agentsList.setListData(agents.toArray());
                
        agentsList.setVisibleRowCount(12);
        pane.add(new JScrollPane(agentsList));
		rtdgWindow.getContentPane().add(pane);
		rtdgWindow.setSize(500, 300);
        JButton btnSendLog = new JButton("Send Log");
        btnSendLog.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sendLog();
            }
        });
        
        JButton btnNewTrace = new JButton("New Trace...");
        btnNewTrace.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newTrace();
            }
        });
        
        JButton btnGetAgents = new JButton("Get Agents");
        btnGetAgents.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	getAgents();
            }
        });        
        
        southPanel.setLayout(new FlowLayout());
        southPanel.add(btnNewTrace);
        southPanel.add(btnSendLog);
        southPanel.add(btnGetAgents);
        rtdgWindow.getContentPane().add(southPanel, BorderLayout.SOUTH);

        rtdgWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        rtdgWindow.setVisible(true);        
	}
	
	protected static void sendLog(){
		
		//PlayerLog playerLog = new PlayerLog();
		//playerLog.setText("Testanto Log");	
		//TraceUtil.getTraceDriver().doTrace(playerLog);
		
	}
	
	protected static void newTrace(){
		String[] choiceStrings = { "SimInfraLog", "PlayerLog" };
        String choice = OptionStrings.showChoices(choiceStrings);
        
        if ("SimInfraLog".equals(choice)) 
            traceGUI = new SimInfraLogGUI();
         else
        	 traceGUI = new PlayerLogGUI();
    	traceGUI.setAgentIDs(getSelectedAgents());
    	
        TraceUtil.getTraceDriver().add(traceGUI);
	}
	
	public static void addAgent(String id){
		agents.add(id);
		agentsList.setListData(agents.toArray());
		
	}
	
	public static void getAgents(){
		for (Agent agent : SimInfraUtil.getSimulationInfrastructure().getAgent()) {
			agentsMap.put(agent.getAgentID().toString(), agent);
			addAgent(agent.getAgentID().toString());			
		}		
	}	
	
	public static Collection<AgentID> getSelectedAgents(){ 
		Collection<AgentID> results = new ArrayList<AgentID>();
	 
		for (Object agentKey : agentsList.getSelectedValues()) 
			results.add(agentsMap.get(agentKey).getAgentID());
		
		return results;
		
	}

}
