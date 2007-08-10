/*
 * GoalUpdateLogEntry.java
 * 
 * Created on 10/08/2007, 19:24:27
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.logging;

/**
 *
 * @author Weslei
 */
public class GoalUpdateLogEntry extends LogEntry {

    private String reason;
    
    public void setReason(String nReason) {
        reason = nReason;
    }

    public String getReason() {
        return reason;
    }
    
}
