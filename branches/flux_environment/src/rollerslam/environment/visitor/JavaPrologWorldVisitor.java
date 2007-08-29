package rollerslam.environment.visitor;

import rollerslam.environment.model.World;
import rollerslam.environment.model.visitor.Visitor;

public interface JavaPrologWorldVisitor extends Visitor {
	String getPrologRepresentation(World world);
}
