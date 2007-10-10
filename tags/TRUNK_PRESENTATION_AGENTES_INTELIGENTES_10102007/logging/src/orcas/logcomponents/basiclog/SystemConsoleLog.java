/*
 * SystemConsoleLog.java
 * 
 * Created on 05/08/2007, 20:51:31
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orcas.logcomponents.basiclog;

import java.io.Serializable;
import java.util.Properties;

/**
 *
 * @author Weslei
 */
class SystemConsoleLog extends AbstractLog {

    SystemConsoleLog() {
    }

    public void doLog(Serializable message) {
        System.out.println(message);
    }

    protected void init(Properties logProperties) {}

}
