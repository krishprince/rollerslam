package rollerslam.agent.communicative.test;

import rollerslam.agent.communicative.realization.service.CommunicativeAgentImpl;
import rollerslam.agent.communicative.specification.type.object.StringOID;
import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.infrastructure.specification.service.Agent;

public class TestEnv extends CommunicativeAgentImpl {

	public TestEnv(Agent port) {
		super(port, 1600);
		
		Semaphor ns = new Semaphor();		
		ns.value = 0;

		WorldObject fo = new WorldObject(new StringOID("semaphor"), ns);				
		kb.objects.put(fo.oid, fo);
	}

}
