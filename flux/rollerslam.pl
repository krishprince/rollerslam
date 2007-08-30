:- ['flux'].

state_update(Z1,dash(Agent, vector(X,Y)),Z2,[]) :-
  holds(acceleration(Agent, vector(X0,Y0)),Z1),
  update(Z1,[acceleration(Agent, vector(X,Y))],[acceleration(Agent, vector(X0,Y0))],Z2).

state_update(Z1,ramify(Agent),Z2,[]) :-
  holds(acceleration(Agent, vector(Ax0,Ay0)),Z1),
  holds(speed(Agent, vector(Vx0,Vy0)),Z1),
  holds(position(Agent, vector(Sx0,Sy0)),Z1),
  Vx1 #= Vx0 + Ax0,
  Vy1 #= Vy0 + Ay0,
  Sx1 #= Sx0 + Vx0,
  Sy1 #= Sy0 + Vy0,

  update(Z1,[acceleration(Agent, vector(Ax0,Ay0)), speed(Agent, vector(Vx1,Vy1)), position(Agent, vector(Sx1,Sy1))],
            [acceleration(Agent, vector(Ax0,Ay0)), speed(Agent, vector(Vx0,Vy0)), position(Agent, vector(Sx0,Sy0))],Z2).

init(Z0) :- Z0 = [acceleration(agent1, vector(0,0)),
                  speed(agent1, vector(0,0)),
                  position(agent1, vector(0,0))],
            	  duplicate_free(Z0).

runAction(CurrentState, Action, NextState) :- state_update(CurrentState, Action, NextState,[]).

runSeriesOfActions(CurrentState, [], CurrentState).
runSeriesOfActions(CurrentState, [C|L], FinalState) :- runAction(CurrentState, C, NextState),
                                                       runSeriesOfActions(NextState, L, FinalState).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%% SAMPLE 0 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

main0(FinalState) :-

%% current state

           CurrentState = [acceleration(agent1, vector(0,0)),
                           speed(agent1, vector(0,0)),
                           position(agent1, vector(0,0))],

%% current action

           Action = dash(agent1, vector(1,3)),

           runAction(CurrentState, Action, FinalState).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%% SAMPLE 1 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

main1(FinalState) :-

%% current state

           CurrentState = [acceleration(agent1, vector(0,0)),
                           speed(agent1, vector(0,0)),
                           position(agent1, vector(0,0))],

%% action sequence

           Actions = [dash(agent1, vector(1,3)), ramify, ramify, ramify],

           runSeriesOfActions(CurrentState, Actions, FinalState).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%% RAMIFICATIONS %%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

processRamifications(InitialState, [], InitialState).
processRamifications(InitialState, [RamifiableObject|Others], FinalState) :-
           runAction(InitialState, ramify(RamifiableObject), IntermediaryState),
           processRamifications(IntermediaryState, Others, FinalState).
