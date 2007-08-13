/*
 * AgentActionLogEntry.java
 * 
 * Created on 10/08/2007, 19:23:14
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.logging;

import rollerslam.infrastructure.agent.Message;

/**
 *
 * @author Weslei
 */
public class AgentActionLogEntry extends LogEntry {

    private Message message;
    
    public AgentActionLogEntry(Integer cycle, Integer agentId, Message message) {
        this(cycle, agentId, message, null);
    }
    
    public AgentActionLogEntry(Integer cycle, Integer agentId, Message message, Object additionalInfo) {
        super(cycle, agentId, additionalInfo);
        this.message = message;
    }
    
    
    
    public void setMessage(Message nMessage) {
        message = nMessage;
    }
    
    public Message getMessage() {
        return message;
    }

}
