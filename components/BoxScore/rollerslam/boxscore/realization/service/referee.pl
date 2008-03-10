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

intersect(line(X1, Y1, X2, Y2), line(X3, Y3, X4, Y4)) :- intersect(X1, Y1, X2, Y2, X3, Y3, X4, Y4).

intersect(X1, Y1, X2, Y2, X3, Y3, X4, Y4) :-
           lineEqConstants(A1, B1, C1, X1, Y1, X2, Y2),
           lineEqConstants(A2, B2, C2, X3, Y3, X4, Y4),
	       det(A1, B1, A2, B2, Det),
           Px is ((B2*C1 - B1*C2) / Det),
	       Py is ((A1*C2 - A2*C1) / Det),
           contains(X1, Y1, X2, Y2, Px, Py),
           contains(X3, Y3, X4, Y4, Px, Py).

lineIntersection(line(X1, Y1, X2, Y2), line(X3, Y3, X4, Y4)) :- lineIntersection(X1, Y1, X2, Y2, X3, Y3, X4, Y4).

lineIntersection(X1, Y1, X2, Y2, X3, Y3, X4, Y4) :-
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


%%%%%%%%%%%%%%%%%%%
%% Utils        %%%
%%%%%%%%%%%%%%%%%%%
history(Fact) :-
			memory(M),
			member(Fact, M).

playerPoint(player(Player), Point) :-
            current_state(Z),
			holds(playerPosition(player(Player), X, Y), Z),
			Point = point(X, Y).

ballPoint(Point) :-
            current_state(Z),
			holds(ballPosition(X, Y), Z),
			Point = point(X, Y).

ballLine(Line) :-
			current_state(Z),
			holds(position(ball, X1, Y1), Z),
			history(position(ball, X2,Y2)),
			Line = line(X1, Y1, X2, Y2).

lowGoalLine(Line, team(Team)) :-
			current_state(Z),
			member(goalLine(X1, Y1, X2, Y2, team(Team)), Z),
			Line = line(X1, Y1, X2, Y2).

basketPoint(Position, team(Team)) :-
			current_state(Z),
			member(team(Team), Z),
			member(basket(X, Y, team(Team)), Z),
			Position = point(X, Y).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%ramifications - Memorize Actions   %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

ramificate(playerKick, ZMemory, ZMemory2) :-
            execute(sensePhysics([kick(player)]), ZPhysics),
			holds(time(Time), ZPhysics),
			holds(kick(Player), ZPhysics),
			holds(position(Player, Position), ZPhysics),
            update(ZMemory, [kick(Player, Time, Position), touched(no)],[kick(_,_,_), throw(_,_,_), volley(_,_,_), touched(_)], ZMemory2).

ramificate(playerThrow, ZMemory, ZMemory2) :-
            execute(sensePhysics([throw(player)]), ZPhysics),
			holds(time(Time), ZPhysics),
			holds(throw(Player), ZPhysics),
			holds(position(Player, Position), ZPhysics),
            update(ZMemory, [throw(Player, Time, Position), touched(no)],[kick(_,_,_), throw(_,_,_), volley(_,_,_), touched(_)], ZMemory2).

ramificate(playerVolley, ZMemory, ZMemory2) :-
            execute(sensePhysics([volley(Player)]), ZPhysics),
			holds(time(Time), ZPhysics),
			holds(volley(Player), ZPhysics),
			holds(position(Player, Position), ZPhysics),
            update(ZMemory, [volley(Player, Time, Position), touched(no)],[kick(_,_,_), throw(_,_,_), volley(_,_,_), touched(_)], ZMemory2).

ramificate(playerHasBall, ZMemory, ZMemory2) :-
			or_holds([volley(_, Time, _), throw(_, Time, _), kick(_, Time, _)], ZMemory),
			execute(sensePhysics(hasBall(player)), ZPhysics),
			holds(hasBall(_), ZPhysics),
            not_holds(time(Time), ZPhysics),
            update(ZMemory, [],[kick(_,_,_), throw(_,_,_), volley(_,_,_), touched(_)], ZMemory2).


ramificate(ballTouched, ZMemory, ZMemory2) :-
            or_holds([volley(_, Time, _), throw(_, Time, _), kick(_, Time, _)], ZMemory),
            holds(position(ball, Position), ZPhysics),
            holds(position(player(X), Position), ZPhysics),
            update(ZMemory, [touched(yes)], [touched(no)], ZMemory2).

ramificate(tryAttempt, ZMemory, ZMemory2) :-
            execute(sensePhysics(hasBall(player)), ZPhysics),
            holds(hasball(Player), ZPhysics),
            holds(position(Player, Position), ZPhysics),
            holds(team(Player, Team), ZPhysics),
            holds(time(Time), ZPhysics),
            lowGoalLine(GoalLine, Team),
			intersect(Position, GoalLine),
			update(ZMemory, [tryAttempt(Player, Time)], [], ZMemory2).

ramificate(removeTryAttemp, ZMemory, ZMemory2) :-
            execute(sensePhysics(hasBall(player)), ZPhysics),
            holds(tryAttempt(Player, _), ZMemory),
            not_holds(hasball(Player), ZPhysics),
            update(ZMemory, [], [tryAttempt(_, _)], ZMemory2).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%ramifications - Scores   %
%%%%%%%%%%%%%%%%%%%%%%%%%%%%
ramificate(scores, ZStats, ZStats2) :-
            execute(sensePhysics([position(ball), time]), ZPhysics),
            ramificate(lowGoalScored, ZStats, ZStats2).

ramificate(lowGoalScored, ZStats, ZStats2) :-
			physics(ZPhysics),
			holds(time(Time), ZPhysics),
			ballLine(BallLine),
            lowGoalLine(GoalLine, Team),
			intersect(BallLine, GoalLine),
			scoreInformation(lowGoalScored, Team, Info),
			update(ZStats, [scored(lowGoal, time(Time), Info)], [], ZStats2).

ramificate(lowGoalTryScored, ZStats, ZStats2) :-
			history(ZMemory),
            execute(sensePhysics(touchFloor(player)), ZPhysics),
            holds(time(Time), ZPhysics)
            holds(touchFloor(Player), ZPhysics),
			holds(tryAttempt(Player, Time)),
			update(ZStats, [scored(try, time(Time), Info)], [], ZStats2).

ramificate(basketScored, ZStats, ZStats2) :-
			execute(sensePhysics(speed(ball)), ZPhysics),
			holds(time(Time), ZPhysics)
			holds(ballSpeed(BallSpeed), ZPhysics),
			ballPoint(BallPoint),
			basketPoint(BallPoint),
			BallSpeed =< 5,  %verificar velocidades minima
			scoreInformation(basketScored, BallPoint, Info),
            update(ZStats, [scored(basket, position(BallPoint), time(Time), Info)], [], ZStats2).

%%%%%%%%%%%%%%%%%%%%
%%score information%
%%%%%%%%%%%%%%%%%%%%
scoreInformation(lowGoalScored, Team, Info) :-
            playerBodyPart(Player, BodyPart, Position),
            lowGoalLine(GoalLine, Team),
			calcDistance(Position, GoalLine, Distance),
			ballTouched(Touched),
			Info = [Player, BodyPart, Distance, Touched].


scoreInformation(basketScored, BasketPosition, Info) :-
			playerBodyPart(Player, BodyPart, Position),
			calcDistance(Position, BasketPosition, Distance),
			Info = [Player, BodyPart, Distance].

playerBodyPart(Player, hand, Position) :- history(throw(Player,_,Position)).
playerBodyPart(Player, foot, Position) :- history(kick(Player,_,Position)).

ballTouched(touched) :- history(touched(yes)).
ballTouched(clear) :- history(touched(no)).

volley(Player, Volley) :-
            ballTouched(clear),
			history(volley(Player,_,_)),
			Volley = volley
			;
            Volley = [].

calcDistance(Position, Team, near) :- inSmallEllipse(Player, Team).

calcDistance(Position, Team, far) :- not inSmallEllipse(Player, Team).



%%%%%%%%%%%%%%%%%%%
%% Debug        %%%
%%%%%%%%%%%%%%%%%%%

main2(Line, team(Team)) :-
		   	init(InitialState),
		   	retract(current_state(_)),
           	assert(current_state(InitialState)),
		   	lowGoalPosition(line(X, Y, U, W), team(Team)),
			Line = line(X, Y, U, W).


init(Z0) :- Z0 = [team(a), score(0, team(a)), goalLine(70, 165, 70, 265, team(a)),
                  team(b), score(0, team(b)), goalLine(400, 165, 400, 265, team(b)),
                  lastBallPosition(80,187)],
                  duplicate_free(Z0),
                  assert(current_state([])).

main(FinalState) :-
           	init(InitialState),
           	CurrentState = [ballPosition(49, 48)|InitialState],
           	retract(current_state(_)),
           	assert(current_state(CurrentState)),
           	execute(lookBall, CurrentState, FinalState).



processRamificationsReferee(InitialState, FinalState) :- retract_all(current_state(_)),
           	assert(current_state(InitialState)),
           	execute(lookBall, InitialState, FinalState).
