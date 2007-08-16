/*
 * LogAnalysesService.java
 *
 */

package rollerslam.infrastructure.logplaying;

import rollerslam.infrastructure.logging.LogEntry;

/**
 *
 * @author Weslei
 */
public interface LogPlayingService {

    public void load (String simDescription);

    public Integer getTotalCycles();

    public Integer getCurrentCycle();

    public void setCurrentCycle(Integer actualCycle);
    
    public LogEntry getLogForAgent(Integer agId);
    
}
