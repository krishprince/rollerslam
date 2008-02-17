package rollerslam.display.realization.service;

import javax.swing.BoxLayout;
import javax.swing.JFrame;

import rollerslam.agent.communicative.realization.service.CommunicativeAgentImpl;
import rollerslam.agent.communicative.specification.type.action.AskAllAction;
import rollerslam.agent.communicative.specification.type.fluent.OOState;
import rollerslam.display.realization.service.gui.RollerslamDisplay;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.Message;
import rollerslam.infrastructure.specification.type.AgentID;

public class DisplayAgent extends CommunicativeAgentImpl {
	
	private AgentID gamePhysics;
	
	public DisplayAgent(Agent port, AgentID gamePhysics, long cycleLenght) {
		super(port, cycleLenght);
		this.gamePhysics = gamePhysics;
		
		createDisplayFrame();		
	}

	private void createDisplayFrame() {
        RollerslamDisplay panel = new RollerslamDisplay(this);

        JFrame jf = new JFrame("Rollerslam Display");

        jf.getContentPane().setLayout(new BoxLayout(jf.getContentPane(), BoxLayout.Y_AXIS));
        jf.getContentPane().add(panel);

        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // finally make the window visible
        jf.pack();
        jf.setResizable(false);

        panel.main();
        jf.setVisible(true);		
	}

	protected Message computeNextAction() {
		AskAllAction askAction = new AskAllAction();
		askAction.receiver.add(gamePhysics);
		return askAction;
	}

	public OOState getStateForDisplay() {
		return super.getKnowledgeForAgent(this.gamePhysics);
	}
}
