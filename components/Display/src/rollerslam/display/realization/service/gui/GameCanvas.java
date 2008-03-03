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
import java.util.Iterator;
import java.util.Set;

import javax.swing.JLabel;

import rollerslam.agent.communicative.specification.type.object.OOState;
import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.display.realization.service.gui.mvc.Model;
import rollerslam.display.realization.service.gui.sprite.Sprite;
import rollerslam.display.realization.service.gui.sprite.SpriteKind;
import rollerslam.display.realization.service.gui.sprite.SpriteStore;
import rollerslam.fluxcommunicativeagent.realization.type.FluxOID;
import rollerslam.fluxcommunicativeagent.realization.type.FluxOOState;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluent;
import rollerslam.fluxinferenceengine.specification.type.Fluent;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTerm;

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

	// private JLabel messagesLabel;

	private Sprite background;
	// private Sprite scoreBoardA;
	// private Sprite scoreBoardB;
	private GameField gameField;

	public Point mPoint;

	public static final int OUTTRACK_HEIGHT = 138000;
	public static final int OUTTRACK_WIDTH  = 188000;

	public GameCanvas(JLabel messages) {
		gameField = new GameField(4);

		setBounds(0, 0, gameField.getWidth(), gameField.getHeight());

		// this.messagesLabel = messages;

		// Tell AWT not to bother repainting our canvas since we're
		// going to do that our self in accelerated mode
		setIgnoreRepaint(true);
		ss = SpriteStore.get();

		background = new Sprite(gameField.getImage());
		// scoreBoardA = new Sprite(gameField.getScoreboardA());
		// scoreBoardB = new Sprite(gameField.getScoreboardB());

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
				Printer printer = new Printer(GameCanvas.this.gameField);
				Graphics2D g;

				while (true) {
					g = (Graphics2D) strategy.getDrawGraphics();

					g.setColor(Color.GREEN);
					g.fillRect(0, 0, 800, 600);

					background.draw(g, 0, 0);

					world = model.getModel();
					if (world != null) {

						for (WorldObject object : world.objects.values()) {

							printer.print(g, object);

						}

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

	public void setModel(Model model) {
		this.model = model;
	}

	public void mouseMoved(MouseEvent e) {
		this.mPoint = e.getPoint();
	}

	public void mouseDragged(MouseEvent e) {
	}

	private class Printer {

		private GameField gameField;

		Printer(GameField gameField) {
			this.gameField = gameField;
		}

		public void print(Graphics2D graphics2D, WorldObject worldObject) {
			String attribute = "position";
			Object value = attributeDiscoverer(worldObject, attribute);
			if (value instanceof CompoundTerm) {
				CompoundTerm compoundTerm = (CompoundTerm) value;
				Integer x = (Integer) compoundTerm.arg(1);
				Integer y = (Integer) compoundTerm.arg(2);
				final SpriteKind sprite = getSprite(worldObject);
				ss.getSprite(sprite).draw(graphics2D,
						translatex(x) - 5, translatey(y) - 5);
			}
		}

		private SpriteKind getSprite(WorldObject worldObject) {
			SpriteKind retorno = null;
			FluxOID fluxOID = (FluxOID)worldObject.oid;
			if("ball".equals(fluxOID.getTerm().functor())){
				retorno = SpriteKind.BALL;
			} else {
				CompoundTerm compoundTerm = (CompoundTerm) fluxOID.getTerm();
				String team = (String)compoundTerm.arg(2);
				if("TEAM_A".equalsIgnoreCase(team)){
					retorno = SpriteKind.RED_PLAYER;
				} else {
					retorno = SpriteKind.BLUE_PLAYER;
				}
			}

			return retorno;
		}

		private Object attributeDiscoverer(WorldObject worldObject,
				String attribute) {
			Object retorno = null;
			if (worldObject.state instanceof FluxOOState) {
				FluxOOState fluxOOState = (FluxOOState) worldObject.state;
				boolean teste = true;
				Set<Fluent> fluents = fluxOOState.getFluents();
				Iterator<Fluent> iterator = fluents.iterator();
				while (teste && iterator.hasNext()) {
					Fluent fluent = iterator.next();
					EclipsePrologFluent eclipsePrologFluent = (EclipsePrologFluent) fluent;
					CompoundTerm compoundTerm = (CompoundTerm) eclipsePrologFluent.getTerm()
							.arg(2);
					Atom atom = (Atom) compoundTerm.arg(1);
					if (attribute.equals(atom.functor())) {
						teste = false;
						retorno = compoundTerm.arg(2);
					}
				}
			}
			return retorno;
		}

		private int translatex(int sx) {
			return ((sx + GameCanvas.OUTTRACK_WIDTH / 2) * gameField
					.getWidth())
					/ GameCanvas.OUTTRACK_WIDTH;
		}

		private int translatey(int sy) {
			return ((sy + GameCanvas.OUTTRACK_HEIGHT / 2) * gameField
					.getHeight())
					/ GameCanvas.OUTTRACK_HEIGHT;
		}

	}

}
