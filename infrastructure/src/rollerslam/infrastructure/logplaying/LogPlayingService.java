/*
 * LogAnalysesService.java
 *
 */

package rollerslam.infrastructure.logplaying;

import java.util.List;
import rollerslam.environment.model.World;
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
    
    public List<LogEntry> getLogForAgent(Integer agId);
    
    public World getWorld();

}