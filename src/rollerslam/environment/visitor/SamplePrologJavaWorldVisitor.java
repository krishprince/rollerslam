package rollerslam.environment.visitor;

import java.util.Collection;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.World;
import rollerslam.environment.model.utils.Vector;

import com.parctechnologies.eclipse.CompoundTerm;

public class SamplePrologJavaWorldVisitor implements PrologJavaWorldVisitor {

	@SuppressWarnings("unchecked")
	public void updateWorldRepresentation(World world, Object worldState) {
		//System.out.println("obj: " + worldState);

		// if it's not there anymore it should be false...
		world.ball.withPlayer = false;
		world.ball.isMoving = false;
		setPropertiesPlayer(world);

		if (worldState instanceof Collection) {
			Collection stateCol = (Collection) worldState;

			for (Object object : stateCol) {
				if (object instanceof CompoundTerm) {
					CompoundTerm term = (CompoundTerm) object;
					// System.out.print("a: " + term.toString() + "\n ");
					if (term.functor().equals("position")) {
						if (((CompoundTerm) term.arg(1)).functor().equals(
								"ball")) {
							world.ball.s = termToVector(((CompoundTerm) term
									.arg(2)));
						} else if (((CompoundTerm) term.arg(1)).functor()
								.equals("player")) {
							int id = (Integer) ((CompoundTerm) term.arg(1))
									.arg(1);
							Player p = getPlayerFromID(world, id);
							p.s = termToVector(((CompoundTerm) term.arg(2)));
						}
					} else if (term.functor().equals("acceleration")) {
						if (((CompoundTerm) term.arg(1)).functor().equals(
								"ball")) {
							world.ball.a = termToVector(((CompoundTerm) term
									.arg(2)));
						} else if (((CompoundTerm) term.arg(1)).functor()
								.equals("player")) {
							int id = (Integer) ((CompoundTerm) term.arg(1))
									.arg(1);
							Player p = getPlayerFromID(world, id);
							p.a = termToVector(((CompoundTerm) term.arg(2)));
						}
					} else if (term.functor().equals("speed")) {
						if (((CompoundTerm) term.arg(1)).functor().equals(
								"ball")) {
							world.ball.v = termToVector(((CompoundTerm) term
									.arg(2)));
						} else if (((CompoundTerm) term.arg(1)).functor()
								.equals("player")) {
							int id = (Integer) ((CompoundTerm) term.arg(1))
									.arg(1);
							Player p = getPlayerFromID(world, id);
							p.v = termToVector(((CompoundTerm) term.arg(2)));
						}
					} else if (term.functor().equals("withPlayer")) {
						if (((CompoundTerm) term.arg(1)).functor().equals(
								"ball")) {
							world.ball.withPlayer = true;
						}
					} else if (term.functor().equals("hasBall")) {
						if (((CompoundTerm) term.arg(1)).functor().equals(
								"player")) {
							int id = (Integer) ((CompoundTerm) term.arg(1))
									.arg(1);
							Player p = getPlayerFromID(world, id);
							p.hasBall = true;
						}
					} else if (term.functor().equals("inGround")) {
						if (((CompoundTerm) term.arg(1)).functor().equals(
								"player")) {
							int id = (Integer) ((CompoundTerm) term.arg(1))
									.arg(1);
							Player p = getPlayerFromID(world, id);
							p.inGround = true;
						}
					} else if (term.functor().equals("counterTackle")) {
						if (((CompoundTerm) term.arg(1)).functor().equals(
								"player")) {
							int id = (Integer) ((CompoundTerm) term.arg(1))
									.arg(1);
							Player p = getPlayerFromID(world, id);
							p.counterTackle = true;
						}
					} else if (term.functor().equals("isMoving")) {
						if (((CompoundTerm) term.arg(1)).functor().equals(
								"ball")) {
							world.ball.isMoving = true;
						}
					}
				}
			}
		}
	}

	private Player getPlayerFromID(World world, int id) {
		for (Player player : world.playersA) {
			if (player.id == id) {
				return player;
			}
		}

		for (Player player : world.playersB) {
			if (player.id == id) {
				return player;
			}
		}
		return null;
	}

	private void setPropertiesPlayer(World world) {
		for (Player player : world.playersA) {
			player.counterTackle = false;
			player.hasBall = false;
			player.inGround = false;
			player.counterTackle = false;
		}
		for (Player player : world.playersB) {
			player.counterTackle = false;
			player.hasBall = false;
			player.inGround = false;
			player.counterTackle = false;
		}
	}

	private Vector termToVector(CompoundTerm compoundTerm) {

		return new Vector((int) Double.parseDouble(compoundTerm.arg(1)
				.toString()), ((int) Double.parseDouble(compoundTerm.arg(2)
				.toString())));
	}

}
