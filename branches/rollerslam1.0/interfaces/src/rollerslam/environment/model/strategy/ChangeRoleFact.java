package rollerslam.environment.model.strategy;

import java.io.Serializable;

import rollerslam.environment.model.utils.Vector;

@SuppressWarnings("serial")
public class ChangeRoleFact implements Serializable {
	public Vector posCoord = null;
	
	public String toString() {
		return "Your new coord is " + posCoord;
	}
}
