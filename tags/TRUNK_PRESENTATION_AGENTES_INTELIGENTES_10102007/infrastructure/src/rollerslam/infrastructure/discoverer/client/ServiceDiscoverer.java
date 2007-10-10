package rollerslam.infrastructure.discoverer.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;

public interface ServiceDiscoverer {
	void findServer() throws IOException;
	Set<InetAddress> getFoundAddresses();
	void clearCache();

	void setTimeout(int timeout);	
	int getTimeout();
}
