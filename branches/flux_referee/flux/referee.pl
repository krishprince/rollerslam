:- ['flux'].

:- dynamic current_state/1.

state_update(Z1,lookBall,Z2,[scored(Team)]) :-
           (holds(score(Score, team(Team2)),Z1),
            Score2 #= Score + 1,
            update(Z1,[score(Score2, team(Team2))],[score(Score, team(Team2))],Z2)).


perform(lookBall, [Y]) :-
           (current_state(Z),
            holds(ballPosition(X1, Y1), Z),
            holds(lastBallPosition(X2,Y2), Z),
            holds(golLine(X3, Y3, X4, Y4, team(Team)), Z),
            intersect(X1, Y1, X2, Y2, X3, Y3, X4, Y4),
            Y = scored(Team)
            ) 
            ; 
            (Y = scored(none)).

init(Z0) :- Z0 = [team(a), score(0, team(a)), golLine(70, 165, 70, 265, team(a)),
                  team(b), score(0, team(b)), golLine(400, 165, 400, 265, team(b)),
                  lastBallPosition(80,187)],
                  duplicate_free(Z0),
                  assert(current_state([])).

main(FinalState) :-
           init(InitialState),
           CurrentState = [ballPosition(49, 217)|InitialState],
           retract(current_state(_)),
           assert(current_state(CurrentState)),
           execute(lookBall, CurrentState, FinalState).
           

           
processRamifications(InitialState, FinalState) :- retract_all(current_state(_)),
           assert(current_state(InitialState)),
           execute(lookBall, InitialState, FinalState).


%%%%%%%%%%%%%%%%%%%
%% Intersection %%%
%%%%%%%%%%%%%%%%%%%
intersect(X1, Y1, X2, Y2, X3, Y3, X4, Y4) :-
           lineEqConstants(A1, B1, C1, X1, Y1, X2, Y2),
           lineEqConstants(A2, B2, C2, X3, Y3, X4, Y4),
	   det(A1, B1, A2, B2, Det),
           Px is ((B2*C1 - B1*C2) / Det),
	   Py is ((A1*C2 - A2*C1) / Det),
           contains(X1, Y1, X2, Y2, Px, Py),
           contains(X3, Y3, X4, Y4, Px, Py).

lineEqConstants(A, B, C, X1, Y1, X2, Y2) :-
           A is Y2-Y1,
	   B is X1-X2,
	   C is A*X1+B*Y1.

det(A1, B1, A2, B2, Det) :-
           Det is A1*B2 - A2*B1,
           not Det = 0.

contains(X1, Y1, X2, Y2, X, Y) :-
           min(X1, X2, Xmin),
           max(X1, X2, Xmax),
           min(Y1, Y2, Ymin),
           max(Y1, Y2, Ymax),
           X >= Xmin, X =< Xmax,
           Y >= Ymin, Y =< Ymax.
