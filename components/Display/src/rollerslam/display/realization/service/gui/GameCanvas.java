/*
 */

package rollerslam.display.realization.service.gui;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.text.NumberFormat;

import javax.swing.JLabel;

import rollerslam.agent.communicative.specification.type.object.OOState;
import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.common.DomainSettings;
import rollerslam.common.datatype.PlayerTeam;
import rollerslam.common.objects.Ball;
import rollerslam.common.objects.Player;
import rollerslam.common.objects.oid.PlayerOID;
import rollerslam.common.objects.state.BallState;
import rollerslam.common.objects.state.PlayerState;
import rollerslam.display.realization.service.gui.mvc.Model;
import rollerslam.display.realization.service.gui.sprite.Sprite;
import rollerslam.display.realization.service.gui.sprite.SpriteKind;
import rollerslam.display.realization.service.gui.sprite.SpriteStore;

/**
 *
 * @author Weslei
 */
@SuppressWarnings(value = "serial")
public class GameCanvas extends Canvas implements MouseMotionListener {

    /** The stragey that allows us to use accelerate page flipping */
    private BufferStrategy strategy;

    private OOState world;

    private Model model;
    private SpriteStore ss;

    //private JLabel messagesLabel;

    private Sprite background;
    //private Sprite scoreBoardA;
    //private Sprite scoreBoardB;
    private GameField gameField;
    
    public Point mPoint;

    public GameCanvas(JLabel messages) {
        gameField = new GameField(4);

        setBounds(0, 0, gameField.getWidth(), gameField.getHeight());

        //this.messagesLabel = messages;

        // Tell AWT not to bother repainting our canvas since we're
        // going to do that our self in accelerated mode
        setIgnoreRepaint(true);
        ss = SpriteStore.get();

        background = new Sprite(gameField.getImage());
        //scoreBoardA = new Sprite(gameField.getScoreboardA());
        //scoreBoardB = new Sprite(gameField.getScoreboardB());
        
        this.addMouseMotionListener(this);
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
                NumberFormat nf = NumberFormat.getInstance();
                nf.setMaximumIntegerDigits(3);
                nf.setMinimumIntegerDigits(3);
                nf.setParseIntegerOnly(true);
                //Font f = new Font(null, Font.BOLD, (int) (scoreBoardB.getHeight() / 1.3));

                //String tmp;
                Graphics2D g;
                //String agId;
                //boolean isRed = true;
                int x;
                int y;

                while (true) {
                  //  agId = "";
                    g = (Graphics2D) strategy.getDrawGraphics();

                    g.setColor(Color.GREEN);
                    g.fillRect(0, 0, 800, 600);

                    background.draw(g, 0, 0);

                    world = model.getModel();
                    if (world != null) {
                        boolean freeBall = true;
                        
                        for (WorldObject object : world.objects.values()) {
                        	if (object instanceof Player) {
                        		Player player = (Player) object;
                        		freeBall &= !((PlayerState)player.state).withBall;
                        		
                        		if (!freeBall) {
                        			break;
                        		}
							}
						}

                        for (WorldObject object : world.objects.values()) {
                        	if (object instanceof Player) {
                        		PlayerState player = (PlayerState) object.state;
                                Sprite s = null;

                                if (((PlayerOID)object.oid).team == PlayerTeam.TEAM_A) {
                                    if (player.withBall) {
                                        s = ss.getSprite(SpriteKind.RED_PLAYER_WITH_BALL);
                                    } else if (player.inGround) {
                                        s = ss.getSprite(SpriteKind.RED_PLAYER_GROUNDED);
                                    } else {
                                        s = ss.getSprite(SpriteKind.RED_PLAYER);
                                    }                                	
                                } else {
                                    if (player.withBall) {
                                        s = ss.getSprite(SpriteKind.BLUE_PLAYER_WITH_BALL);
                                        freeBall = false;
                                    } else if (player.inGround) {
                                        s = ss.getSprite(SpriteKind.BLUE_PLAYER_GROUNDED);
                                    } else {
                                        s = ss.getSprite(SpriteKind.BLUE_PLAYER);
                                    }                                	
                                }
                                
                                x = translatex(player.s.x) - 15;
                                y = translatey(player.s.y) - 30;
                                if (mPoint != null && (mPoint.x >= x && mPoint.x <= (x + 15))
                                    && (mPoint.y >= y && mPoint.y <= (y + 30))) {
                                    // agId = "PId: " + object.oid;
                                    // isRed = true;
                                }
                                s.draw(g, x, y);
							} else if (object instanceof Ball) {
								BallState ball = (BallState) object.state;
		                        
								if (freeBall) {
		                            ss
											.getSprite(SpriteKind.BALL)
											.draw(
													g,
													translatex(ball.s.x) - 5,
													translatey(ball.s.y) - 5);
		                        }								
							}

						}                        

                    }
                    
/*
                    for (Message message : world.actions) {
                            if (message instanceof SendMsgAction) {
                                MessageHandler.scheduleForExhibition(((SendMsgAction) message).subject);
                            }
                        }

                        g.setColor(Color.RED);
                        g.fillRect(0, 0, scoreBoardA.getWidth(), scoreBoardA.getHeight());
                        g.setColor(Color.BLUE);
                        g.fillRect(scoreBoardB.getWidth(), 0, scoreBoardB.getWidth(), scoreBoardB.getHeight());
                        
                        tmp = nf.format(world.scoreboard.scoreTeamA);

                        g.setFont(f);
                        g.setColor(Color.WHITE);
                        g.drawString(tmp, 0, (int) (scoreBoardB.getHeight() / 1.3));

                        tmp = nf.format(world.scoreboard.scoreTeamB);

                        g.setColor(Color.WHITE);
                        g.drawString(tmp, scoreBoardB.getWidth(), (int) (scoreBoardB.getHeight() / 1.3));
                        
                        g.setColor(Color.BLACK);
                        g.drawString("Sim cycle: " + world.currentCycle, 5, 50);
                        
                        g.setColor(isRed ? Color.RED : Color.BLUE);
                        g.drawString(agId, 5, 70);
                        
                        

                        messagesLabel.setText(MessageHandler.getCurrentMessage()); 
                    }
*/
                    
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
        return ((sx + DomainSettings.OUTTRACK_WIDTH / 2) * gameField.getWidth()) / DomainSettings.OUTTRACK_WIDTH;
    }

    private int translatey(int sy) {
        return ((sy + DomainSettings.OUTTRACK_HEIGHT / 2) * gameField.getHeight()) / DomainSettings.OUTTRACK_HEIGHT;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public void mouseMoved(MouseEvent e) {
        this.mPoint = e.getPoint();
    }

    public void mouseDragged(MouseEvent e) {}
    
}
