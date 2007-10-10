package rollerslam.infrastructure.client.communication;

public interface CommunicationFacade {
	void connect(String host, SimulationObserver observer) throws Exception;
}
