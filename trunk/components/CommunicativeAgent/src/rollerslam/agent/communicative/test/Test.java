package rollerslam.agent.communicative.test;

import rollerslam.agent.communicative.specification.type.CommunicativeAgentID;
import rollerslam.infrastructure.realization.service.SimulationInfrastructureImpl;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.SimulationState;

public class Test {

	public static void main(String[] a) {
		SimulationInfrastructureImpl sim = new SimulationInfrastructureImpl();

		Agent envConn = sim.connectAgent(new CommunicativeAgentID("ENV"));
		new TestEnv(envConn);
		
		Agent agConn = sim.connectAgent(new CommunicativeAgentID("AGT"));
		new TestAgent(agConn, envConn.getAgentID());

		Agent dispConn = sim.connectAgent(new CommunicativeAgentID("DIS"));
		new TestDisplay(dispConn, envConn.getAgentID());

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("START");
		
		sim.simAdmin.setState(SimulationState.RUNNING);
	}
	
}
