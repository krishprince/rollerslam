package rollerslam.infrastructure.logging;

import java.io.Serializable;

public abstract class LogEntry implements Serializable {

    private Integer cycle = -1;
    private Integer agentId = -1;
    private Object additionalData = new Object();
    
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
