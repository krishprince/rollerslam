package rollerslam.infrastructure.server;

import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.logging.LogRecordingService;


public interface ServerInitialization {

	/**
	 * Initializes the server using the passed agent as the environment agent.
	 * The passed agent will never be available to garbage collection.
	 * This method does not return.
	 * 
	 * @param port the name server port
	 * @param environmentAgent the environment agent
	 * @throws Exception
	 */
	void init(int port, AutomataAgent environmentAgent) throws Exception;

    void init(int port, AutomataAgent environmentAgent, LogRecordingService logRecordingService) throws Exception; 
}
