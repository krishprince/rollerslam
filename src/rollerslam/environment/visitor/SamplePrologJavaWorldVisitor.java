package rollerslam.environment.visitor;

import java.util.Collection;

import rollerslam.environment.model.AnimatedObject;
import rollerslam.environment.model.Ball;
import rollerslam.environment.model.Basket;
import rollerslam.environment.model.Goal;
import rollerslam.environment.model.OutTrack;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.Ramp;
import rollerslam.environment.model.Scoreboard;
import rollerslam.environment.model.Trampoline;
import rollerslam.environment.model.World;
import rollerslam.environment.model.WorldObject;
import rollerslam.environment.model.utils.Vector;

import com.parctechnologies.eclipse.CompoundTerm;
import com.sun.org.apache.bcel.internal.classfile.Code;
import com.sun.org.apache.bcel.internal.classfile.CodeException;
import com.sun.org.apache.bcel.internal.classfile.ConstantClass;
import com.sun.org.apache.bcel.internal.classfile.ConstantDouble;
import com.sun.org.apache.bcel.internal.classfile.ConstantFieldref;
import com.sun.org.apache.bcel.internal.classfile.ConstantFloat;
import com.sun.org.apache.bcel.internal.classfile.ConstantInteger;
import com.sun.org.apache.bcel.internal.classfile.ConstantInterfaceMethodref;
import com.sun.org.apache.bcel.internal.classfile.ConstantLong;
import com.sun.org.apache.bcel.internal.classfile.ConstantMethodref;
import com.sun.org.apache.bcel.internal.classfile.ConstantNameAndType;
import com.sun.org.apache.bcel.internal.classfile.ConstantPool;
import com.sun.org.apache.bcel.internal.classfile.ConstantString;
import com.sun.org.apache.bcel.internal.classfile.ConstantUtf8;
import com.sun.org.apache.bcel.internal.classfile.ConstantValue;
import com.sun.org.apache.bcel.internal.classfile.Deprecated;
import com.sun.org.apache.bcel.internal.classfile.ExceptionTable;
import com.sun.org.apache.bcel.internal.classfile.Field;
import com.sun.org.apache.bcel.internal.classfile.InnerClass;
import com.sun.org.apache.bcel.internal.classfile.InnerClasses;
import com.sun.org.apache.bcel.internal.classfile.JavaClass;
import com.sun.org.apache.bcel.internal.classfile.LineNumber;
import com.sun.org.apache.bcel.internal.classfile.LineNumberTable;
import com.sun.org.apache.bcel.internal.classfile.LocalVariable;
import com.sun.org.apache.bcel.internal.classfile.LocalVariableTable;
import com.sun.org.apache.bcel.internal.classfile.Method;
import com.sun.org.apache.bcel.internal.classfile.Signature;
import com.sun.org.apache.bcel.internal.classfile.SourceFile;
import com.sun.org.apache.bcel.internal.classfile.StackMap;
import com.sun.org.apache.bcel.internal.classfile.StackMapEntry;
import com.sun.org.apache.bcel.internal.classfile.Synthetic;
import com.sun.org.apache.bcel.internal.classfile.Unknown;

public class SamplePrologJavaWorldVisitor implements PrologJavaWorldVisitor {

	@SuppressWarnings("unchecked")
	public void updateWorldRepresentation(World world, Object worldState) {
		// System.out.println("obj: " + worldState);

		// if it's not there anymore it should be false...
		world.ball.withPlayer = false;

		if (worldState instanceof Collection) {
			Collection stateCol = (Collection) worldState;

			for (Object object : stateCol) {
				if (object instanceof CompoundTerm) {
					CompoundTerm term = (CompoundTerm) object;

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

	private Vector termToVector(CompoundTerm compoundTerm) {
		return new Vector((Integer) compoundTerm.arg(1), (Integer) compoundTerm
				.arg(2));
	}

	public void updateWorldRepresentation(World world, String worldState) {
		// TODO Auto-generated method stub

	}

	public void visit(World obj) {
		// TODO Auto-generated method stub

	}

	public void visit(WorldObject obj) {
		// TODO Auto-generated method stub

	}

	public void visit(AnimatedObject obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Ball obj) {
		// TODO Auto-generated method stub

	}

	public void visit(OutTrack obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Player obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Basket obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Goal obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Ramp obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Trampoline obj) {
		// TODO Auto-generated method stub

	}

	public void visit(Scoreboard obj) {
		// TODO Auto-generated method stub

	}

	public void visitCode(Code arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitCodeException(CodeException arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantClass(ConstantClass arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantDouble(ConstantDouble arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantFieldref(ConstantFieldref arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantFloat(ConstantFloat arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantInteger(ConstantInteger arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantInterfaceMethodref(ConstantInterfaceMethodref arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantLong(ConstantLong arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantMethodref(ConstantMethodref arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantNameAndType(ConstantNameAndType arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantPool(ConstantPool arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantString(ConstantString arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantUtf8(ConstantUtf8 arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitConstantValue(ConstantValue arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitDeprecated(Deprecated arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitExceptionTable(ExceptionTable arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitField(Field arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitInnerClass(InnerClass arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitInnerClasses(InnerClasses arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitJavaClass(JavaClass arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitLineNumber(LineNumber arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitLineNumberTable(LineNumberTable arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitLocalVariable(LocalVariable arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitLocalVariableTable(LocalVariableTable arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitMethod(Method arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitSignature(Signature arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitSourceFile(SourceFile arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitStackMap(StackMap arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitStackMapEntry(StackMapEntry arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitSynthetic(Synthetic arg0) {
		// TODO Auto-generated method stub
		
	}

	public void visitUnknown(Unknown arg0) {
		// TODO Auto-generated method stub
		
	}
}
