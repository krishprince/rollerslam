package rollerslam.agent.communicative.realization.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import rollerslam.agent.communicative.specification.service.CommunicativeAgent;
import rollerslam.agent.communicative.specification.type.action.AskAction;
import rollerslam.agent.communicative.specification.type.action.AskAllAction;
import rollerslam.agent.communicative.specification.type.action.TellAction;
import rollerslam.agent.communicative.specification.type.object.OID;
import rollerslam.agent.communicative.specification.type.object.OOState;
import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.Message;
import rollerslam.infrastructure.specification.service.SimulationInfrastructure;
import rollerslam.infrastructure.specification.type.AgentID;

public class CommunicativeAgentImpl extends CommunicativeAgent {

	//TODO compreender o que eh isso?
	private SimulationInfrastructure simulation;
	private long cycleLenght;

	@Override
	public void setSimulationInfrastructure(SimulationInfrastructure simulation) {
		this.simulation = simulation;
	}

	public CommunicativeAgentImpl(final Agent agent, final long cycleLenght) {
		this.setAgent(agent);
		this.setKb(new OOState());
		this.setAgentKB(new HashMap<AgentID, OOState>());
		this.cycleLenght = cycleLenght;
	}

	protected void startThread() {
		new Thread() {
			public void run() {
				while(true) {

					Set<Message> perceptions = CommunicativeAgentImpl.this.getAgent().getPerceptions();
					Set<Message> actions = processCycle(perceptions);

					if (actions != null && !actions.isEmpty()) {
						CommunicativeAgentImpl.this.getAgent().sendActions(actions);
					}

					try {
						Thread.sleep(cycleLenght);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}.start();
	}

	protected Set<Message> processCycle(Set<Message> perceptions) {
		Set<Message> messages = new HashSet<Message>();

		for (Message message : perceptions) {
			if (message instanceof TellAction) {
				TellAction tellAction = (TellAction) message;

				OOState knowledge = getKnowledgeForAgent(tellAction.getSender());

				for (WorldObject object : tellAction.objects) {
					knowledge.objects.put(object.oid, object);
				}
			} else if (message instanceof AskAction) {
				AskAction askAction = (AskAction) message;
				TellAction ta = new TellAction();
				ta.objects = new HashSet<WorldObject>();

				for (OID oid : askAction.oids) {
					WorldObject obj = this.getKb().objects.get(oid);
					if (obj != null) {
						ta.objects.add(obj);
					}
				}

				ta.getReceiver().add(message.getSender());
				messages.add(ta);
			} else if (message instanceof AskAllAction) {
				TellAction ta = new TellAction();
				ta.objects = new HashSet<WorldObject>(this.getKb().objects.values());

				ta.getReceiver().add(message.getSender());
				messages.add(ta);
			} else {
				processSpecificMessage(message);
			}
		}

		Message nextAction = computeNextAction();
		if (nextAction != null) {
			nextAction.setSender(this.getAgent().getAgentID());
			messages.add(nextAction);
		}

		for (Message message : messages) {
			message.setSender(this.getAgent().getAgentID());
		}
		//TODO refactoring to logger
		System.out.println("[" + this.getAgent().getAgentID() + "] KB # " + this.getKb());
		System.out.println("[" + this.getAgent().getAgentID() + "] AKB # " + this.getAgentKB());
		return messages;
	}

	protected OOState getKnowledgeForAgent(AgentID sender) {
		OOState oos = this.getAgentKB().get(sender);

		if (oos == null) {
			oos = new OOState();
			this.getAgentKB().put(sender, oos);
		}

		return oos;
	}

	protected Message computeNextAction() {
		return null;
	}

	protected void processSpecificMessage(Message message) {
		System.out.println("PROC MESSAGE " + message);
	}
}
