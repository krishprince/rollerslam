package rollerslam.discoverer.client;

import java.io.IOException;

import rollerslam.discoverer.util.HostAddress;

public interface ServerDiscoverer {
	public void findServer() throws IOException;
	
	public HostAddress getServerAddress();
	
	public void setTimeout(int timeout);
	
	public int getTimeout();
}
