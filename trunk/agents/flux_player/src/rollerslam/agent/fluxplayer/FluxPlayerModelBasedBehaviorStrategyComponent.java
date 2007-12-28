package rollerslam.agent.fluxplayer;

import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.leg.DashAction;
import rollerslam.environment.model.actions.leg.KickAction;
import rollerslam.environment.model.utils.Vector;
import rollerslam.environment.visitor.JavaPrologWorldVisitor;
import rollerslam.environment.visitor.SampleJavaPrologWorldVisitor;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;

import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.CompoundTermImpl;
import com.parctechnologies.eclipse.EclipseConnection;

public class FluxPlayerModelBasedBehaviorStrategyComponent implements
		ModelBasedBehaviorStrategyComponent {

	private EclipseConnection eclipse;
	private JavaPrologWorldVisitor visitor = new SampleJavaPrologWorldVisitor();
	
	public FluxPlayerModelBasedBehaviorStrategyComponent(
			EclipseConnection eclipse) {
		this.eclipse = eclipse;
	}

	public Message computeAction(EnvironmentStateModel w) {
		if (w instanceof FluxPlayerWorldModel) {
			World world = (World) ((FluxPlayerWorldModel) w).environmentStateModel;
			
			if (world != null) {
				CompoundTerm query = new CompoundTermImpl("processAgentCycle",
						((FluxPlayerWorldModel) w).myID,
						((FluxPlayerWorldModel) w).myTeam.toString(),
						visitor.getPrologRepresentation(world), null);

				CompoundTerm ret;

				try {
					ret = eclipse.rpc(query);
					return actionTerm2Object(world, (CompoundTerm) ret.arg(4));

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}			
		return null;		
	}

	private Message actionTerm2Object(World world, CompoundTerm arg) {
		Message ret = null;
		
		if (arg.functor().equals("dash")) {
			ret = new DashAction(termToVector((CompoundTerm)arg.arg(1)));
		} else if (arg.functor().equals("kick")) {
			ret = new KickAction(termToVector((CompoundTerm)arg.arg(1)));
		}
		return ret;
	}

	private Vector termToVector(CompoundTerm compoundTerm) {

		return new Vector((int) Double.parseDouble(compoundTerm.arg(1)
				.toString()), ((int) Double.parseDouble(compoundTerm.arg(2)
				.toString())));
	}	
}
