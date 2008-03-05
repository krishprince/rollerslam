
gamePhysicsInitializeModel([PlayersPerTeam], 
	[	
		ball@[position->vector(0,0)],
		ball@[velocity->vector(0,0)],
		ball@[acceleration->vector(0,0)] | Players
			
	]) :-
	
	players(PlayersPerTeam, Players).

gamePhysicsUpdateModel(Z0, Z0).

gamePhysicsProcessAction(Z0, dash(P, Dir), Z1) :-
	holds(P@[acceleration->Acc], Z0),
	update(Z0, [P@[acceleration->Dir]], [P@[acceleration->Acc]], Z1).
	
gamePhysicsComputeNextAction(_, noAction).


playerOid(PID, Team, Atom) :-
	((Team = "TEAM_A", TeamAtom = a) ;
	 (Team = "TEAM_B", TeamAtom = b)),

	 concat_atom([p,TeamAtom,PID], Atom).

players(NumberOfPlayers, Oids) :- 
	players0(NumberOfPlayers, "TEAM_A", OidsA),
	players0(NumberOfPlayers, "TEAM_B", OidsB),
	append(OidsA, OidsB, OidsF),

	flatten(OidsF, Oids).

players0(0, _, []).
players0(N, T, [PS|R]) :- 
	NM is N - 1, 
	players0(NM, T, R),
	playerOid(N, T, P),

	generatePlayerPosition(T, Pos),

	PS = [ 
		P@[position->Pos],
		P@[velocity->vector(0,0)],
		P@[acceleration->vector(0,0)],
		P@[team->T]
	     ].

generatePlayerPosition(Team, vector(X,Y)) :-
	genPlayerPosition(X0, Y),
	
	((Team = "TEAM_A", X = X0) ;
         (Team = "TEAM_B", X is -X0)).

random(V,M) :- 
	random(V0), V is V0 mod M.

genPosition(X, Y) :-
	random(X0, 92000),
	X1 is X0 + 1000,

	X = X1,

	random(Y0, 67000),
	Y1 is Y0 + 1000,
	Y2 is Y1 * 2,
	Y3 is Y2 - 67000,
	Y = Y3.

pointInRink(X,Y) :-
	computeDistance(X,Y,-63835,0, D1),
	computeDistance(X,Y, 63835,0, D2),

	Sum is D1 + D2,

	Sum < 188000.
	
computeDistance(X,Y,X0,Y0,D) :- 
	Dx is X-X0,
	Dy is Y-Y0,
	
	SDx is Dx * Dx,
	SDy is Dy * Dy,
	
	Sum is SDx + SDy,
	sqrt(Sum, D).


genPlayerPosition(X,Y) :-
	genPosition(X0,Y0),
	((pointInRink(X0,Y0), X = X0, Y = Y0) ;
	 (genPlayerPosition(X,Y))).