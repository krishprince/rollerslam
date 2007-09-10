/*
 * LogAnalysesService.java
 *
 */

package rollerslam.logplayer;

import java.util.List;
import rollerslam.infrastructure.logging.LogEntry;

/**
 *
 * @author Weslei
 */
public interface LogPlayingService {

    public List<LogEntry> getAllLogForAgentInCycle(Integer agentId, String messageType);

    public void load (String simDescription);

    public Integer getTotalCycles();

    public Integer getCurrentCycle();

    public void setCurrentCycle(Integer actualCycle);
    
    public LogEntry getLogForAgent(Integer agId);

    public List<Integer> getAgentsIds();
    
}
