/*
 * LogPlayingServiceBICTImpl.java
 * 
 * Created on 15/09/2007, 23:11:19
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.logplayer.impl.bict;

import rollerslam.logplayer.bict.LogPlayingServiceBICT;
import rollerslam.logplayer.impl.LogPlayingServiceImpl;

/**
 *
 * @author Weslei
 */
public class LogPlayingServiceBICTImpl extends LogPlayingServiceImpl 
                                        implements LogPlayingServiceBICT {

    public LogPlayingServiceBICTImpl() {
    }

    public boolean testLoad(Object sim) {
        super.load((String)sim);
        if (super.conn != null && super.currentCycle != null) {
            return true;
        }
        return false;
    }

    public boolean testGetTotalCycles() {
        return super.totalCycles != null;
    }

    public boolean testGetCurrentCycle() {
        return super.currentCycle == this.getCurrentCycle();
    }

    public boolean testSetCurrentCycle(Integer c) {
        setCurrentCycle(c);
        return c == getCurrentCycle();
    }

    public boolean testGetLogForAgent(Integer agId) {
        return getLogForAgent(agId) != null;
    }

    public boolean testGetAllLogForAgentInCycle(Integer agId, String messageType) {
        return getAllLogForAgentInCycle(agId, messageType) != null;
    }

    public boolean testGetAgentsIds() {
        return getAgentsIds() != null;
    }
    
    public static void main(String... args) {
        LogPlayingServiceBICTImpl lpsbi = new LogPlayingServiceBICTImpl();
        
        verify("testLoad", lpsbi.testLoad("file:///c:/temp/rollerslam_20070915_194009_160"));
        verify("testGetTotalCycles", lpsbi.testGetTotalCycles());
        verify("testGetCurrentCycle", lpsbi.testGetCurrentCycle());
        verify("testSetCurrentCycle", lpsbi.testSetCurrentCycle(1));
        verify("testGetLogForAgent", lpsbi.testGetLogForAgent(-1));
        verify("testGetAllLogForAgentInCycle", lpsbi.testGetAllLogForAgentInCycle(null, null));
        verify("testGetAgentsIds", lpsbi.testGetAgentsIds());
    }
    
    private static void verify(String name, boolean value) {
        if (value) {
            System.out.println(name + " ok.");
        } else {
            System.out.println(name + " not ok.");
        }
    }

}
