/*
 */

package rollerslam.display.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;

import javax.swing.JLabel;

import rollerslam.display.gui.mvc.Model;
import rollerslam.display.gui.sprite.SpriteKind;
import rollerslam.display.gui.sprite.SpriteStore;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.SimulationSettings;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.infrastructure.agent.Message;

/**
 *
 * @author Weslei
 */
@SuppressWarnings(value = "serial")
public class GameCanvas extends Canvas {

    /** The stragey that allows us to use accelerate page flipping */
    private BufferStrategy strategy;

    private World world;
 
    private Model model;
    private SpriteStore ss;

    private JLabel messagesLabel;
    
    public GameCanvas(JLabel messages) {
        setBounds(0, 0, 800, 600);

        this.messagesLabel = messages;
        
        // Tell AWT not to bother repainting our canvas since we're
        // going to do that our self in accelerated mode
        setIgnoreRepaint(true);
        ss = SpriteStore.get();
    }

	public void init() {
        // create the buffering strategy which will allow AWT
        // to manage our accelerated graphics
        createBufferStrategy(2);

        strategy = getBufferStrategy();

        updateGraphics();
    }
    
    private void updateGraphics() {
        new Thread() {

            public void run() {
                while (true) {

                    Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

                    g.setColor(Color.GREEN);
                    g.fillRect(0, 0, 800, 600);
                    ss.getSprite(SpriteKind.FIELD_BACKGROUND).draw(g, 0, 0);

                    world = model.getModel();
                    if (world != null) {
                        for (Player player : world.playersA) {
                            ss.getSprite(SpriteKind.RED_PLAYER).draw(g, translatex(player.s.x), translatey(player.s.y));
                        }

                        for (Player player : world.playersB) {
                            ss.getSprite(SpriteKind.BLUE_PLAYER).draw(g, translatex(player.s.x), translatey(player.s.y));
                        }

                        ss.getSprite(SpriteKind.BALL).draw(g, translatex(world.ball.s.x), translatey(world.ball.s.y));
                        
                        for (Message message : world.actions) {
                        	if (message instanceof SendMsgAction) {
                        		MessageHandler.scheduleForExhibition(((SendMsgAction)message).subject);
                        	}
						}
                        
                        messagesLabel.setText(MessageHandler.getCurrentMessage());
                    }

                    g.dispose();
                    strategy.show();

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    private int translatex(int sx) {
        return ((sx + SimulationSettings.OUTTRACK_WIDTH / 2) * 782) / SimulationSettings.OUTTRACK_WIDTH;
    	//return (sx * 764) / World.WIDTH / 2;
    }

    private int translatey(int sy) {
        return ((sy + SimulationSettings.OUTTRACK_HEIGHT / 2) * 582) / SimulationSettings.OUTTRACK_HEIGHT;
    	//return (sy * 564) / World.HEIGHT / 2; 
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
