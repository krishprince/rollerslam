package rollerslam.environment;

import java.util.Collection;
import java.util.Vector;

import rollerslam.environment.model.World;
import rollerslam.environment.visitor.JavaPrologWorldVisitor;
import rollerslam.environment.visitor.PrologJavaWorldVisitor;
import rollerslam.environment.visitor.SampleJavaPrologWorldVisitor;
import rollerslam.environment.visitor.SamplePrologJavaWorldVisitor;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;

public class EnvironmentWorldModel extends EnvironmentStateModel {
	private World world;
	private Collection fluents = new Vector();
	private JavaPrologWorldVisitor javaPrologVisitor = new SampleJavaPrologWorldVisitor();
	private PrologJavaWorldVisitor prologJavaVisitor = new SamplePrologJavaWorldVisitor();
	
	public EnvironmentWorldModel() {
		setWorld(new World());
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setWorld(World world) {
		this.world = world;
		updateFluents();		
	}

	public Collection getFluents() {
		return fluents;
	}
	
	public void updateFluents() {
		fluents = javaPrologVisitor.getPrologRepresentation(world);		
	}

	public void updateWorld() {
		prologJavaVisitor.updateWorldRepresentation(world, fluents);		
	}
	
	public void setFluents(Collection fluents) {
		this.fluents = fluents;
	}
}
