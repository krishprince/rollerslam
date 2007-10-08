:- ['flux'].


%%%%%%%%%%%%%%%%%%%%
%% PRE-CONDITION %%%
%%%%%%%%%%%%%%%%%%%%

poss(throwA(player(X),_),Z1) :- holds(hasBall(player(X)), Z1),
                                   not_holds(inGround(player(X)),Z1).
poss(release(player(X)),Z1) :- holds(hasBall(player(X)),Z1),
                               not_holds(inGround(player(X)),Z1).
poss(dash(player(X)),Z1):- not_holds(inGround(player(X)),Z1).
poss(kick(player(X),_),Z1) :- holds(hasBall(player(X)), Z1),
                                             not_holds(inGround(player(X)),Z1).
                                             
poss(tackle(player(A),player(B), _),Z1) :- not_holds(inGround(player(A)),Z1),
                                                                 holds(hasBall(player(B)), Z1),
                                                                 not_holds(counterTackle(player(B)),Z1).
                                                                 
poss(counterTackle(player(X)),Z1) :- not_holds(inGround(player(X)),Z1).
poss(hit(player(X),_, _),Z1) :- not_holds(inGround(player(X)), Z1),
                                not_holds(hasBall(player(X)), Z1).
poss(catchA(player(X), _),Z1) :- not_holds(inGround(player(X)),Z1),
                                                     not holds(hasBall(player(_)), Z1).
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
  holds(maxAcceleration(player(A), Max),Z1),
  checkModule(New_Acc, Max, New_New_Acc),
  update(Z1,[acceleration(player(A), New_New_Acc)],[acceleration(player(A), Old_Acc)],Z2).

%%
%% Throw Action
%%

state_update(Z1,throwA(player(A),Error, vector(Ax, Ay), Strength, ResultModule),Z2,[]) :-
  (poss(throwA(player(A),Strength),Z1),
  holds(acceleration(ball, vector(X0, Y0)),Z1),
  validateError(Error, 0.15, ErrorR),
  moduleFlux(ErrorR,ResultModule, ResultX, ResultY),
  Num is ((1 + Strength) * 1.25),
  multVector(vector(Ax,Ay), Num , vector(XR, YR)),
  X is XR * ResultX,
  Y is YR * ResultY,
  update(Z1,[acceleration(ball, vector(X,Y))],[hasBall(player(A)),acceleration(ball, vector(X0, Y0))],Z2))
  ;
  (not poss(throwA(player(A),Strength),Z1), 
  Z2=Z1
  ).

%%
%% Release Action
%%

state_update(Z1,release(player(X)),Z2,[]) :-
  (poss(release(player(X)),Z1),
  update(Z1,[acceleration(ball, vector(0,0))],[hasBall(player(X))],Z2))
  ;
  (not poss(release(player(X)),Z1), 
  Z2=Z1
  ).

%%
%% Kick Action
%%

state_update(Z1,kick(player(A),Error, vector(Ax, Ay), Strength, ResultModule),Z2,[]) :-
  (poss(kick(player(A),Error),Z1),
  holds(acceleration(ball, vector(Sxb,Syb)),Z1),
  validateError(Error,0.3, ErrorR),
  moduleFlux(ErrorR, ResultModule,ResultX, ResultY),
  Num is ((1 + Strength) * 1.25),
  multVector(vector(Ax,Ay), Num , vector(XR, YR)),
  X is XR * ResultX,
  Y is YR * ResultY,
  update(Z1,[acceleration(ball, vector(X,Y))],[hasBall(player(A)),acceleration(ball, vector(Sxb, Syb))],Z2))
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

state_update(Z1,hit(player(A),Error, MaxDistance, vector(Ax, Ay), ResultModule),Z2,[]) :-
  (poss(hit(player(A),Error, MaxDistance),Z1),
  holds(position(player(A), vector(Sxa,Sya)),Z1),
  holds(position(ball, vector(Sxc,Syc)),Z1),
  holds(acceleration(ball, vector(Sxb,Syb)),Z1),
  closer(Sxa, Sya, Sxc, Syc, MaxDistance),
  validateError(Error, 0.3, ErrorR),
  moduleFlux(ErrorR,ResultModule, ResultX, ResultY),
  X is Ax * ResultX,
  Y is Ay * ResultY,
  update(Z1,[acceleration(ball, vector(X,Y))],[acceleration(ball, vector(Sxb,Syb))],Z2))
  ;
  (not poss(hit(player(A),Error, MaxDistance),Z1),
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
 
 
 state_update(Z1,lookBall,Z2,[scored(Team)]) :-
           (holds(score(Score, team(Team)),Z1),
           Score2 #= Score + 1,
           update(Z1,[score(Score2, team(Team))],[score(Score, team(Team))],Z2))
           ;
           (Z1=Z2).

%% End Action Group
