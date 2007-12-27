:- ['flux'].

:- dynamic goal/1.
:- dynamic action/1.

goal(runToBall).
action(dash(10)).

perform(_, _).
poss(_,_).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

state_update(Z1, interpretEnvModel, Z1, _).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

state_update(Z1, updateGoal, Z2, _) :-
		player(PX),
		
		holds(position(ball, vector(Sxa, Sya)), Z1),
		holds(position(player(PX), vector(Sxb, Syb)), Z1),
		
	   calcDistance(Sxa, Sya, Sxb, Syb, Sdist),
		
		Sdist < 10,
		
		update(Z1, [goal(kickBall)], [goal(runToBall)], Z2).

state_update(Z1, updateGoal, Z1, _).	   

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

state_update(Z1, computeNextAction, Z2, _) :- 
	 	holds(goal(runToBall), Z1),
	 	
		player(PX),
		
		holds(position(ball, Sball), Z1),
		holds(position(player(PX), Splayer), Z1),
		
		subtractVector(Sball, Splayer, Acc),

	 	update(Z1, [action(dash(Acc))], [], Z2). 

state_update(Z1, computeNextAction, Z2, _) :- 
	 	holds(goal(kickBall), Z1),

		player(PX),
		
		holds(position(ball, Sball), Z1),
		holds(position(player(PX), Splayer), Z1),
		
		subtractVector(Sbal, Splayerl, Acc),
			 	
	 	update(Z1, [action(kick(Acc))], [], Z2). 
	 	
state_update(Z1, computeNextAction, Z1, _).	 	

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

 init(PlayerID, PlayerTeam) :- assert(player(PlayerID)), assert(team(PlayerTeam)). 

processAgentCycle(WorldState, Action) :- 
	goal(OldGoal), action(OldAction),
	Z = [goal(OldGoal), action(OldAction) | WorldState], 
	
	state_update(Z, interpretEnvModel, Z1, []),
    state_update(Z1, updateGoal, Z2, []),
    state_update(Z2, computeNextAction, Z3, []),
    
    holds(goal(NewGoal), Z3),
    holds(action(NewAction), Z3),
    
	retract(goal(_)),
	retract(action(_)),
	
	assert(goal(NewGoal)),
	assert(action(NewAction)),
	
	Action = NewAction.
	