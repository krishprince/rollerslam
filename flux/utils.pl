:- ['flux'].


%%%%%%%%%%%%%%%%%%%%%
%% helper functions%%
%%%%%%%%%%%%%%%%%%%%%

isInField(Sx, FieldPart):-
               (Sx<0,
               FieldPart is 'West');
               (Sx>0,
               FieldPart is 'East').

closer(Sxb, Syb, Sxa, Sya, Distance) :-
           calcDistance(Sxb, Syb, Sxa, Sya, A, B, C),
           Distance >= A.

calcDistance(Sxb, Syb, Sxa, Sya, A, B, C) :-
                  B is (Sxb - Sxa),
                  C is (Syb - Sya),
                  B is (B * B),
                  C is (C * C),
                  A is ((B + C)^(1/2)).
                  
                  
runAction(CurrentState, Action, NextState) :- state_update(CurrentState, Action, NextState,[]).

runSeriesOfActions(CurrentState, [], CurrentState).
runSeriesOfActions(CurrentState, [C|L], FinalState) :- runAction(CurrentState, C, NextState),
                                                       runSeriesOfActions(NextState, L, FinalState).
                                                       
in_list(X, [F|Z]) :- X=F ; in_list(X,Z).

collect_ramifiable_agents(State, Result) :- collect_ramifiable_agents0(State, [], Result). 

collect_ramifiable_agents0([], UpToNow, UpToNow).
collect_ramifiable_agents0([speed(Agent, _) | R], UpToNow, Result) :-
				(in_list(Agent, UpToNow), collect_ramifiable_agents0(R, UpToNow, Result))
									;
				(not in_list(Agent, UpToNow), collect_ramifiable_agents0(R, [Agent|UpToNow], Result)).
collect_ramifiable_agents0([X | R], UpToNow, Result) :- 
 			collect_ramifiable_agents0(R, UpToNow, Result).
                         
                         
                         
perform(lookBall, [Y]) :-
           (current_state(Z),
           holds(ballPosition(X1, Y1), Z),
           holds(lastBallPosition(X2,Y2), Z),
           holds(golLine(X3, Y3, X4, Y4, team(Team)), Z),
           intersect(X1, Y1, X2, Y2, X3, Y3, X4, Y4),
           Y = scored(Team)).


complex_action(..., Z1, Z2) :- Z2 = Z1.


init(Z0) :- Z0 = [team(a), score(0, team(a)), golLine(70, 165, 70, 265, team(a)),
                  team(b), score(0, team(b)), golLine(400, 165, 400, 265, team(b))],
                  duplicate_free(Z0),
                  assert(current_state([])).

main(FinalState) :-
           init(InitialState),
           CurrentState = [InitialState],
           retract(current_state(_)),
           assert(current_state(CurrentState)),
           execute(..., CurrentState, FinalState).



processRamificationsReferee(InitialState, FinalState) :- retract_all(current_state(_)),
           assert(current_state(InitialState)),
           execute(..., InitialState, FinalState).
           
           
           
           
isPointInEllipse(MajorAxis, F1x, F1y, F2x, F2y, Px, Py) :-
	calcDistance(F1x, F1y, Px, Py, Dist1),
	calcDistance(F2x, F2y, Px, Py, Dist2),
	Sum is Dist1 + Dist2,
	MajorAxis >= Sum.

sumVector(vector(X,Y), vector(X1,Y1), vector(XR, YR)) :- XR is X + X1, YR is Y + Y1.
subtractVector(vector(X,Y), vector(X1,Y1), vector(XR, YR)) :- XR is X - X1, YR is Y - Y1.
multVector(vector(X,Y), Num, vector(XR, YR)):- XR is X * Num, YR is Y * Num.

getModule(vector(X,Y), Result) :- 
    A is X / 1000,
    B is Y / 1000,
    A2 is A * A,
    B2 is B * B,
    C is ((A2 + B2)^(1/2)),
    Result is C * 1000.
    
limitModuloTo(vector(X,Y), Max) :- 
    getModule(vector(X,Y), Result),
    Max > Result.
    
setModule(vector(X,Y), Max, vector(XR, YR)) :- 
    getModule(vector(X,Y), Result),
    Ratio is (Max/Result),
    multVector(vector(X,Y), Ratio, vector(XR, YR)).
    
checkModule(V, Max, VR):- 
	    (limitModuloTo(V, Max), V=VR)
	    ;
	    setModule(V, Max, VR).
	    
checkError(Error, Num):-
        Error > Num.
        
validateError(Error, Num, Result):-
        (checkError(Error, Num),
         Result is Num)
        ; 
        (Result is Error).  
        
checkModule(Param):-
       Param = 0.
%% Is there any way to use mod with decimals?       

moduleFlux(Strength,ModResult, ResultX, ResultY):-
       (checkModule(ModResult),
       ResultX is Strength,
       ResultY is 1)
       ;
       (ResultX is 1,
       ResultY is Strength).              	    
       
%%---------------------
