/*
 * GameBounds.java
 *
 * Created on 19/08/2007, 21:35:28
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rollerslam.display.realization.service.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 *
 * @author Weslei
 */
public class GameField {
    
    private double multiplier;
    
    
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

    private static final long[] SCOREBOARD = {0, 0, 18000, 7000};
        
    public GameField() {
        this(0.001);
    }
    
    public GameField(double multiplier) {
        this.multiplier = multiplier * 0.001;
        createImage();
    }
    
    private BufferedImage buffer;
    private BufferedImage bufferSBA;
    private BufferedImage bufferSBB;
    private int width;
    private int height;
    
    private void createImage() {
        width = getSize(WORLD[2]);
        height = getSize(WORLD[3]);
        
        buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB );
        bufferSBA = new BufferedImage(getSize(SCOREBOARD[2]) / 2, getSize(SCOREBOARD[3]), BufferedImage.TYPE_INT_RGB );
        bufferSBB = new BufferedImage(getSize(SCOREBOARD[2]) / 2, getSize(SCOREBOARD[3]), BufferedImage.TYPE_INT_RGB );
        Graphics g = buffer.createGraphics();
        
        g.setColor(Color.GREEN);
        g.fillRect(0, 0, width, height);
        
        
        g.setColor(new Color(16441515)); //FAE0AB
        g.fillOval(getSize(WORLD[0]), getSize(WORLD[1]), getSize(WORLD[2]), getSize(WORLD[3]));

        g.setColor(Color.BLACK);
        g.drawOval(getSize(WORLD[0]), getSize(WORLD[1]), getSize(WORLD[2]), getSize(WORLD[3]));
        
        g.setColor(new Color(16382400)); //F9F9C0
        g.fillOval(getSize(OUTTRACK[0]), getSize(OUTTRACK[1]), getSize(OUTTRACK[2]), getSize(OUTTRACK[3]));

        g.setColor(Color.BLACK);
        g.drawOval(getSize(OUTTRACK[0]), getSize(OUTTRACK[1]), getSize(OUTTRACK[2]), getSize(OUTTRACK[3]));

        //RAMP_A
        g.setColor(new Color(16441515));//FAE0AB
        g.fillOval(getSize(RAMP_A[0]), getSize(RAMP_A[1]), getSize(RAMP_A[2]), getSize(RAMP_A[3]));
                
        //ramp b
        g.fillOval(getSize(RAMP_B[0]), getSize(RAMP_B[1]), getSize(RAMP_B[2]), getSize(RAMP_B[3]));
        
        g.setColor(Color.BLACK);
        g.drawOval(getSize(RAMP_A[0]), getSize(RAMP_A[1]), getSize(RAMP_A[2]), getSize(RAMP_A[3]));
        g.drawOval(getSize(RAMP_B[0]), getSize(RAMP_B[1]), getSize(RAMP_B[2]), getSize(RAMP_B[3]));

        //TRAMP A
        g.setColor(new Color(16382400)); //F9F9C0       
        g.fillOval(getSize(TRAMPOLINE_A[0]), getSize(TRAMPOLINE_A[1]), getSize(TRAMPOLINE_A[2]), getSize(TRAMPOLINE_A[3]));
        
        //tramp b
        g.fillOval(getSize(TRAMPOLINE_B[0]), getSize(TRAMPOLINE_B[1]), getSize(TRAMPOLINE_B[2]), getSize(TRAMPOLINE_B[3]));

        g.setColor(Color.BLACK);
        g.drawOval(getSize(TRAMPOLINE_A[0]), getSize(TRAMPOLINE_A[1]), getSize(TRAMPOLINE_A[2]), getSize(TRAMPOLINE_A[3]));
        g.drawOval(getSize(TRAMPOLINE_B[0]), getSize(TRAMPOLINE_B[1]), getSize(TRAMPOLINE_B[2]), getSize(TRAMPOLINE_B[3]));

        //GOALS
        g.setColor(Color.RED);
        g.drawRect(getSize(GOAL_A[0]), getSize(GOAL_A[1]), getSize(GOAL_A[2]), getSize(GOAL_A[3]));
        
        g.setColor(Color.BLUE);
        g.drawRect(getSize(GOAL_B[0]), getSize(GOAL_B[1]), getSize(GOAL_B[2]), getSize(GOAL_B[3]));
        
        //CENTER
        g.setColor(Color.BLACK);
        g.fillOval(getSize(CENTER1[0]), getSize(CENTER1[1]), getSize(CENTER1[2]), getSize(CENTER1[3]));
        g.drawOval(getSize(CENTER2[0]), getSize(CENTER2[1]), getSize(CENTER2[2]), getSize(CENTER2[3]));
        
        g.setColor(Color.BLUE);
        g.fillRect(getSize(SCOREBOARD[0]), getSize(SCOREBOARD[1]), getSize(SCOREBOARD[2]), getSize(SCOREBOARD[3]));
        g.setColor(Color.RED);
        g.fillRect(getSize(SCOREBOARD[0]), getSize(SCOREBOARD[1]), getSize(SCOREBOARD[2]) / 2, getSize(SCOREBOARD[3]));
        
        g.setColor(Color.BLACK);
        g.drawRect(getSize(SCOREBOARD[0]), getSize(SCOREBOARD[1]), getSize(SCOREBOARD[2]), getSize(SCOREBOARD[3]));
        g.drawRect(getSize(SCOREBOARD[0]), getSize(SCOREBOARD[1]), getSize(SCOREBOARD[2]), getSize(SCOREBOARD[3]));
        
    }
    
    public BufferedImage getImage() {
        return buffer;
    }

    public BufferedImage getScoreboardA() {
        return bufferSBA;
    }
    
    public BufferedImage getScoreboardB() {
        return bufferSBB;
    }

    private int getSize(long s) {
        return (int) (s * multiplier);
    }
    
    public static void main(String args[]) {
        new GameField(4).getImage();
    }
    
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
}
