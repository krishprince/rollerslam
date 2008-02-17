package rollerslam.display.realization.service.gui;

import java.util.Vector;

public class MessageHandler {
	
	private static Vector<Fact> facts = new Vector<Fact>();
	private static long removeOn = -1;
	private static Fact currentFact = null;	
	
	public static void scheduleForExhibition(Fact fact) {
		facts.add(fact);
	}
	
	public static String getCurrentMessage() {
		
		long now = System.currentTimeMillis();
		if (now > removeOn && !facts.isEmpty()) {
			currentFact = facts.elementAt(0);
			facts.removeElementAt(0);
			
			removeOn = 0;
		}
		
		if (currentFact != null) {
			return currentFact.toString();
		} else {
			return "";
		}
	}
}
