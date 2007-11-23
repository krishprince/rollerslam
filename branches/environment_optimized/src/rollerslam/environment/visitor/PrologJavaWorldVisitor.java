package rollerslam.environment.visitor;

import rollerslam.environment.model.World;

public interface PrologJavaWorldVisitor {
	void updateWorldRepresentation(World world, Object worldState);
}
