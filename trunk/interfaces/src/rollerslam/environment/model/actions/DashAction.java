package rollerslam.environment.model.actions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

public class DashAction implements Message {
	public Agent agent;
	public int ax;
	public int ay;
	
	public DashAction(Agent a, int ax, int ay) {
		this.agent = a;
		this.ax = ax;
		this.ay = ay;
	}
}
