package rollerslam.infrastructure.logging;

import java.io.Serializable;

public abstract class LogEntry implements Serializable {

    private Integer cycle;
    private Integer agentId;
    private Object additionalData;
    
    protected LogEntry(Integer cycle, Integer agentId, Object additionalData) {
        this.cycle = cycle;
        this.agentId = agentId;
        this.additionalData = additionalData;
    }
    
    protected LogEntry(Integer cycle, Integer agentId) {
        this(cycle, agentId, null);
    }
    
    
    public void setCycle(int nCycle) {
        cycle = nCycle;
    }
    
    public int getCycle() {
        return cycle;
    }
    
    public void setAgentId(int nAgentId) {
        agentId = nAgentId;
    }
    
    public int getAgentId() {
        return agentId;
    }
    
    public void setAdditionalData(Object nData) {
        additionalData = nData;
    }
    
    public Object getAdditionalData() {
        return additionalData;
    }

}
