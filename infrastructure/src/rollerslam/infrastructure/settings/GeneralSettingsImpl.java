/*
 * GeneralSettings.java
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.infrastructure.settings;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.Properties;

/**
 *
 * @author Weslei
 */
public class GeneralSettingsImpl implements Serializable, GeneralSettings {

    private Properties settings;
    private static GeneralSettings instance;
    
    private GeneralSettingsImpl() {
        try {
            settings = new Properties();
            settings.load(ClassLoader.getSystemResourceAsStream("settings.properties"));
        } catch (Exception ex) {
            throw new RuntimeException("Error reading settings properties. Details: " + ex, ex);
        }
    }
    
    public static GeneralSettings getInstance() {
        if (instance == null) {
            synchronized(GeneralSettingsImpl.class) {
                if (instance == null) {
                    instance = new GeneralSettingsImpl();
                }
            }
        }
        return instance;
    }
    public Object getSetting(String settingId) throws RemoteException {
        return settings.get(settingId);
    }

}
