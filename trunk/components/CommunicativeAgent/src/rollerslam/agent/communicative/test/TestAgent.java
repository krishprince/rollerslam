package rollerslam.agent.communicative.test;

import rollerslam.agent.communicative.realization.service.CommunicativeAgentImpl;
import rollerslam.agent.communicative.specification.type.action.TellAction;
import rollerslam.agent.communicative.specification.type.object.StringOID;
import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.Message;
import rollerslam.infrastructure.specification.type.AgentID;

public class TestAgent extends CommunicativeAgentImpl {

	private AgentID env;

	public TestAgent(Agent port, AgentID env) {
		super(port, 1500);

		this.env = env;
	}

	protected Message computeNextAction() {
		TellAction tellAction = new TellAction();
		tellAction.getReceiver().add(env);

		Semaphor ns = new Semaphor();

		ns.value = (int) (System.currentTimeMillis() % 10000);

		WorldObject fo = new WorldObject(new StringOID("semaphor"), ns);
		tellAction.objects.add(fo);

		return tellAction;
	}
}
