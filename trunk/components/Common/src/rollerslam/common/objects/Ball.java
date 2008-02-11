package rollerslam.common.objects;

import rollerslam.agent.communicative.specification.type.fluent.FluentObject;
import rollerslam.common.objects.oid.BallOID;
import rollerslam.common.objects.state.BallState;

public class Ball extends FluentObject {

	public Ball(BallOID oid, BallState state) {
		super(oid, state);
	}

}
