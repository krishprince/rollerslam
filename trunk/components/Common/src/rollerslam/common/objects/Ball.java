package rollerslam.common.objects;

import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.common.objects.oid.BallOID;
import rollerslam.common.objects.state.BallState;

public class Ball extends WorldObject {

	public Ball(BallOID oid, BallState state) {
		super(oid, state);
	}

}
