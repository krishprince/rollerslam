/*
 * LogFactory.java
 * 
 * Created on 05/08/2007, 20:51:06
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orcas.logcomponents.basiclog;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Weslei
 */
public class LogFactory {
    
    private static LogFactory instance;
    private Log log;

    public LogFactory() {
        try {
            java.util.Properties p = new java.util.Properties();
            p.load(LogFactory.class.getResourceAsStream("basiclogproperties.properties"));
            log = (Log) Class.forName(p.getProperty("concrete.log.class")).newInstance();
            int logLevel = Integer.parseInt(p.getProperty("log.level"));
            
            ((AbstractLog)log).setLogLevel(LogLevel.get(logLevel));
        } catch (Exception ex) {
            throw new RuntimeException("Error to instantiate log system... \n\n" + ex);
        }
        
    }
    
    public static LogFactory getInstance() {        
        if (instance == null) {
            synchronized(LogFactory.class) {
                if (instance == null) {
                    instance = new LogFactory();
                }
            }
        }
        return instance;
    }
    
    public Log getLog() {
        return log;
    }

}
