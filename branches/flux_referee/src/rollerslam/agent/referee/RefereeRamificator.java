package rollerslam.agent.referee;

import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.EclipseConnection;

import rollerslam.environment.model.World;
import rollerslam.environment.visitor.JavaPrologWorldVisitor;
import rollerslam.environment.visitor.PrologJavaWorldVisitor;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.RamificationComponent;

public class RefereeRamificator  implements RamificationComponent {
	private EclipseConnection eclipse;
	private JavaPrologWorldVisitor javaPrologVisitor;
	private RefereePrologJavaWorldVisitor prologJavaVisitor;
	
	public RefereeRamificator(EclipseConnection eclipse) {
		this.eclipse = eclipse;
		this.javaPrologVisitor = new RefereeJavaPrologWorldVisitor();
		this.prologJavaVisitor = new RefereePrologJavaWorldVisitor();
	}
	
	public void processRamifications(EnvironmentStateModel refereeWorld) {
		EnvironmentStateModel world = ((RefereeWorldModel) refereeWorld).getEnvironmentStateModel();
		if(world != null) {
			String query = "processRamifications(["
				+ javaPrologVisitor.getPrologRepresentation((World) world)
				+ "], Z)";
			
			CompoundTerm ret;
			try {
				ret = eclipse.rpc(query);
				//System.out.println("query: "+ query);
				System.out.println("result: "+ ret.arg(2));
				
				prologJavaVisitor.updateWorldRepresentation((RefereeWorldModel) refereeWorld, ret.arg(2));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
