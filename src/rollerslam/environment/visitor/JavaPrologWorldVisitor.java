package rollerslam.environment.visitor;

import rollerslam.environment.model.World;
import rollerslam.environment.model.visitor.Visitor;

import com.parctechnologies.eclipse.CompoundTerm;

public interface JavaPrologWorldVisitor extends Visitor {
	CompoundTerm getPrologRepresentation(World world);
}
