package rollerslam.infrastructure.specification.service;

import java.util.HashSet;
import java.util.Set;

import rollerslam.infrastructure.specification.type.AgentID;

public abstract class Message {

	public AgentID sender;
	public Set<AgentID> receiver = new HashSet<AgentID>();
	
	public String toString() {
		return "From: " + sender + " To: " + receiver;
	}
	
}
