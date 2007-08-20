/*
 */

package rollerslam.display.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import javax.swing.JLabel;
import rollerslam.display.gui.mvc.Model;
import rollerslam.display.gui.sprite.Sprite;
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
    
    private Sprite background;
    private GameField gameField;

    public GameCanvas(JLabel messages) {
        gameField = new GameField(4);

        setBounds(0, 0, gameField.getWidth(), gameField.getHeight());

        this.messagesLabel = messages;

        // Tell AWT not to bother repainting our canvas since we're
        // going to do that our self in accelerated mode
        setIgnoreRepaint(true);
        ss = SpriteStore.get();

        background = new Sprite(gameField.getImage());
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

                    background.draw(g, 0, 0);

                    world = model.getModel();
                    if (world != null) {
                        boolean freeBall = true;
                        for (Player player : world.playersA) {
                            Sprite s = null;
                            if (player.hasBall) {
                                s = ss.getSprite(SpriteKind.RED_PLAYER_WITH_BALL);
                                freeBall = false;
                            } else if (player.inGround) {
                                s = ss.getSprite(SpriteKind.RED_PLAYER_GROUNDED);
                            } else {
                                s = ss.getSprite(SpriteKind.RED_PLAYER);
                            }
                            s.draw(g, translatex(player.s.x) - 15, translatey(player.s.y) - 30);
                        }

                        for (Player player : world.playersB) {
                            Sprite s = null;
                            if (player.hasBall) {
                                s = ss.getSprite(SpriteKind.BLUE_PLAYER_WITH_BALL);
                                freeBall = false;
                            } else if (player.inGround) {
                                s = ss.getSprite(SpriteKind.BLUE_PLAYER_GROUNDED);
                            } else {
                                s = ss.getSprite(SpriteKind.BLUE_PLAYER);
                            }
                            s.draw(g, translatex(player.s.x) - 15, translatey(player.s.y) - 30);
                        }

                        if (freeBall) {
                            ss.getSprite(SpriteKind.BALL).draw(g, translatex(world.ball.s.x) - 5, translatey(world.ball.s.y) - 5);
                        }

                        for (Message message : world.actions) {
                            if (message instanceof SendMsgAction) {
                                MessageHandler.scheduleForExhibition(((SendMsgAction) message).subject);
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
        return ((sx + SimulationSettings.OUTTRACK_WIDTH / 2) * gameField.getWidth()) / SimulationSettings.OUTTRACK_WIDTH;
    }

    private int translatey(int sy) {
        return ((sy + SimulationSettings.OUTTRACK_HEIGHT / 2) * gameField.getHeight()) / SimulationSettings.OUTTRACK_HEIGHT;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
