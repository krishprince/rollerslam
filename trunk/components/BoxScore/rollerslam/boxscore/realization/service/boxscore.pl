:- ['teste'].

%%%%%%%%%%%%%%%%%%%
%% Intersection %%%
%%%%%%%%%%%%%%%%%%%
pointDistance(point(X1, Y1), point(X2, Y2), Distance) :-
            A is (X1 - X2)*(X1 - X2),
			B is (Y1 - Y2)*(Y1 - Y2),
			sqrt(A+B, Distance).

pointLineDistance(point(X, Y), line(X0, Y0, X1, Y1), Distance) :-
			A is (Y0 - Y1)*X+(X1-X0)*Y+(X0*Y1 - X1*Y0),
			sqrt((X1-X0)*(X1-X0) + (Y1-Y0)*(Y1-Y0), B),
			Distance is A/B.

semiLineIntersection(line(X1, Y1, X2, Y2), line(X3, Y3, X4, Y4), vector(Px, Py)) :- intersect(X1, Y1, X2, Y2, X3, Y3, X4, Y4, Px, Py).

semiLineIntersection(line(X1, Y1, X2, Y2), line(X3, Y3, X4, Y4)) :- intersect(X1, Y1, X2, Y2, X3, Y3, X4, Y4, _, _).

semiLineIntersection(X1, Y1, X2, Y2, X3, Y3, X4, Y4, Px, PY) :-
           lineEqConstants(A1, B1, C1, X1, Y1, X2, Y2),
           lineEqConstants(A2, B2, C2, X3, Y3, X4, Y4),
	       det(A1, B1, A2, B2, Det),
           Px is ((B2*C1 - B1*C2) / Det),
	       Py is ((A1*C2 - A2*C1) / Det),
           contains(X1, Y1, X2, Y2, Px, Py),
           contains(X3, Y3, X4, Y4, Px, Py).

lineIntersection(line(X1, Y1, X2, Y2), line(X3, Y3, X4, Y4), vector(Px, Py)) :- lineIntersection(X1, Y1, X2, Y2, X3, Y3, X4, Y4, Px, Py).

lineIntersection(line(X1, Y1, X2, Y2), line(X3, Y3, X4, Y4)) :- lineIntersection(X1, Y1, X2, Y2, X3, Y3, X4, Y4, _, _).

lineIntersection(X1, Y1, X2, Y2, X3, Y3, X4, Y4, Px, Py) :-
           lineEqConstants(A1, B1, C1, X1, Y1, X2, Y2),
           lineEqConstants(A2, B2, C2, X3, Y3, X4, Y4),
	       det(A1, B1, A2, B2, Det),
           Px is ((B2*C1 - B1*C2) / Det),
	       Py is ((A1*C2 - A2*C1) / Det).
	       
poitLineIntersection(vector(Px, Py), line(X1, Y1, X2, Y2)) :- poitLineIntersection(Px, Py, X1, Y1, X2, Y2).

poitLineIntersection(Px, Py, X1, Y1, X2, Y2) :- 
            M1 is ((Y2-Y1) / (X2-X1)),  %Verifica se possuem o mesmo coeficiente angular y-y0 = m(x-x0)
            M2 is ((Py - Y1) / (Px - X1)),
            M1 = M2.

collisionPath(vector(PositionX, PositionY), vector(Sx, Sy), Line) :-
            PositionX2 is PositionX + Sx,
            PositionY2 is PositionY + Sy,
            lineIntersection(line(PositionX, PositionY, PositionX2, PositionY2), Line, vector(Px, Py)),
            NPx is Px - PositionX, %valor na nova base
            NPy is Py - PositionY, %valor na nova base
            NPx*Sx >= 0, %Verifica se estão no mesmo quadrante
            NPy*Sy >= 0. %Verifica se estão no mesmo quadrante
            
collisionPath(vector(PositionX, PositionY), vector(Sx, Sy), vector(Px, Py)) :-
            PositionX2 is PositionX + Sx,
            PositionY2 is PositionY + Sy,
            poitLineIntersection(vector(Px, Py), line(PositionX, PositionY, PositionX2, PositionY2)).

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



%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%initialize                         %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
boxscoreInitializeModel(_, []).

boxscoreUpdateModel(Z0, Z1) :-
            processaRamifications(Z0, [pass, shootStreak, pass_suc, stolen_pass, tackle_suc, counter_tackle_suc], Z2),
            processaRamifications(Z2, [kick, throw, spike, volley, dropAndKick, tackle, counter_tackle], Z1).
            
boxscoreUpdateModel(Z0, Z1) :- Z1 = Z0.            

boxscoreComputeNextAction(Z, askAll).

processaRamifications(Z0, [],Z1) :- Z1 = Z0.

processaRamifications(Z0, [Action|L],Z1) :- processaRamification(Z0, Action, Z2), processaRamifications(Z2, L, Z1).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%ramifications - Memorize Actions   %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
processaRamification(Z0, kick, Z1) :-
            actionKick(P, Z0),
            update(Z0, [action(kick, P)], [], Z1).

processaRamification(Z0, kick, Z1) :- Z1 = Z0.

processaRamification(Z0, throw, Z1) :-
            actionThrow(P, Z0),
            update(Z0, [action(throw, P)], [], Z1).
            
processaRamification(Z0, throw, Z1) :- Z1 = Z0.

processaRamification(Z0, spike, Z1) :-
            actionSpike(P, Z0),
            update(Z0, [action(spike, P)], [], Z1).
            
processaRamification(Z0, spike, Z1) :- Z1 = Z0.

processaRamification(Z0, volley, Z1) :-
            actionVolley(P, Z0),
            update(Z0, [action(volley, P)], [], Z1).
            
processaRamification(Z0, volley, Z1) :- Z1 = Z0.

processaRamification(Z0, dropAndKick, Z1) :-
            actionDropAndKick(P, Z0),
            update(Z0, [action(dropAndKick, P)], [], Z1).
            
processaRamification(Z0, dropAndKick, Z1) :- Z1 = Z0.

processaRamification(Z0, tackle, Z1) :-
            actionTackle(P, P2, Z0),
            update(Z0, [action(tackle, P, P2)], [], Z1).
            
processaRamification(Z0, tackle, Z1) :- Z1 = Z0.

processaRamification(Z0, counter_tackle, Z1) :-
            actionCounter_tackle(P, P2, Z0),
            update(Z0, [action(counter_tackle, P, P2)], [], Z1).
            
processaRamification(Z0, counter_tackle, Z1) :- Z1 = Z0.

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%ramifications - stats              %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
processaRamification(Z0, pass, Z1) :-
            holds(action(Action, P), Z0),
            member(Action, [kick, throw, spike, volley, dropAndKick]),
            playerTeam(P, Team, Z0),
            playerTeam(P2, Team, Z0),
            not P = P2,
            playerPosition(P2, P2Position, Z0),
            ballSpeed(BallSpeed, Z0),
            ballPosition(BallPosition, Z0),
            collisionPath(BallPosition, BallSpeed, P2Position),
            update(Z0, [pass(P, Action, P2), passAttempt(P, P2)], [action(Action, P)], Z1).
            
processaRamification(Z0, pass, Z1) :- Z1 = Z0.

processaRamification(Z0, shootStreak, Z1) :-
            holds(action(Action, P), Z0),
            member(Action, [kick, throw, spike, volley, dropAndKick]),
            playerTeam(P, Team, Z0),
            ballPosition(BallPosition, Z0),
            ballSpeed(BallSpeed, Z0),
            goalLine(Line, Team, Z0),
            collisionPath(BallPosition, BallSpeed, Line),
            holds(shootStreak(P, Action, N), Z0),
            N2 is N + 1,
            update(Z0, [shootStreak(P, Action, N2), goalShot(P, Action)], [shootStreak(P, Action, N), action(Action, P)], Z1).

processaRamification(Z0, shootStreak, Z1) :- Z1 = Z0.

processaRamification(Z0, pass_suc, Z1) :-
            holds(passAttempt(P, P2), Z0),
            holds(hasBall(player(P2)), Z0),
            update(Z0, [pass_suc(P, P2)], [passAttempt(P, P2)], Z1).
            
processaRamification(Z0, pass_suc, Z1) :- Z1 = Z0.

processaRamification(Z0, stolen_pass, Z1) :-
            holds(passAttempt(P, P1), Z0),
            playerTeam(P, Team),
            holds(hasBall(player(P2)), Z0),
            playerTeam(P2, Team2),
            not Team = Team2,
            update(Z0, [stolen_pass(P2, P, P1)], [passAttempt(P, P1)], Z1).

processaRamification(Z0, stolen_pass, Z1) :- Z1 = Z0.

processaRamification(Z0, wrong_pass, Z1) :-
            holds(passAttempt(P, P1), Z0),
            playerTeam(P, Team),
            holds(hasBall(player(P2)), Z0),
            playerTeam(P2, Team),
            update(Z0, [wrong_pass(P2, P, P1)], [passAttempt(P, P1)], Z1).

processaRamification(Z0, wrong_pass, Z1) :- Z1 = Z0.

processaRamification(Z0, tackle_suc, Z1) :-
            holds(action(tackle, P, P2), Z0),
            onFloor(P2, Z0),
            update(Z0, [tackle_suc(P, N+1), tackled(P2, Y+1)], [tackle_suc(P2, N), tackled(P2, Y), action(tackle, P, P2)], Z1).
            
processaRamification(Z0, tackle_suc, Z1) :- Z1 = Z0.

processaRamification(Z0, counter_tackle_suc, Z1) :-
            holds(action(counter_tackle, P), Z0),
            holds(action(tackle, P2, P), Z0),
            not onFloor(P, Z0),
            update(Z0, [counter_tackle_suc(P, N+1)], [counter_tackle_suc(P, N), action(counter_tackle, P, P2)], Z1).
            %retirar os counter tackle
            
processaRamification(Z0, counter_tackle_suc, Z1) :- Z1 = Z0.