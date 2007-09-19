package rollerslam.agent.player;

public enum AgentGoalLogMessages {
	NEXT_GOAL("NEXT GOAL => "),
	LAST_GOAL("LAST GOAL => "),

	WAIT_JOIN_GAME("AGENT WAITS TO JOIN GAME"),
	INITIALIZATION("INITIALIZATION"),
	SET_ROLES("SET ROLES"),
	GO_TO_INIT_COORD("GO TO INITIAL COORDINARTES"),
	STAND_UP("STAND UP"),
	GO_TO_GOAL("GO TO GOAL"),
	GO_TO_BALL("GO TO BALL"),
	STOP("STOP"),
	WAIT_MOVIMENT("WAIT MOVIMENT"),
	CATCH_BALL("CATCH BALL"),
	TACKLE_PLAYER("TACKLE PLAYER WITH BALL"),
	COUNTER_TACKLE("COUNTER TACKLE"),
	KICK_BALL("KICK BALL"),
	THROW_BALL("THROW BALL"),
	
	ROLES_RECEIVED("ROLES RECEIVED => POSITION - %POS%; ROLE - %ROLE%"),
	NOT_RECEIVE_COACH_MESG("DID NOT RECEIVE COACH MESSAGE"),
	IN_GROUND("AGENT GROUNDED"),
	HAS_BALL("AGENT HAS THE BALL"),
	NEAR_BALL("AGENT CLOSE TO THE BALL"),
	IN_AREA("AGENT IN HIS AREA"),
	NOT_IN_GROUND("AGENT NOT GROUNDED"),
	FAR_BALL("AGENT FAR FROM THE BALL"),
	VERY_NEAR_BALL("AGENT VERY CLOSE TO THE BALL"),
	BALL_WITH_NOBODY("NOBODY HAS THE BALL"),
	BALL_WITH_ANYBODY("ANYBODY HAS THE BALL"),
	BALL_WITH_TEAM("BALL WITH TEAMMATE"),
	BALL_WITH_OPPONENT("BALL WITH OPPONENT TEAM"),
	NEAR_OPPONENT("AGENT CLOSE TO THE OPPONENT"),
	FAR_OPPONENT("AGENT FAR FROM OPPONENT"),
	DOESNT_HAVE_BALL("AGENT HASN'T THE BALL"),
	BALL_WITHOUT_TEAM("BALL WITHOUT AGENT TEAM"),
	MANY_OPPONENTS_NEAR("MANY OPPONENTS AGENTS CLOSE TO THE AGENT"),
	NEAR_GOAL("AGENT CLOSE TO THE GOAL");
	

    private final String value;
    AgentGoalLogMessages(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
