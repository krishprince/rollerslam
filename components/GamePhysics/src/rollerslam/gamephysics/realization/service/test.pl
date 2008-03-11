:- ['flux'].
:- ['ooflux'].
:- ['rollerslam'].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% SKATE (Yes)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

mainSkate(FinalState) :-

%% current state

 CurrentState = [
            agent1@[onAir->false],
            agent1@[acceleration->vector(1,1)],
            agent1@[speed->vector(1,1)],
            agent1@[position->vector(1,1)],
            agent1@[stamina->1000]
                ],
%% action sequence

          Actions = [skate(agent1,1.2)],

          runSeriesOfActions(CurrentState, Actions, FinalState).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% KICK Near Ball (Yes)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

mainKickNear(FinalState) :-

%% current state

 CurrentState = [
            agent1@[acceleration->vector(1,1)],
            agent1@[speed->vector(1,1)],
            agent1@[position->vector(1,1)],
            agent1@[stamina->1000],
            ball@[acceleration->vector(1,1)],
            ball@[position->vector(1,1)],
            ball@[speed->vector(1,1)]
                ],
%% action sequence

          Actions = [kick(agent1,1.2)],

          runSeriesOfActions(CurrentState, Actions, FinalState).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% KICK Not Near Ball (No)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

mainKickNotNear(FinalState) :-

%% current state

 CurrentState = [
            agent1@[acceleration->vector(1,1)],
            agent1@[speed->vector(1,1)],
            agent1@[position->vector(1,1)],
            agent1@[stamina->1000],
            ball@[acceleration->vector(1,1)],
            ball@[position->vector(1000,1000)],
            ball@[speed->vector(1,1)]
                ],
%% action sequence

          Actions = [kick(agent1,1.2)],

          runSeriesOfActions(CurrentState, Actions, FinalState).
