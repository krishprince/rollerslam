package rollerslam.environment;

import rollerslam.environment.model.World;
import rollerslam.environment.visitor.JavaPrologWorldVisitor;
import rollerslam.environment.visitor.PrologJavaWorldVisitor;
import rollerslam.environment.visitor.SampleJavaPrologWorldVisitor;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.RamificationComponent;

import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.EclipseConnection;

public class FluxRamificationComponent implements RamificationComponent {

	private EclipseConnection eclipse;
	private JavaPrologWorldVisitor javaPrologVisitor;
	private PrologJavaWorldVisitor prologJavaVisitor;
	
	public FluxRamificationComponent(EclipseConnection eclipse) {
		this.eclipse = eclipse;
		this.javaPrologVisitor = new SampleJavaPrologWorldVisitor();
		this.prologJavaVisitor = new SamplePrologJavaWorldVisitor();
	}
	
	public void processRamifications(EnvironmentStateModel world) {
		String query = "processRamifications(["+javaPrologVisitor.getPrologRepresentation((World) world)+"], R)";

//		System.out.println("query: "+query);
		
		CompoundTerm ret;
		try {
			ret = eclipse.rpc(query);
			
//			System.out.println("result: "+ret);
			
			System.out.println(ret.arg(1).equals(ret.arg(2)));
			prologJavaVisitor.updateWorldRepresentation((World)world, ret.arg(2));
			
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

}
