package rollerslam.infrastructure.realization.service;

import java.util.HashSet;
import java.util.Set;

import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.Message;
import rollerslam.infrastructure.specification.service.SimulationState;
import rollerslam.infrastructure.specification.type.AgentID;

public class AgentImpl implements Agent {

	private SimulationInfrastructureImpl simulation;
	private AgentID						 id;

	public AgentImpl(SimulationInfrastructureImpl simulation, AgentID id) {
		this.simulation = simulation;
		this.id         = id;
	}

	public AgentID getAgentID() {
		return id;
	}

	public Set<Message> getPerceptions() {

		Set<Message> ret = simulation.msgs.get(id) ;

		if (ret != null && !ret.isEmpty()) {
			System.out.println("[" + id + "] GET " + ret);
		}

		if (simulation.getSimAdmin().getState() == SimulationState.RUNNING) {
			synchronized (simulation.token) {
				Set<Message> oldRet = ret;
				ret = new HashSet<Message>(oldRet);
				oldRet.clear();
			}
		}
		return ret;
	}

	public SimulationState getSimulationState() {
		return simulation.getSimAdmin().getState();
	}

	public void sendActions(Set<Message> actions) {
		if (actions != null && !actions.isEmpty()) {
			//TODO refactoring to logger
			System.out.println("[" + id + "] SEND " + actions);
		}

		if (simulation.getSimAdmin().getState() == SimulationState.RUNNING) {
			synchronized (simulation.token) {
				for (Message action : actions) {
					for (AgentID agentID : action.getReceiver()) {
						action.setSender(getAgentID());
						simulation.msgs.get(agentID).add(action);
					}
				}
			}
		}
	}

}
