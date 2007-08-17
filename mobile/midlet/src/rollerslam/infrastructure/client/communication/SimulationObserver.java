package rollerslam.infrastructure.client.communication;

public interface SimulationObserver {
	void notify(SimulationState simulationState);
}
