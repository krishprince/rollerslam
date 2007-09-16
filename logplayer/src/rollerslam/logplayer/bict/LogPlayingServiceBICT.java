/*
 * LogPlayingServiceBICT.java
 *
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.logplayer.bict;

import rollerslam.logplayer.LogPlayingService;

/**
 *
 * @author Weslei
 */
public interface LogPlayingServiceBICT extends LogPlayingService {

    public boolean testLoad(Object sim);

    public boolean testGetTotalCycles();

    public boolean testGetCurrentCycle();

    public boolean testSetCurrentCycle(Integer c);

    public boolean testGetLogForAgent(Integer agId);

    public boolean testGetAllLogForAgentInCycle(Integer agId, String messageType);

    public boolean testGetAgentsIds();
}
