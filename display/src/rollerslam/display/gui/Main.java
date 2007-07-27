/*
 * server.java
 * 
 */

package rollerslam.display.gui;

import javax.swing.JFrame;

import rollerslam.infrastructure.client.ClientFacadeImpl;

/**
 *
 * @author Weslei
 */
public class Main {

    /** Creates a new instance of server */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		ClientFacadeImpl.getInstance().init(args[0]);
		
    	// TODO code application logic here
        ServerGUI s = new ServerGUI();
        
    }

}
