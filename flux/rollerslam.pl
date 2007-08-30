:- ['flux'].

%%%%%%%%%%%%%%
%% Actions %%%
%%%%%%%%%%%%%%

state_update(Z1,dash(Agent, vector(X,Y)),Z2,[]) :-
  holds(acceleration(Agent, vector(X0,Y0)),Z1),
  update(Z1,[acceleration(Agent, vector(X,Y))],[acceleration(Agent, vector(X0,Y0))],Z2).

%%%%%%%%%%%%%%%%%%%%
%% Ramifications %%%
%%%%%%%%%%%%%%%%%%%%

state_update(Z1,ramify,Z2,[]) :-
  collect_ramifiable_agents(Z1, Agents),
  ramify_agents(Z1, Agents, Z2). 
									 
ramify_agents(Z1, [], Z1).
ramify_agents(Z1, [A|R], Z3) :- ramify_agent(Z1,A,Z2), ramify_agents(Z2,R,Z3). 

ramify_agent(Z1, Agent, Z2) :-
  holds(acceleration(Agent, vector(Ax0,Ay0)),Z1),
  holds(speed(Agent, vector(Vx0,Vy0)),Z1),
  holds(position(Agent, vector(Sx0,Sy0)),Z1),
  Vx1 #= Vx0 + Ax0,
  Vy1 #= Vy0 + Ay0,
  Sx1 #= Sx0 + Vx0,
  Sy1 #= Sy0 + Vy0,

  update(Z1,[acceleration(Agent, vector(Ax0,Ay0)), speed(Agent, vector(Vx1,Vy1)), position(Agent, vector(Sx1,Sy1))],
            [acceleration(Agent, vector(Ax0,Ay0)), speed(Agent, vector(Vx0,Vy0)), position(Agent, vector(Sx0,Sy0))],Z2).


%%%%%%%%%%%%%%%%%%%%%%%%
%% Helper  Functions %%%
%%%%%%%%%%%%%%%%%%%%%%%%

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

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%% RAMIFICATIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

processRamifications(InitialState, FinalState) :- 
		runAction(InitialState, ramify, FinalState).
