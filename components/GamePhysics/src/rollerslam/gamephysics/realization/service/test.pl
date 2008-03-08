:- ['flux'].
:- ['ooflux'].
:- ['rollerslam'].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% SKATE
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

mainSkate(FinalState) :-

%% current state

 CurrentState = [
            agent1@[acceleration->vector(1,1)],
            agent1@[speed->vector(1,1)],
            agent1@[position->vector(1,1)],
            agent1@[stamina->1000]
                ],
%% action sequence

          Actions = [skate(agent1,1.2)],

          runSeriesOfActions(CurrentState, Actions, FinalState).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% KICK
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

mainKick(FinalState) :-

%% current state

 CurrentState = [
            agent1@[acceleration->vector(1,1)],
            agent1@[speed->vector(1,1)],
            agent1@[position->vector(1,1)],
            agent1@[stamina->1000],
            Ball@[acceleration->vector(1,1)],
            Ball@[position->vector(1,1)],
            Ball@[speed->vector(1,1)]
                ],
%% action sequence

          Actions = [kick(agent1,1.2)],

          runSeriesOfActions(CurrentState, Actions, FinalState).
