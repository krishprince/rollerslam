package rollerslam.agent.referee;

import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.EclipseConnection;

import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.RamificationComponent;

public class RefereeRamificator  implements RamificationComponent {
	private EclipseConnection eclipse;
	private RefereeJavaPrologWorldVisitor javaPrologVisitor;
	private RefereePrologJavaWorldVisitor prologJavaVisitor;
	
	public RefereeRamificator(EclipseConnection eclipse) {
		this.eclipse = eclipse;
		this.javaPrologVisitor = new RefereeJavaPrologWorldVisitor();
		this.prologJavaVisitor = new RefereePrologJavaWorldVisitor();
	}
	
	public void processRamifications(EnvironmentStateModel refereeWorld) {
		EnvironmentStateModel world = ((RefereeWorldModel) refereeWorld).getEnvironmentStateModel();
		if(world != null) {
			String query = "processRamificationsReferee(["
				+ javaPrologVisitor.getPrologRepresentation((World) world)
				+ "], R)";
			
			CompoundTerm ret;
			try {
				System.out.println("query: "+ query);
				ret = eclipse.rpc(query);
				
				prologJavaVisitor.updateWorldRepresentation((RefereeWorldModel) refereeWorld, ret.arg(2));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
