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
    RED_PLAYER("rollerslam/display/gui/resources/pnb.png"),
    RED_PLAYER_WITH_BALL("rollerslam/display/gui/resources/pwb.png"),
    RED_PLAYER_GROUNDED("rollerslam/display/gui/resources/pnb.png"),
    
    //blue team
    BLUE_PLAYER("rollerslam/display/gui/resources/pnb.png"),
    BLUE_PLAYER_WITH_BALL("rollerslam/display/gui/resources/pwb.png"),
    BLUE_PLAYER_GROUNDED("rollerslam/display/gui/resources/pnb.png"),
    
    //misc
    BALL("rollerslam/display/gui/resources/ball.png"),
    FIELD_BACKGROUND("rollerslam/display/gui/resources/field.png");

    private final String value;
    SpriteKind(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
