package rollerslam.infrastructure.server;

import java.io.Serializable;

/**
 * This Message is used to automatic message passing
 * 
 * @author maas
 */
public class MethodCallMessage implements Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 9019903771277568548L;
	public String methodName;
	public Serializable[] parameters;
	
	public String toString() {
		String ret = methodName + "(";
		
		for (Serializable s : parameters) {
			ret += s + ", ";
		}
		
		ret += ")";
		return ret;
	}
}
