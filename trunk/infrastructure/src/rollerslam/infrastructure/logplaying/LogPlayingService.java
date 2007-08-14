/*
 * LogAnalysesService.java
 *
 */

package rollerslam.infrastructure.logplaying;

/**
 *
 * @author Weslei
 */
public interface LogPlayingService {

    public void load (String simDescription);

    public Integer getTotalCycles();

    public Integer getCurrentCycle();

    public void setCurrentCycle(Integer actualCycle);
}
