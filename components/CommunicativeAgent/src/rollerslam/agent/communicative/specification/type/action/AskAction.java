package rollerslam.agent.communicative.specification.type.action;

import java.util.HashSet;
import java.util.Set;

import rollerslam.agent.communicative.specification.type.object.OID;
import rollerslam.infrastructure.specification.service.Message;

public class AskAction extends Message {
	public Set<OID> oids = new HashSet<OID>();
	
	public String toString() {
		return super.toString() + " # ASK_ACTION [" + oids + "]";
	}
}
