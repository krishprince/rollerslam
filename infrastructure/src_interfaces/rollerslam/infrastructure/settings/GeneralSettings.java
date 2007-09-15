/*
 * GeneralSettings.java
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.settings;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Weslei
 */
public interface GeneralSettings extends Remote {
    
    public static final String PLAYERS_PER_TEAM = "PLAYERS_PER_TEAM";
    public static final String INSTANCES_TO_BE_LOGGED = "INSTANCES_TO_BE_LOGGED";
    public static final String TRACE_EXCEPTIONS = "TRACE_EXCEPTIONS";
    
    public Object getSetting(String settingId) throws RemoteException;

}
