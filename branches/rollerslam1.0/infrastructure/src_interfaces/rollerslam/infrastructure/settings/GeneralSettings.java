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
    public Object getSetting(String settingId) throws RemoteException;
}
