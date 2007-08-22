package rollerslam.environment.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Fact implements Serializable {
	
	public int    cycle;
	public String sender;
	public String receiver;
	public Serializable message;
	
	public Fact() {
		
	}
	
	public Fact(String sender, String receiver, Serializable message) {
		this.sender   = sender;
		this.receiver = receiver;
		this.message  = message;
	}
	
	public String toString() {
		return sender + " says to " + receiver + ": " + message.toString();		
	}
}
