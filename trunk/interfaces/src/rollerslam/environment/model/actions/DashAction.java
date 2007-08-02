package rollerslam.environment.model.actions;

import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;

@SuppressWarnings("serial")
public class DashAction extends Message {
	public int ax;
	public int ay;
	
	public DashAction(Agent a, int ax, int ay) {
		super(a);
		this.ax = ax;
		this.ay = ay;
	}
}
