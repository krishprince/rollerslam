/*
 * GameBounds.java
 *
 * Created on 19/08/2007, 21:35:28
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.display.mobile.gui;

import javax.microedition.lcdui.Graphics;


/**
 * This is a copy of /display/src/rollerslam/display/gui/GameField.java
 * 
 * @author Weslei
 */
public class GameField {
        
    //{x, y, width, height}
    private static final long[] WORLD = {0, 0, 188000, 138000};
    private static final long[] OUTTRACK = {16000, 16000, 156000, 106000};
    
    private static final long[] GOAL_A = {30065, 65500, 200, 7000};
    private static final long[] GOAL_B = {157735, 65500, 200, 7000};
    
    private static final long[] RAMP_A = {18665, 54000, 23000, 30000};
    private static final long[] RAMP_B = {146335, 54000, 23000, 30000};
    
    private static final long[] TRAMPOLINE_A = {23165, 58500, 14000, 21000};
    private static final long[] TRAMPOLINE_B = {150835, 58500, 14000, 21000};
    
    private static final long[] CENTER1 = {93000, 68000, 2000, 2000};
    private static final long[] CENTER2 = {92000, 67000, 4000, 4000};

    private static final long[] SCOREBOARD = {170000, 0, 18000, 7000};
                
    private static int width = 0;
    private static int height = 0;
    
    public static void drawGameField(javax.microedition.lcdui.Graphics g, int width, int height) {

    	GameField.width = width;
    	GameField.height = height;
    	
        g.setColor(0x00ff00); // green
        g.fillRect(0, 0, width, height);
        
        
        g.setColor(16441515); //FAE0AB
        fillOval(g, translate_x(WORLD[0]), translate_y(WORLD[1]), translate_x(WORLD[2]), translate_y(WORLD[3]));

        g.setColor(0); // black
        drawOval(g, translate_x(WORLD[0]), translate_y(WORLD[1]), translate_x(WORLD[2]), translate_y(WORLD[3]));
        
        g.setColor(16382400); //F9F9C0
        fillOval(g, translate_x(OUTTRACK[0]), translate_y(OUTTRACK[1]), translate_x(OUTTRACK[2]), translate_y(OUTTRACK[3]));

        g.setColor(0); // black
        drawOval(g, translate_x(OUTTRACK[0]), translate_y(OUTTRACK[1]), translate_x(OUTTRACK[2]), translate_y(OUTTRACK[3]));

        //RAMP_A
        g.setColor(16441515);//FAE0AB
        fillOval(g, translate_x(RAMP_A[0]), translate_y(RAMP_A[1]), translate_x(RAMP_A[2]), translate_y(RAMP_A[3]));
                
        //ramp b
        fillOval(g, translate_x(RAMP_B[0]), translate_y(RAMP_B[1]), translate_x(RAMP_B[2]), translate_y(RAMP_B[3]));
        
        g.setColor(0); // black
        drawOval(g, translate_x(RAMP_A[0]), translate_y(RAMP_A[1]), translate_x(RAMP_A[2]), translate_y(RAMP_A[3]));
        drawOval(g, translate_x(RAMP_B[0]), translate_y(RAMP_B[1]), translate_x(RAMP_B[2]), translate_y(RAMP_B[3]));

        //TRAMP A
        g.setColor(16382400); //F9F9C0       
        fillOval(g, translate_x(TRAMPOLINE_A[0]), translate_y(TRAMPOLINE_A[1]), translate_x(TRAMPOLINE_A[2]), translate_y(TRAMPOLINE_A[3]));
        
        //tramp b
        fillOval(g, translate_x(TRAMPOLINE_B[0]), translate_y(TRAMPOLINE_B[1]), translate_x(TRAMPOLINE_B[2]), translate_y(TRAMPOLINE_B[3]));

        g.setColor(0); //black
        drawOval(g, translate_x(TRAMPOLINE_A[0]), translate_y(TRAMPOLINE_A[1]), translate_x(TRAMPOLINE_A[2]), translate_y(TRAMPOLINE_A[3]));
        drawOval(g, translate_x(TRAMPOLINE_B[0]), translate_y(TRAMPOLINE_B[1]), translate_x(TRAMPOLINE_B[2]), translate_y(TRAMPOLINE_B[3]));

        //GOALS
        g.setColor(0x0000ff); // blue
        
        g.drawRect(translate_x(GOAL_A[0]), translate_y(GOAL_A[1]), translate_x(GOAL_A[2]), translate_y(GOAL_A[3]));
        
        g.drawRect(translate_x(GOAL_B[0]), translate_y(GOAL_B[1]), translate_x(GOAL_B[2]), translate_y(GOAL_B[3]));
        
        //CENTER
        g.setColor(0); //black
        fillOval(g, translate_x(CENTER1[0]), translate_y(CENTER1[1]), translate_x(CENTER1[2]), translate_y(CENTER1[3]));
        drawOval(g, translate_x(CENTER2[0]), translate_y(CENTER2[1]), translate_x(CENTER2[2]), translate_y(CENTER2[3]));
        
//        g.setColor(0x0000ff); // blue
//        g.fillRect(translate_x(SCOREBOARD[0]), translate_y(SCOREBOARD[1]), translate_x(SCOREBOARD[2]), translate_y(SCOREBOARD[3]));
//        g.setColor(0xff0000); // red
//        g.fillRect(translate_x(SCOREBOARD[0]), translate_y(SCOREBOARD[1]), translate_x(SCOREBOARD[2]) / 2, translate_y(SCOREBOARD[3]));
        
//        g.setColor(0); //black
//        g.drawRect(translate_x(SCOREBOARD[0]), translate_y(SCOREBOARD[1]), translate_x(SCOREBOARD[2]), translate_y(SCOREBOARD[3]));
//        g.drawRect(translate_x(SCOREBOARD[0]), translate_y(SCOREBOARD[1]), translate_x(SCOREBOARD[2]) / 2, translate_y(SCOREBOARD[3]));
        
    }
    
    private static void drawOval(Graphics g, int x, int y, int width,
			int height) {
		g.drawArc(x, y, width, height, 0, 360);
	}

	private static void fillOval(Graphics g, int x, int y, int width,
			int height) {
		g.fillArc(x, y, width, height, 0, 360);
	}

	private static int translate_x(long l) {
        return (int)((l * width) / WORLD[2]);
	}
    
    private static int translate_y(long s) {
        return (int)((s * height) / WORLD[3]);
    }
    
}
