:- ['flux'].

:- dynamic goal/1.
:- dynamic action/1.

%% initial GOAL

goal(runToBall).

%% initial ACTION

action(dash(10)).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

state_update(Z1, interpretEnvModel, Z1, _).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

state_update(Z1, updateGoal, Z2, _) :-
		player(PX),
		
		holds(position(ball, vector(Sxa, Sya)), Z1),
		holds(position(player(PX), vector(Sxb, Syb)), Z1),
		
	   calcDistance(Sxa, Sya, Sxb, Syb, Sdist),
		
		Sdist < 20,
		
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
		
		holds(position(ball, vector(Bx, By)), Z1),
					 	
		Bxm is 0 - Bx,
		Bym is 0 - By,
					 	
	 	update(Z1, [action(kick(vector(Bxm, Bym)))], [], Z2). 
	 	
state_update(Z1, computeNextAction, Z1, _).	 	

