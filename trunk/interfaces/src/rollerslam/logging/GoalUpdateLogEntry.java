/*
 * GoalUpdateLogEntry.java
 *
 */

package rollerslam.logging;

import rollerslam.infrastructure.logging.*;

/**
 *
 * @author Weslei
 */
public class GoalUpdateLogEntry extends LogEntry {

    private String reason;

    public GoalUpdateLogEntry(Integer cycle, Integer agentId, String reason) {
        this(cycle, agentId, reason, null);
    }

    public GoalUpdateLogEntry(Integer cycle, Integer agentId, String reason, Object additionalInfo) {
        super(cycle, agentId, additionalInfo);
        this.reason = reason;
    }

    public void setReason(String nReason) {
        reason = nReason;
    }

    public String getReason() {
        return reason;
    }
    
    public String toString() {
        return super.toString() + " reason = " + reason;
    }
}
