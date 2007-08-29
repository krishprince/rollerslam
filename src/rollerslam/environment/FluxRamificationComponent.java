package rollerslam.environment;

import java.io.IOException;

import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.RamificationComponent;

import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.EclipseConnection;
import com.parctechnologies.eclipse.EclipseException;

public class FluxRamificationComponent implements RamificationComponent {

	private EclipseConnection eclipse;
	
	public FluxRamificationComponent(EclipseConnection eclipse) {
		this.eclipse = eclipse;
	}
	
	public void processRamifications(EnvironmentStateModel world) {
	    try {
			CompoundTerm result = eclipse.rpc("runRamifications(X)");
		} catch (EclipseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
