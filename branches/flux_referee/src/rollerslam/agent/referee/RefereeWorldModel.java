package rollerslam.agent.referee;

import rollerslam.environment.model.actions.UpdateScoreAction;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;

public class RefereeWorldModel extends EnvironmentStateModel {
	private EnvironmentStateModel environmentStateModel;
	private UpdateScoreAction message;

	public RefereeWorldModel(EnvironmentStateModel environmentStateModel) {		
		this.environmentStateModel = environmentStateModel;		
	}

	public EnvironmentStateModel getEnvironmentStateModel() {
		return environmentStateModel;
	}

	public void setEnvironmentStateModel(EnvironmentStateModel environmentStateModel) {
		this.environmentStateModel = environmentStateModel;
	}
	
	public UpdateScoreAction getMessage() {
		
		return message;
	}

	public void setMessage(UpdateScoreAction message) {
		this.message = message;
	}

}
