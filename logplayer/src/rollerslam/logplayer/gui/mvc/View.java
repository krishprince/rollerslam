/*
 * View.java
 * 
 * Created on 18/08/2007, 16:23:49
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.logplayer.gui.mvc;

import java.awt.event.ActionEvent;
import java.util.List;

/**
 *
 * @author Weslei
 */
public interface View {
    public void loadSimButtonClick(ActionEvent e);
    public void runStopButtonClick(ActionEvent e);
    public void updateCurrentCycleMsg(Integer c, Integer m);
    public void updateSliderBounds(Integer m);
    public void updateSlider(Integer i);
    public void updateComboAgentsIds(List<Integer> lIds);
}
