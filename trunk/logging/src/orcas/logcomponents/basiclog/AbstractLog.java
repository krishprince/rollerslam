/*
 * AbstractLog.java
 * 
 * Created on 05/08/2007, 21:09:06
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orcas.logcomponents.basiclog;

import java.io.Serializable;

/**
 *
 * @author Weslei
 */
public abstract class AbstractLog implements Log {
    
    private LogLevel basicLevel;

    public AbstractLog() {
        this(LogLevel.DEBUG);
    }
    
    public void setLogLevel(LogLevel newLevel) {
        basicLevel = newLevel;
    }
    
    public LogLevel getLogLevel() {
        return basicLevel;
    }
    
    public AbstractLog(LogLevel level) {
        this.basicLevel = level;
    }

    public void log(Serializable message) {
        log(message, LogLevel.DEBUG);
    }

    public void log(Serializable message, LogLevel level) {
        if (level.getLevel() >= basicLevel.level) {
            doLog(message);
        }
    }

    protected abstract void doLog(Serializable message);

}
