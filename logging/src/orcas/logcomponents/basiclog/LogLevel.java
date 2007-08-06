/*
 * LogLevel.java
 *
 * Created on 05/08/2007, 21:06:24
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orcas.logcomponents.basiclog;

/**
 *
 * @author Weslei
 */
public enum LogLevel {

    DEBUG(0),
	AGENT_ACTION(100),
	AGENT_DECISION(200),
	WORLD_STATE(300);

    int level;

    LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }

    public static LogLevel get(int level) {
        switch (level) {
            case 0:
                return LogLevel.DEBUG;
        }
        return LogLevel.DEBUG;
    }
}
