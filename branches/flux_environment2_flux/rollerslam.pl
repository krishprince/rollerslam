:- ['flux'].

%% Focus1 (-63835,0)
%% Focus2 (0,63835)
%% Diameter= 188000

%%%%%%%%%%%%%%%%%%%%
%% PRE-CONDITION %%%
%%%%%%%%%%%%%%%%%%%%

poss(throwA(agent(X),_),Z1) :- holds(hasBall(agent(X)), Z1),
                                   not_holds(inGround(agent(X)),Z1).
poss(release(agent(X)),Z1) :- holds(hasBall(agent(X)),Z1).
poss(dash(agent(X)),Z1):- not_holds(inGround(agent(X)),Z1).
poss(kick(agent(X),_),Z1) :- holds(hasBall(agent(X)), Z1),
                                             not_holds(inGround(agent(X)),Z1).
                                             
%% dizer que A e B são diferentes!!!                                             
poss(tackle(agent(A),agent(B), _),Z1) :- not_holds(inGround(agent(A)),Z1),
                                                                 holds(hasBall(agent(B)), Z1),
                                                                 not_holds(counterTackle(agent(B)),Z1).
                                                                 
poss(counterTackle(agent(X)),Z1) :- not_holds(inGround(agent(X)),Z1).
poss(hit(agent(X),_, _),Z1) :- not_holds(inGround(agent(X)), Z1).
poss(catchA(agent(X), _),Z1) :- not_holds(inGround(agent(X)),Z1),
                                       not_holds(hasBall(agent(X)), Z1).
poss(standUp(agent(X)),Z1) :- holds(inGround(agent(X)),Z1).

%% End Pre-Condition Group

%%%%%%%%%%%%%
%% ACTIONS %%
%%%%%%%%%%%%%

%%
%% Dash Action
%%
state_update(Z1,dash(agent(X), vector(X,Y)),Z2,[]) :-
  holds(acceleration(agent(X), vector(X0,Y0)),Z1),
  update(Z1,[acceleration(agent(X), vector(X,Y))],[acceleration(agent(X), vector(X0,Y0))],Z2).

%%
%% Throw Action
%%

state_update(Z1,throwA(agent(X),Strength),Z2,[]) :-
  (poss(throwA(agent(X),Strength),Z1),
  holds(position(ball, vector(X0, Y0)),Z1),
  X is X0 * Strength,
  Y is Y0 * Strength,
  update(Z1,[position(ball, vector(X,Y))],[hasBall(agent(X)),position(ball, vector(X0, Y0))],Z2))
  ;
  (not poss(throwA(agent(X),Strength),Z1), 
  Z2=Z1
  ).

%%
%% Release Action
%%

state_update(Z1,release(agent(X)),Z2,[]) :-
  (poss(release(agent(X)),Z1),
  update(Z1,[],[hasBall(agent(X))],Z2))
  ;
  (not poss(release(agent(X)),Z1), 
  Z2=Z1
  ).

%%
%% Kick Action
%%

state_update(Z1,kick(agent(X),Strength),Z2,[]) :-
  (poss(kick(agent(X),Strength),Z1),
  holds(position(ball, vector(X0, Y0)),Z1),
  X is X0 * Strength,
  Y is Y0 * Strength,
  update(Z1,[position(ball, vector(X,Y))],[hasBall(agent(X)),position(ball, vector(X0, Y0))],Z2))
  ;
  (not poss(kick(agent(X),Strength),Z1), 
  Z2=Z1
  ).

%%
%% Tackle Action
%%

state_update(Z1,tackle(agent(A),agent(B), MaxDistance),Z2,[]) :-
  (poss(tackle(agent(A),agent(B),vector(Xmax, Ymax), MaxDistance),Z1),
  holds(position(agent(A), vector(Sxa,Sya)),Z1),
  holds(position(agent(B), vector(Sxb,Syb)),Z1),
  closer(Sxa, Sya, Sxb, Syb, MaxDistance),
  update(Z1,[inGround(agent(B))],[hasBall(agent(B))],Z2))
  ;
  (not poss(tackle(agent(A),agent(B),vector(Xmax, Ymax), MaxDistance),Z1),
  Z2=Z1
  ).

%%
%% CounterTacle Action
%%

state_update(Z1,counterTackle(agent(X)),Z2,[]) :-
  (poss(counterTackle(agent(X)),Z1),
  update(Z1,[counterTackle(agent(X))],[],Z2))
  ;
  (not poss(counterTackle(agent(X)),Z1), 
  Z2=Z1
  ).

%%
%% Hit Action
%%

state_update(Z1,hit(agent(X),Strength, MaxDistance),Z2,[]) :-
  (poss(hit(agent(X),Strength, MaxDistance),Z1),
  holds(position(agent(X), vector(Sxa,Sya)),Z1),
  holds(position(ball, vector(Sxb,Syb)),Z1),
  closer(Sxa, Sya, Sxb, Syb, MaxDistance),
  X is Sxb * Strength,
  Y is Syb * Strength,
  update(Z1,[position(ball, vector(X,Y))],[position(ball, vector(Sxb,Syb))],Z2))
  ;
  (not poss(hit(agent(X),Strength, MaxDistance),Z1),
  Z2=Z1
  ).

%%
%% Catch Action
%%

state_update(Z1,catchA(agent(X), MaxDistance),Z2,[]) :-
  (poss(catchA(agent(X), MaxDistance),Z1),
  holds(position(agent(X), vector(Sxa,Sya)),Z1),
  holds(position(ball, vector(Sxb,Syb)),Z1),
  closer(Sxa, Sya, Sxb, Syb, MaxDistance),
  update(Z1,[hasBall(agent(X))],[],Z2))
  ;
  (not poss(catchA(agent(X), MaxDistance),Z1), 
  Z2=Z1
  ).

%%
%% Stand Up Action
%%

state_update(Z1,standUp(agent(X)),Z2,[]) :-
 (poss(standUp(agent(X)),Z1),
  update(Z1,[],[inGround(agent(X))],Z2))
  ;
 (not poss(standUp(agent(X)),Z1), 
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
  
  Vx1 is Vx0 + Ax0,
  Vy1 is Vy0 + Ay0,
  Sx1 is Sx0 + Vx0,
  Sy1 is Sy0 + Vy0,
  
  (
  
  (
     isPointNotInEllipse(188000, -63835, 0, 0, 63835, Sx1, Sy1),
    update(Z1,[acceleration(Object, vector(0,0)), speed(Object, vector(0,0))],
                      [acceleration(Object, vector(Ax0,Ay0)), speed(Object, vector(Vx0,Vy0))],Z2)
   )
  ;  
  (
  update(Z1,[ speed(Object, vector(Vx1,Vy1)), position(Object, vector(Sx1,Sy1))],
                    [ speed(Object, vector(Vx0,Vy0)), position(Object, vector(Sx0,Sy0))], Z2)            
   )            
  ).
            
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%% RAMIFYING BALL %%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%            
ramify_ball(Z1,  Z3):-
  ramify_animated_object(Z1,ball,Z2),
  
  (
  
  (holds(hasBall(agent(X)), Z1),
  holds(position(agent(X), vector(Xa,Ya)), Z1),
  update(Z1, [position(ball, vector(Xa, Ya), position(agent(X), vector(Xa, Ya)))], [position(agent(X), vector(Xa, Ya))], Z2))
  ; 
  
  (holds(isMoving(ball, _), Z2),
  holds(position(ball, vector(X,Y)), Z2),
  X1 is X / Attrition,
  Y1 is Y / Attrition,  
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

           



