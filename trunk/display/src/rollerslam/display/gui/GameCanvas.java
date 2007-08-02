/*
 */

package rollerslam.display.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import rollerslam.display.gui.mvc.Model;
import rollerslam.display.gui.sprite.Sprite;
import rollerslam.display.gui.sprite.SpriteStore;
import rollerslam.environment.model.OutTrack;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.World;

/**
 *
 * @author Weslei
 */
@SuppressWarnings(value = "serial")
public class GameCanvas extends Canvas {

    /** The stragey that allows us to use accelerate page flipping */
    private BufferStrategy strategy;

    private World world;
    private Sprite background;
    private Sprite pwb;
    private Sprite pnb;
    private Sprite b;
    private Model model;

    public GameCanvas() {
        setBounds(0, 0, 800, 600);

        // Tell AWT not to bother repainting our canvas since we're
        // going to do that our self in accelerated mode
        setIgnoreRepaint(true);

        background = SpriteStore.get().getSprite("rollerslam/display/gui/resources/field.png");
        pwb = SpriteStore.get().getSprite("rollerslam/display/gui/resources/pwb.png");
        pnb = SpriteStore.get().getSprite("rollerslam/display/gui/resources/pnb.png");
        b = SpriteStore.get().getSprite("rollerslam/display/gui/resources/ball.png");
    }

    public void init() {
        // create the buffering strategy which will allow AWT
        // to manage our accelerated graphics
        createBufferStrategy(2);

        strategy = getBufferStrategy();

        new Thread() {

            public void run() {
                while (true) {

                    Graphics2D g = (Graphics2D) strategy.getDrawGraphics();

                    g.setColor(Color.GREEN);
                    g.fillRect(0, 0, 800, 600);
                    background.draw(g, 0, 0);

                    world = model.getModel();
                    if (world != null) {
                        for (Player player : world.playersA) {
                            pwb.draw(g, translatex(player.sx), translatey(player.sy));
                        }

                        for (Player player : world.playersB) {
                            pnb.draw(g, translatex(player.sx), translatey(player.sy));
                        }

                        b.draw(g, translatex(world.ball.sx), translatey(world.ball.sy));
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
        return ((sx + OutTrack.WIDTH / 2) * 800) / OutTrack.WIDTH;
    }

    private int translatey(int sy) {
        return ((sy + OutTrack.HEIGHT / 2) * 600) / OutTrack.HEIGHT;
    }

    public void setModel(Model model) {
        this.model = model;
    }
}
