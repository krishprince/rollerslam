package rollerslam.infrastructure.agent;

import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class EffectorSensorImpl implements Effector, Sensor {

	private boolean clearOnRead;
	private boolean clearOnWrite;
	
	private Set<Message> messages = new HashSet<Message>();
	
	public EffectorSensorImpl(boolean clearOnRead, boolean clearOnWrite) {
		this.clearOnRead = clearOnRead;
		this.clearOnWrite = clearOnWrite;
	}

	public synchronized void clear() {
		messages.clear();
	}
	
	public synchronized Set<Message> getActions() throws RemoteException {
		Set<Message> ret = new HashSet<Message>();
		ret.addAll(messages);
		
		if (clearOnRead) {
			clear();
		}
		
		return ret;
	}

	public synchronized void doAction(Message m) throws RemoteException {
		if (clearOnWrite) {
			clear();
		}
		messages.add(m);
	}

}
