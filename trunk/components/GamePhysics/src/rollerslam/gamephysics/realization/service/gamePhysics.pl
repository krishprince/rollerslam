
gamePhysicsInitializeModel([PlayersPerTeam], 
	[
	
		me@[playersPerTeam->PlayersPerTeam],

		pa1@[position->vector(0,0)],
		pa1@[acceleration->vector(0,0)]	
	
	]).

gamePhysicsUpdateModel(Z0, Z0).

gamePhysicsProcessAction(Z0, dash(P, Dir), Z1) :-
	holds(P@[acceleration->Acc], Z0),
	update(Z0, [P@[acceleration->Dir]], [P@[acceleration->Acc]], Z1).
	
gamePhysicsComputeNextAction(_, noAction).