/*
 * LogFactory.java
 * 
 */

package orcas.logcomponents.basiclog;

/**
 *
 * @author Weslei
 */
public class LogFactory {
    
    private static LogFactory instance;
    private AbstractLog log;

    public LogFactory() {
        try {
            java.util.Properties p = new java.util.Properties();
            p.load(LogFactory.class.getResourceAsStream("basiclogproperties.properties"));
            
            String logClass = p.getProperty("concrete.log.class");
            if (logClass == null || "".equals(logClass.trim())) {
                logClass = "orcas.logcomponents.basiclog.SystemConsoleLog";
            }
            log = (AbstractLog) Class.forName(logClass).newInstance();
            
            log.init(p);
            
            int logLevel = Integer.parseInt(p.getProperty("log.level"));
            
            log.setLogLevel(LogLevel.get(logLevel));
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
