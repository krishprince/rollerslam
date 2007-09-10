/*
 * Controller.java
 */

package rollerslam.logplayer.gui.mvc;

import java.io.File;
import java.util.List;
import rollerslam.infrastructure.logging.LogEntry;

/**
 *
 * @author Weslei
 */
public interface Controller {
    
    public void load(File db);
    
    public void play();
    
    public void stop();
    
    public void goTo(Integer cycle);
    
    public void setPlaySpeed(Integer speed);
    
    public Integer getPlaySpeed();

    public List<LogEntry> getLogForAgent(Integer agentId, String messageType);

}
