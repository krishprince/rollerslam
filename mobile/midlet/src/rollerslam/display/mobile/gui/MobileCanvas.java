package rollerslam.display.mobile.gui;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

import rollerslam.display.mobile.gui.mvc.Model;
import rollerslam.infrastructure.server.PrintTrace;

public class MobileCanvas extends GameCanvas {

	private static final int OUTTRACK_HEIGHT = 138000;
	private static final int OUTTRACK_WIDTH = 188000;
	Graphics g;
	Model model;
	
	int BALL_COLOR = 0xffffff;
	int PLAYER_A_COLOR = 0xff0000;
	int PLAYER_B_COLOR = 0x0000ff;
	int THING_SIZE = 8;
	
	protected MobileCanvas() {
		super(true);
		
		this.setFullScreenMode(true);
		g = getGraphics();
		
		new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						if (PrintTrace.TracePrint){
							e.printStackTrace();
						}
					}
									
					MobileCanvas.this.paint(g);
					
					flushGraphics();
				}
			}
		}).start();
	}

	public void paint(Graphics g) {
        GameField.drawGameField(g, getWidth(), getHeight());
                       
        if (model != null && model.getSimulationState() != null && model.getSimulationState().getData() != null) {
            int[] worldView = (int[]) model.getSimulationState().getData();
            
        	int qtdPA = worldView[2];
        	for(int i=0;i<qtdPA;++i) {
        		int pos = i*2 + 3;        		
        		drawThing(g, PLAYER_A_COLOR, worldView[pos], worldView[pos+1]);
        	}

        	int base = 3 + qtdPA * 2; 
        	int qtdPB = worldView[base];
        	
        	for(int i=0;i<qtdPB;++i) {
        		int pos = i*2 + base + 1;        		
        		drawThing(g, PLAYER_B_COLOR, worldView[pos], worldView[pos+1]);
        	}

        	drawThing(g, BALL_COLOR, worldView[0], worldView[1]);        	
        }
	}
	
	private void drawThing(Graphics g, int color, int wx, int wy) {
		int ox = translatex(wx);
		int oy = translatey(wy);
		
		g.setColor(color);
		g.fillArc(ox-THING_SIZE/2, oy-THING_SIZE/2, THING_SIZE, THING_SIZE, 0, 360);
	}
	
    private int translatex(int sx) {
        return ((sx + OUTTRACK_WIDTH / 2) * getWidth()) / OUTTRACK_WIDTH;
    }

    private int translatey(int sy) {
        return ((sy + OUTTRACK_HEIGHT / 2) * getHeight()) / OUTTRACK_HEIGHT;
    }

	public void setModel(Model model) {
		this.model = model;
	}

}
