/*
 * Log.java
 * 
 * Created on 05/08/2007, 20:50:31
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orcas.logcomponents.basiclog;

/**
 *
 * @author Weslei
 */
public interface Log {
    
    public void log(Object message);
    public void log(Object message, LogLevel level);

}
