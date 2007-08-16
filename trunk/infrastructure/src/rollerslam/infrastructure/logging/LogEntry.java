package rollerslam.infrastructure.logging;

import java.io.Serializable;
import java.util.Date;

public abstract class LogEntry implements Serializable {

    private Integer cycle;
    private Integer agentId;
    private Date timestamp;
    private Object additionalData;
    
    protected LogEntry(Integer cycle, Integer agentId, Object additionalData) {
        this.cycle = cycle;
        this.agentId = agentId;
        this.additionalData = additionalData;
        timestamp = new Date();
    }
    
    protected LogEntry(Integer cycle, Integer agentId) {
        this(cycle, agentId, null);
    }
    
    
    public void setCycle(Integer nCycle) {
        cycle = nCycle;
    }
    
    public Integer getCycle() {
        return cycle;
    }
    
    public void setAgentId(Integer nAgentId) {
        agentId = nAgentId;
    }
    
    public Integer getAgentId() {
        return agentId;
    }
    
    public void setAdditionalData(Object nData) {
        additionalData = nData;
    }
    
    public Object getAdditionalData() {
        return additionalData;
    }
    
    public Date getTimestamp() {
        return this.timestamp;
    }
    
    public String toString() {
        return "LogEntry: cycle = " + cycle + " aid = " + agentId + " ts = " + timestamp
                + "addData = " + additionalData;
    }

}
