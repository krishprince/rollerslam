package rollerslam.environment.visitor;

import rollerslam.environment.model.World;
import rollerslam.environment.model.visitor.Visitor;

import com.parctechnologies.eclipse.CompoundTerm;

public interface PrologJavaWorldVisitor extends Visitor {
	void updateWorldRepresentation(World world, CompoundTerm worldState);
}
