package rollerslam.infrastructure;

import rollerslam.infrastructure.agent.Agent;

/**
 * Generates proxies on the fly for annotated objects
 * 
 * @author maas
 */
public interface ProxyHelper {
	
	/**
	 * @param proxyInterface the interface implemented by the proxy
	 * @param remoteAgent the remote agent
	 * @return a proxy for the remote agent
	 */
	Object getProxyForRemoteAgent(Class proxyInterface, Agent remoteAgent);
	
	
}
