package rollerslam.agent.communicative.test;

import rollerslam.agent.communicative.realization.service.CommunicativeAgentImpl;
import rollerslam.agent.communicative.specification.type.action.AskAction;
import rollerslam.agent.communicative.specification.type.object.StringOID;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.Message;
import rollerslam.infrastructure.specification.type.AgentID;

public class TestDisplay extends CommunicativeAgentImpl {

	private AgentID env;
	
	public TestDisplay(Agent port, AgentID env) {
		super(port, 500);
		
		this.env = env;
	}

	protected Message computeNextAction() {
		AskAction askAction = new AskAction();
		askAction.receiver.add(env);
		askAction.oids.add(new StringOID("semaphor"));
		return askAction;
	}
}
