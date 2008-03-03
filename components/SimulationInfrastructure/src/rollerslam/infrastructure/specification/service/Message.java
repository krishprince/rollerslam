package rollerslam.infrastructure.specification.service;

import java.util.HashSet;
import java.util.Set;

import rollerslam.infrastructure.specification.type.AgentID;

public abstract class Message {

	private AgentID sender;
	private Set<AgentID> receiver = new HashSet<AgentID>();

	public AgentID getSender() {
		return sender;
	}

	public void setSender(AgentID sender) {
		this.sender = sender;
	}

	public Set<AgentID> getReceiver() {
		return receiver;
	}

	public void setReceiver(Set<AgentID> receiver) {
		this.receiver = receiver;
	}

	public String toString() {
		return "From: " + sender + " To: " + receiver;
	}

}
