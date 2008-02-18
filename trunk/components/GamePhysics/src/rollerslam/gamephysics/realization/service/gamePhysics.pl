
gamePhysicsUpdateModel(Z0, Z0).

gamePhysicsProcessAction(Z0, dash(P, Dir), Z1) :-
	holds(P@[acceleration->Acc], Z0),
	update(Z0, [P@[acceleration->Dir]], [P@[acceleration->Acc]], Z1).

gamePhysicsComputeNextAction(_,_) :- fail.