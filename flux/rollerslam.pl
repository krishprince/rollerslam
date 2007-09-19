:- ['flux'].

%% Focus1 (-63835,0)
%% Focus2 (0,63835)
%% Diameter= 188000

%%%%%%%%%%%%%%%%%%%%
%% PRE-CONDITION %%%
%%%%%%%%%%%%%%%%%%%%

poss(throwA(player(X),_),Z1) :- holds(hasBall(player(X)), Z1),
                                   not_holds(inGround(player(X)),Z1).
poss(release(player(X)),Z1) :- holds(hasBall(player(X)),Z1).
poss(dash(player(X)),Z1):- not_holds(inGround(player(X)),Z1).
poss(kick(player(X),_),Z1) :- holds(hasBall(player(X)), Z1),
                                             not_holds(inGround(player(X)),Z1).
                                             
%% dizer que A e B são diferentes!!!                                             
poss(tackle(player(A),player(B), _),Z1) :- not_holds(inGround(player(A)),Z1),
                                                                 holds(hasBall(player(B)), Z1),
                                                                 not_holds(counterTackle(player(B)),Z1).
                                                                 
poss(counterTackle(player(X)),Z1) :- not_holds(inGround(player(X)),Z1).
poss(hit(player(X),_, _),Z1) :- not_holds(inGround(player(X)), Z1).
poss(catchA(player(X), _),Z1) :- not_holds(inGround(player(X)),Z1),
                                                     not holds(hasBall(player(A)), Z1).
poss(standUp(player(X)),Z1) :- holds(inGround(player(X)),Z1).

%% End Pre-Condition Group

%%%%%%%%%%%%%
%% ACTIONS %%
%%%%%%%%%%%%%

%%
%% Dash Action
%%
state_update(Z1,dash(player(A), New_Acc),Z2,[]) :-
  holds(acceleration(player(A), Old_Acc),Z1),  
  holds(maxAcceleration(Max),Z1),
  checkModule(New_Acc, Max, New_New_Acc),
  update(Z1,[acceleration(player(A), New_New_Acc)],[acceleration(player(A), Old_Acc)],Z2).

%%
%% Throw Action
%%

state_update(Z1,throwA(player(A),Strength),Z2,[]) :-
  (poss(throwA(player(A),Strength),Z1),
  holds(position(ball, vector(X0, Y0)),Z1),
  X is X0 * Strength,
  Y is Y0 * Strength,
  update(Z1,[position(ball, vector(X,Y))],[hasBall(player(A)),position(ball, vector(X0, Y0))],Z2))
  ;
  (not poss(throwA(player(A),Strength),Z1), 
  Z2=Z1
  ).

%%
%% Release Action
%%

state_update(Z1,release(player(X)),Z2,[]) :-
  (poss(release(player(X)),Z1),
  update(Z1,[],[hasBall(player(X))],Z2))
  ;
  (not poss(release(player(X)),Z1), 
  Z2=Z1
  ).

%%
%% Kick Action
%%

state_update(Z1,kick(player(A),Strength),Z2,[]) :-
  (poss(kick(player(A),Strength),Z1),
  holds(position(ball, vector(X0, Y0)),Z1),
  X is X0 * Strength,
  Y is Y0 * Strength,
  update(Z1,[position(ball, vector(X,Y)), isMoving(ball, _)],[hasBall(player(A)),position(ball, vector(X0, Y0))],Z2))
  ;
  (not poss(kick(player(A),Strength),Z1), 
  Z2=Z1
  ).

%%
%% Tackle Action
%%

state_update(Z1,tackle(player(A),player(B), MaxDistance),Z2,[]) :-
  (poss(tackle(player(A),player(B),vector(Xmax, Ymax), MaxDistance),Z1),
  holds(position(player(A), vector(Sxa,Sya)),Z1),
  holds(position(player(B), vector(Sxb,Syb)),Z1),
  closer(Sxa, Sya, Sxb, Syb, MaxDistance),
  update(Z1,[inGround(player(B))],[hasBall(player(B))],Z2))
  ;
  (not poss(tackle(player(A),player(B),vector(Xmax, Ymax), MaxDistance),Z1),
  Z2=Z1
  ).

%%
%% CounterTacle Action
%%

state_update(Z1,counterTackle(player(X)),Z2,[]) :-
  (poss(counterTackle(player(X)),Z1),
  update(Z1,[counterTackle(player(X))],[],Z2))
  ;
  (not poss(counterTackle(player(X)),Z1), 
  Z2=Z1
  ).

%%
%% Hit Action
%%

state_update(Z1,hit(player(A),Strength, MaxDistance),Z2,[]) :-
  (poss(hit(player(A),Strength, MaxDistance),Z1),
  holds(position(player(A), vector(Sxa,Sya)),Z1),
  holds(position(ball, vector(Sxb,Syb)),Z1),
  closer(Sxa, Sya, Sxb, Syb, MaxDistance),
  X is Sxb * Strength,
  Y is Syb * Strength,
  update(Z1,[position(ball, vector(X,Y))],[position(ball, vector(Sxb,Syb))],Z2))
  ;
  (not poss(hit(player(A),Strength, MaxDistance),Z1),
  Z2=Z1
  ).

%%
%% Catch Action
%%

state_update(Z1,catchA(player(X), MaxDistance),Z2,[]) :-
  (poss(catchA(player(X), MaxDistance),Z1),
  holds(position(player(X), vector(Sxa,Sya)),Z1),
  holds(position(ball, vector(Sxb,Syb)),Z1),
  closer(Sxa, Sya, Sxb, Syb, MaxDistance),
  update(Z1,[hasBall(player(X))],[],Z2))
  ;
  (not poss(catchA(player(X), MaxDistance),Z1), 
  Z2=Z1
  ).

%%
%% Stand Up Action
%%

state_update(Z1,standUp(player(X)),Z2,[]) :-
 (poss(standUp(player(X)),Z1),
  update(Z1,[],[inGround(player(X))],Z2))
  ;
 (not poss(standUp(player(X)),Z1), 
 Z2=Z1
 ).

%% End Action Group

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%% RAMIFICATIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

processRamifications(InitialState, FinalState) :-
           ramifySlam(InitialState, ramify, FinalState) .

processRamifications(_, _ ) :- print('RAMIFICATION FAILED').

ramifySlam(Z1,ramify,Z2) :-
collect_ramifiable_objects(Z1, Agents),
ramify_objects(Z1, Agents, Z2).

ramify_objects(Z1, [], Z1).
ramify_objects(Z1, [A|R], Z3) :- ramify_object(Z1,A,Z2), ramify_objects(Z2,R,Z3).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%% RAMIFYING ANIMATED OBJECTS %%%%%%%%%%%%%%%            
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%	(holds(position(Object, vector(X, Y)),Z1), isPointNotInEllipse(188000, -63835, 0, 0, 63835, X, Y)) 	


ramify_animated_object(Z1, Object, Z2):-
   holds(acceleration(Object, vector(Ax0,Ay0)),Z1),
   holds(speed(Object, vector(Vx0,Vy0)),Z1),
   holds(position(Object, vector(Sx0,Sy0)),Z1),
   holds(maxSpeed(Max),Z1),
  Vx1 is Vx0 + Ax0,
  Vy1 is Vy0 + Ay0,
  Sx1 is Sx0 + Vx0,
  Sy1 is Sy0 + Vy0,
  checkModule(vector(Vx1, Vy1), Max, VR),
  
  (
  
  (
     isPointNotInEllipse(188000, -63835, 0, 0, 63835, Sx1, Sy1),
    update(Z1,[acceleration(Object, vector(0,0)), speed(Object, vector(0,0))],
                      [acceleration(Object, vector(Ax0,Ay0)), speed(Object, vector(Vx0,Vy0))],Z2)
   )
  ;  
  (
  update(Z1,[ speed(Object, VR), position(Object, vector(Sx1,Sy1))],
                    [ speed(Object, vector(Vx0,Vy0)), position(Object, vector(Sx0,Sy0))], Z2)            
   )            
  ).
            
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%% RAMIFYING BALL %%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%            
ramify_ball(Z1,  Z3):-
  ramify_animated_object(Z1,ball,Z2),
  
  (
  
  (holds(hasBall(player(A)), Z2),
  holds(position(player(A), vector(Xa,Ya)), Z2),
  holds(position(ball, vector(Xba,Yba)), Z2),

  update(Z2, [position(ball, vector(Xa, Ya))], [position(ball, vector(Xba, Yba))], Z3))
  ; 
  
  (holds(isMoving(ball, _), Z2),
  holds(position(ball, vector(X,Y)), Z2),
  holds(attrition(Attr), Z2),
  X1 is X / Attr,
  Y1 is Y / Attr,  
  update(Z2, [position(ball, vector(X1, Y1))], [position(ball, vector(X, Y))], Z3))
  
  ;
  
  (Z3 = Z2)
  
  ).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%% RAMIFYING OUT-OF-BOUNDARIES %%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%            

stop_object(Z1,Object,Z2) :- 
  holds(acceleration(Object, vector(Ax0,Ay0)),Z1),
  holds(speed(Object, vector(Vx0,Vy0)),Z1),
  update(Z1,[acceleration(Object, vector(0,0)), speed(Object, vector(0,0))],
            [acceleration(Object, vector(Ax0,Ay0)), speed(Object, vector(Vx0,Vy0))],Z2).

object_cantmove(Object, Z1) :- 
	 (Object = player(P), holds(inGround(player(P)),Z1)).

ramify_cantmove(Z1,Object,Z2) :- 
	(object_cantmove(Object,Z1), stop_object(Z1,Object,Z2)) 
	; 	
	(Z1=Z2). 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%% RAMIFICATION %%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%            

ramify_object(Z1, ball, Z3):- ramify_ball(Z1, Z2), ramify_cantmove(Z2,ball, Z3). 
ramify_object(Z1, player(P), Z3):- Player = player(P), ramify_animated_object(Z1, Player, Z2), ramify_cantmove(Z2,Player, Z3). 

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%% HELPER  Functions %%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

runAction(CurrentState, Action, NextState) :- state_update(CurrentState, Action, NextState,[]).

runSeriesOfActions(CurrentState, [], CurrentState).
runSeriesOfActions(CurrentState, [C|L], FinalState) :- runAction(CurrentState, C, NextState),
                                                       runSeriesOfActions(NextState, L, FinalState).
                                                       
in_list(X, [F|Z]) :- X=F ; in_list(X,Z).

collect_ramifiable_objects(State, Result) :- collect_ramifiable_objects0(State, [], Result). 

collect_ramifiable_objects0([], UpToNow, UpToNow).
collect_ramifiable_objects0([speed(Object, _) | R], UpToNow, Result) :-
				(in_list(Object, UpToNow), collect_ramifiable_objects0(R, UpToNow, Result))
									;
				(not in_list(Object, UpToNow), collect_ramifiable_objects0(R, [Object|UpToNow], Result)).

collect_ramifiable_objects0([_ | R], UpToNow, Result) :- 
 			collect_ramifiable_objects0(R, UpToNow, Result).



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
    
      
    
    
               



