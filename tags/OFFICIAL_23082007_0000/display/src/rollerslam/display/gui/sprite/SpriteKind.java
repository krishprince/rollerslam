/*
 * SpriteKind.java
 * 
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.display.gui.sprite;


/**
 *
 * @author Weslei
 */
public enum SpriteKind {
    
    
    //read team
    RED_PLAYER("rollerslam/display/gui/resources/rp.png"),
    RED_PLAYER_WITH_BALL("rollerslam/display/gui/resources/rpb.png"),
    RED_PLAYER_GROUNDED("rollerslam/display/gui/resources/rpg.png"),
    
    //blue team
    BLUE_PLAYER("rollerslam/display/gui/resources/bp.png"),
    BLUE_PLAYER_WITH_BALL("rollerslam/display/gui/resources/bpb.png"),
    BLUE_PLAYER_GROUNDED("rollerslam/display/gui/resources/bpg.png"),
    
    //misc
    BALL("rollerslam/display/gui/resources/b.png");

    private final String value;
    SpriteKind(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
