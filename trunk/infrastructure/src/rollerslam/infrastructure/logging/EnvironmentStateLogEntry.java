/*
 * EnvironmentStateLogEntry.java
 * 
 * Created on 10/08/2007, 19:21:52
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.logging;

import rollerslam.environment.model.World;

/**
 *
 * @author Weslei
 */
public class EnvironmentStateLogEntry extends LogEntry {

    private World world;
    
    public void setWorld(World nWorld) {
        world = nWorld;
    }
    
    public World getWorld() {
        return world;
    }
}
