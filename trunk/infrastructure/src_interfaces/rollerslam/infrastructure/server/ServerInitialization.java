package rollerslam.infrastructure.server;

import rollerslam.infrastructure.agent.EnvironmentAgent;


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
	void init(int port, EnvironmentAgent environmentAgent) throws Exception;
}
