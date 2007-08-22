/*
 * LogFactory.java
 *
 */

package orcas.logcomponents.basiclog;

import java.util.Properties;

/**
 *
 * @author Weslei
 */
public class LogFactory {

    private static LogFactory instance;
    private AbstractLog log;

    private LogFactory(Properties props) throws Exception {
        String logClass = props.getProperty("concrete.log.class");
        if (logClass == null || "".equals(logClass.trim())) {
            logClass = "orcas.logcomponents.basiclog.SystemConsoleLog";
        }
        log = (AbstractLog) Class.forName(logClass).newInstance();

        log.init(props);

        int logLevel = Integer.parseInt(props.getProperty("log.level"));

        log.setLogLevel(LogLevel.get(logLevel));
    }

    public static LogFactory getInstance(Properties props) {
        if (instance == null) {
            synchronized (LogFactory.class) {
                if (instance == null) {
                    try {
                        instance = new LogFactory(props);
                    } catch (Exception err) {
                        throw new RuntimeException("Error initializing logFactory. Details: " + err, err);
                    }
                }
            }
        }
        return getInstance();
    }
    
    public static LogFactory getInstance() {
        if (instance == null) {
            throw new RuntimeException("LogFactory not initialized. Call getInstance(props) at first usage.");
        }
        return instance;
    }

    public Log getLog() {
        return log;
    }
}
