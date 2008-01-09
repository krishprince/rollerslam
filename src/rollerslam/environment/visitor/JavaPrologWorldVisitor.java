package rollerslam.environment.visitor;

import java.util.Collection;

import rollerslam.environment.model.World;
import rollerslam.environment.model.visitor.Visitor;

public interface JavaPrologWorldVisitor extends Visitor {
	Collection getPrologRepresentation(World world);
}
