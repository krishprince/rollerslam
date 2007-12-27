:- ['flux'].

in_list(X, [F|Z]) :- X=F ; in_list(X,Z).

closer(Sxa, Sya, Sxb, Syb, MaxDistance) :-
           calcDistance(Sxa, Sya, Sxb, Syb, A),
           MaxDistance >= A.

calcDistance(Sxa, Sya, Sxb, Syb, A) :-
                  B is (Sxb - Sxa),
                  C is (Syb - Sya),
                  B2 is (B * B),
                  C2 is (C * C),
                  A is ((B2 + C2)^(1/2)).

isPointNotInEllipse(MajorAxis, F1x, F1y, F2x, F2y, Px, Py) :- 
	calcDistance(F1x, F1y, Px, Py, Dist1),
	calcDistance(F2x, F2y, Px, Py, Dist2),
	
	Sum is Dist1 + Dist2,
	Sum > MajorAxis.

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
    

 init(PlayerID, PlayerTeam) :- assert(player(PlayerID)), assert(team(PlayerTeam)). 

processAgentCycle(WorldState, Action) :- 
	goal(OldGoal), action(OldAction),
	Z = [goal(OldGoal), action(OldAction) | WorldState], 
	
	state_update(Z, interpretEnvModel, Z1, []),
    state_update(Z1, updateGoal, Z2, []),
    state_update(Z2, computeNextAction, Z3, []),
    
    holds(goal(NewGoal), Z3),
    holds(action(NewAction), Z3),
    
	retract(goal(_)),
	retract(action(_)),
	
	assert(goal(NewGoal)),
	assert(action(NewAction)),
	
	Action = NewAction.
	      
    
    
               



