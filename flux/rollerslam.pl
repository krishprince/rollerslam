:- ['flux'].

%% Focus1 (-63835,0)
%% Focus2 (0,63835)
%% Diameter= 188000

%%%%%%%%%%%%%%%%%%%%
%% PRE-CONDITION %%%
%%%%%%%%%%%%%%%%%%%%

poss(throwA(Agent,Strength),Z1) :- holds(hasBall(Agent), Z1),
                                   not_holds(inGround(Agent),Z1).
poss(release(Agent),Z1) :- holds(hasBall(Agent),Z1).
poss(dash(Agent),Z1):- not_holds(inGround(Agent),Z1).
poss(kick(Agent,Strength),Z1) :- holds(hasBall(Agent), Z1),
                                 not_holds(inGround(Agent),Z1).
poss(tackle(Agent,AgentB,vector(Xmax, Ymax), MaxDistance),Z1) :- not_holds(inGround(Agent),Z1),
                                                                 holds(hasBall(AgentB), Z1),
                                                                 not_holds(counterTackle(AgentB),Z1),
                                                                 closer(Sxa, Sya, Sxb, Syb, MaxDistance).
poss(counterTackle(Agent),Z1) :- not_holds(inGround(Agent),Z1).
  poss(hit(Agent,Strength, MaxDistance),Z1) :- not_holds(inGround(Agent), Z1),
                                               closer(Sxa, Sya, Sxb, Syb, MaxDistance).
poss(catchA(Agent, MaxDistance),Z1) :- not_holds(inGround(Agent),Z1),
                                       not_holds(hasBall(Agent), Z1),
                                       closer(Sxa, Sya, Sxb, Syb, MaxDistance).
poss(standUp(Agent),Z1) :- holds(inGround(Agent),Z1).
poss(ramifySit2, Z1):- holds(isMoving(Ball, Attrition), Z1).
poss(ramifySit3, Z1):- holds(outBoundary(Object), Z1).


%% End Pre-Condition Group

%%%%%%%%%%%%%
%% ACTIONS %%
%%%%%%%%%%%%%

%%
%% Dash Action
%%
state_update(Z1,dash(Agent, vector(X,Y)),Z2,[]) :-
  holds(acceleration(Agent, vector(X0,Y0)),Z1),
  update(Z1,[acceleration(Agent, vector(X,Y))],[acceleration(Agent, vector(X0,Y0))],Z2).

%%
%% Throw Action
%%

state_update(Z1,throwA(Agent,Strength),Z2,[]) :-
  (poss(throwA(Agent,Strength),Z1),
  holds(position(Ball, vector(X0, Y0)),Z1),
  X #= X0 * Strength,
  Y #= Y0 * Strength,
  update(Z1,[position(Ball, vector(X,Y))],[hasBall(Agent),position(Ball, vector(X0, Y0))],Z2))
  ;
  (not poss(throwA(Agent,Strength),Z1), 
  Z2=Z1
  ).

%%
%% Release Action
%%

state_update(Z1,release(Agent),Z2,[]) :-
  (poss(release(Agent),Z1),
  update(Z1,[],[hasBall(Agent)],Z2))
  ;
  (not poss(release(Agent),Z1), 
  Z2=Z1
  ).

%%
%% Kick Action
%%

state_update(Z1,kick(Agent,Strength),Z2,[]) :-
  (poss(kick(Agent,Strength),Z1),
  holds(position(Ball, vector(X0, Y0)),Z1),
  X #= X0 * Strength,
  Y #= Y0 * Strength,
  update(Z1,[position(Ball, vector(X,Y))],[hasBall(Agent),position(Ball, vector(X0, Y0))],Z2))
  ;
  (not poss(kick(Agent,Strength),Z1), 
  Z2=Z1
  ).

%%
%% Tackle Action
%%

state_update(Z1,tackle(Agent,AgentB,vector(Xmax, Ymax), MaxDistance),Z2,[]) :-
  (poss(tackle(Agent,AgentB,vector(Xmax, Ymax), MaxDistance),Z1),
  holds(position(Agent, vector(Sxa,Sya)),Z1),
  holds(position(AgentB, vector(Sxb,Syb)),Z1),
  update(Z1,[inGround(AgentB)],[hasBall(AgentB)],Z2))
  ;
  (not poss(tackle(Agent,AgentB,vector(Xmax, Ymax), MaxDistance),Z1),
  Z2=Z1
  ).

%%
%% CounterTacle Action
%%

state_update(Z1,counterTackle(Agent),Z2,[]) :-
  (poss(counterTackle(Agent),Z1),
  update(Z1,[counterTackle(Agent)],[],Z2))
  ;
  (not poss(counterTackle(Agent),Z1), 
  Z2=Z1
  ).

%%
%% Hit Action
%%

state_update(Z1,hit(Agent,Strength, MaxDistance),Z2,[]) :-
  (poss(hit(Agent,Strength, MaxDistance),Z1),
  holds(position(Agent, vector(Sxa,Sya)),Z1),
  holds(position(Ball, vector(Sxb,Syb)),Z1),
  X #= Sxb * Strength,
  Y #= Syb * Strength,
  update(Z1,[position(Ball, vector(X,Y))],[position(Ball, vector(Sxb,Syb))],Z2))
  ;
  (not poss(hit(Agent,Strength, MaxDistance),Z1),
  Z2=Z1
  ).

%%
%% Catch Action
%%

state_update(Z1,catchA(Agent, MaxDistance),Z2,[]) :-
  (poss(catchA(Agent, MaxDistance),Z1),
  holds(position(Agent, vector(Sxa,Sya)),Z1),
  holds(position(Ball, vector(Sxb,Syb)),Z1),
  update(Z1,[hasBall(Agent)],[],Z2))
  ;
  (not poss(catchA(Agent, MaxDistance),Z1), 
  Z2=Z1
  ).

%%
%% Stand Up Action
%%

state_update(Z1,standUp(Agent),Z2,[]) :-
 (poss(standUp(Agent),Z1),
  update(Z1,[],[inGround(Agent)],Z2))
  ;
 (not poss(standUp(Agent),Z1), 
 Z2=Z1
 ).

%% End Action Group

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%% RAMIFICATIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

processRamifications(InitialState, FinalState) :-
		runAction(InitialState, ramify, FinalState).


state_update(Z1,ramify,Z2,[]) :-
collect_ramifiable_agents(Z1, Agents),
ramify_objects(Z1, Agents, Z2).

ramify_objects(Z1, [], Z1).
ramify_objects(Z1, [A|R], Z3) :- ramify_object(Z1,A,Z2), ramify_objects(Z2,R,Z3).

ramify_object(Z1, Agent, Z2) :-
 ( holds(acceleration(Agent, vector(Ax0,Ay0)),Z1),
   holds(speed(Agent, vector(Vx0,Vy0)),Z1),
   holds(position(Agent, vector(Sx0,Sy0)),Z1),
  Vx1 #= Vx0 + Ax0,
  Vy1 #= Vy0 + Ay0,
  Sx1 #= Sx0 + Vx0,
  Sy1 #= Sy0 + Vy0,
  update(Z1,[acceleration(Agent, vector(Ax0,Ay0)), speed(Agent, vector(Vx1,Vy1)), position(Agent, vector(Sx1,Sy1))],
            [acceleration(Agent, vector(Ax0,Ay0)), speed(Agent, vector(Vx0,Vy0)), position(Agent, vector(Sx0,Sy0))],Z2))
            ;
 (poss(ramifySit2, Z1),
  holds(position(Ball, vector(X,Y)), Z1),
  X1 #= X / Attrition,
  Y1 #= Y / Attrition,
  update(Z1, [position(Ball, vector(X1, Y1))], [position(Ball, vector(X, Y))], Z2))
            ;
 (poss(standUp(Agent),Z1),
  holds(acceleration(Agent, vector(Ax0,Ay0)),Z1),
  holds(speed(Agent, vector(Vx0,Vy0)),Z1),
  Vx1 #= 0,
  Vy1 #= 0,
  Ax1 #= 0,
  Ay1 #= 0,
  update(Z1,[acceleration(Agent, vector(Ax1,Ay1)), speed(Agent, vector(Vx1,Vy1))],
            [acceleration(Agent, vector(Ax0,Ay0)), speed(Agent, vector(Vx0,Vy0))],Z2))
            ;
 (poss(kick(Agent,Strength),Z1),
  holds(position(Agent, vector(X1, Y1)),Z1),
  holds(position(Ball, vector(X0, Y0)),Z1),
  X2 #= X1,
  Y2 #= Y1,
  update(Z1, [position(Ball, vector(X2, Y2))], [position(Ball, vector(X0, Y0))], Z2))
            ;
(poss(ramifySit3, Z1),
 (Object='ball',
 holds(acceleration(Ball, vector(Ax0,Ay0)),Z1),
 holds(speed(Ball, vector(Vx0,Vy0)),Z1),
  Vx1 #= 0,
  Vy1 #= 0,
  Ax1 #= 0,
  Ay1 #= 0,
  update(Z1,[acceleration(Ball, vector(Ax1,Ay1)), speed(Ball, vector(Vx1,Vy1))],
            [acceleration(Ball, vector(Ax0,Ay0)), speed(Ball, vector(Vx0,Vy0))],Z2)))
           ;
(holds(acceleration(Agent, vector(Ax0,Ay0)),Z1),
 holds(speed(Agent, vector(Vx0,Vy0)),Z1),
  Vx1 #= 0,
  Vy1 #= 0,
  Ax1 #= 0,
  Ay1 #= 0,
  update(Z1,[acceleration(Agent, vector(Ax1,Ay1)), speed(Agent, vector(Vx1,Vy1))],
            [acceleration(Agent, vector(Ax0,Ay0)), speed(Agent, vector(Vx0,Vy0))],Z2)).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Initial State and HELPER  Functions %%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%



init(Z0) :- Z0 = [acceleration(agent1, vector(0,0)),
                  speed(agent1, vector(0,0)),
                  position(agent1, vector(0,0)),
                  position(ball1, vector(1,2)),
                  hasBall(agent1),
                  inGround(agent1)],
                  duplicate_free(Z0),
                  consistent(Z0).

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

closer(Sxa, Sya, Sxb, Syb, MaxDistance) :-
           calcDistance(Sxa, Sya, Sxb, Syb, A, B, C),
           MaxDistance >= A.

calcDistance(Sxa, Sya, Sxb, Syb, A, B, C) :-
                  B is (Sxb - Sxa),
                  C is (Syb - Sya),
                  B is (B * B),
                  C is (C * C),
                  A is (B + C).   %% raíz



           



