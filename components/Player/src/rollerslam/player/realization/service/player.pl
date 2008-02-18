
playerUpdateModel([], Z) :- 
	update([], [me@[senseCycle->true]], [], Z).
	
playerUpdateModel(Z0, Z1) :- 
	holds(me@[senseCycle->true], Z0),
	update(Z0, [me@[senseCycle->false]], [me@[senseCycle->true]], Z1).

playerUpdateModel(Z0, Z1) :- 
	holds(me@[senseCycle->false], Z0),
	update(Z0, [me@[senseCycle->true]], [me@[senseCycle->false]], Z1).

playerComputeNextAction(Z , ask(ball)).
	
playerComputeNextAction(Z , dash(vector(100, 100))) :-
	holds(me@[senseCycle->false], Z).

playerProcessAction(_0, _, _) :- fail.
	