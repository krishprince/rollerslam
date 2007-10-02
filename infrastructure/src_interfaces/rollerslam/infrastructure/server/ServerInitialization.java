package rollerslam.infrastructure.server;

import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.logging.LogRecordingService;

import com.parctechnologies.eclipse.EclipseConnection;


public interface ServerInitialization {

	/**
	 * Initializes the server using the passed agent as the environment agent.
	 * The passed agent will never be available to garbage collection.
	 * This method does not return.
	 * 
	 * @param environmentAgent the environment agent
	 * @throws Exception
	 */
	void init(AutomataAgent environmentAgent) throws Exception;

    void init(AutomataAgent environmentAgent, LogRecordingService logRecordingService) throws Exception;
    	
	EclipseConnection getEclipseConnection();
	
}
