/*
 * ControllerImpl.java
 * 
 * Created on 15/08/2007, 11:46:45
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.logplayer.gui;

import java.io.File;
import rollerslam.display.gui.mvc.Model;
import rollerslam.display.gui.mvc.View;
import rollerslam.logging.EnvironmentStateLogEntry;
import rollerslam.logplayer.LogPlayingService;
import rollerslam.logplayer.gui.mvc.Controller;
import rollerslam.logplayer.impl.LogPlayingServiceImpl;



/**
 *
 * @author Weslei
 */
public class ControllerImpl implements Controller, Runnable {

    private Model model;
    
    private View view;
    
    private LogPlayingService lps;
    
    private Thread logPlayThread;
    
    private Integer max;
    
    protected long interval = 50;
    
    
    public ControllerImpl(View view, Model model) {
        this.model = model;
        this.view = view;
        
        lps = new LogPlayingServiceImpl();
        logPlayThread = new Thread(this);
        
    }

    public void load(File db) {
        try {
            lps.load(db.toURI().toString());
            max = lps.getTotalCycles();
            goTo(0);
        } catch (Exception er) {
            throw new RuntimeException(er);
        }
    }

    public void play() {
        logPlayThread.run();
    }

    public void stop() {
        logPlayThread.stop();
    }

    public void goTo(Integer cycle) {
        if (cycle < 0) { 
            cycle = 0;
        }
        if (cycle > max) { 
            cycle = max; 
        }
        updateState(cycle);
    }
    
    private void updateState(Integer cycle) {
        lps.setCurrentCycle(cycle);
        model.setModel(((EnvironmentStateLogEntry)lps.getLogForAgent(-1)).getWorld());
    }

    public void run() {
        while (lps.getCurrentCycle() <= max) {
            try {
                goTo(lps.getCurrentCycle() + 1);
                java.lang.Thread.sleep(interval);
            } catch (InterruptedException ex) {
                //do nothing
            }
        }
    }

}
