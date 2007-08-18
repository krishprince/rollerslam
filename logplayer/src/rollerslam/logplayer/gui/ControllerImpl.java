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
import rollerslam.infrastructure.logging.LogEntry;

import rollerslam.logging.EnvironmentStateLogEntry;
import rollerslam.logplayer.LogPlayingService;
import rollerslam.logplayer.gui.mvc.Controller;
import rollerslam.logplayer.gui.mvc.View;
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

    private boolean playing = false;


    public ControllerImpl(View view, Model model) {
        this.model = model;
        this.view = view;

        lps = new LogPlayingServiceImpl();
        logPlayThread = new Thread(this);
        logPlayThread.start();
    }

    public void load(File db) {
        try {
            String dbUri = db.toURI().toString();
            dbUri = dbUri.substring(0, dbUri.length() - 7);

            lps.load(dbUri);
            max = lps.getTotalCycles() - 1;
            goTo(1);
        } catch (Exception er) {
            throw new RuntimeException(er);
        }
    }

    public void play() {
        playing = true;
    }

    public void stop() {
        playing = false;
    }

    public void goTo(Integer cycle) {
        if (cycle < 1) {
            cycle = 1;
        }
        if (cycle > max) {
            cycle = max;
            if (playing) {
                this.view.runStopButtonClick(null);
            }
        }
        updateState(cycle);
    }

    private void updateState(Integer cycle) {
        lps.setCurrentCycle(cycle);
        view.updateCurrentCycleMsg(cycle, max);
        view.updateSliderBounds(max);
        view.updateSlider(cycle);
        LogEntry o = lps.getLogForAgent(-1);
        if (o != null) {
            model.setModel(((EnvironmentStateLogEntry) o).getWorld());
        } else {
            System.out.println("No entry for agent environment on cycle " + cycle + " with total cycles " + max);
            model.setModel(null);
        }
    }

    public void run() {
        while (true) {
            while (playing) {
                try {
                    goTo(lps.getCurrentCycle() + 1);
                    Thread.sleep(interval);
                } catch (InterruptedException ex) {}
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException ex) {}
        }
    }
}
