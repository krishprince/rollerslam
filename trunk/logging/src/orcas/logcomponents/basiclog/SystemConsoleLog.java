/*
 * SystemConsoleLog.java
 * 
 * Created on 05/08/2007, 20:51:31
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package orcas.logcomponents.basiclog;

/**
 *
 * @author Weslei
 */
class SystemConsoleLog extends AbstractLog {

    SystemConsoleLog() {
    }

    protected void doLog(Object message) {
        System.out.println(message);
    }

}
