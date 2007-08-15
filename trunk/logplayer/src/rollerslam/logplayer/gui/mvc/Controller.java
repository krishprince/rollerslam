/*
 * Controller.java
 */

package rollerslam.logplayer.gui.mvc;

import java.io.File;

/**
 *
 * @author Weslei
 */
public interface Controller {
    
    public void load(File db);
    
    public void play();
    
    public void stop();
    
    public void goTo(Integer cycle);

}
