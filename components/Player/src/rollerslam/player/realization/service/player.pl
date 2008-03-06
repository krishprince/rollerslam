
playerInitializeModel([PlayerID, Team, Oid], 
	[me@[id->PlayerID], 
	 me@[team->Team], 
	 me@[senseCycle->true],
	 me@[oid->OidAtom]]) :-

	 term_string(OidAtom, Oid).
	
playerUpdateModel(Z0, Z1) :- 
	holds(me@[senseCycle->true], Z0),
	update(Z0, [me@[senseCycle->false]], [me@[senseCycle->true]], Z1).

playerUpdateModel(Z0, Z1) :-
	holds(me@[senseCycle->false], Z0),
	update(Z0, [me@[senseCycle->true]], [me@[senseCycle->false]], Z1).	

playerComputeNextAction(Z, dash(P, vector(1000, 1000))) :-
    holds(me@[senseCycle->false], Z),
	holds(me@[oid->P], Z).

playerComputeNextAction(Z, ask(ball)) :-
    holds(me@[senseCycle->true], Z).

playerProcessAction(Z, _, Z).