:- ['position'].

poss(interpretPosition, _).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% State Update responsible to update the Agents location%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

state_update(Z1, interpretPosition, Z2, []):-
       holds(position(Agent,S), Z1),
       holds(isInPart(Agent,X),Z1),
        (isOnField(S),
         NewLocal #='ON_FIELD'
         )
         ;
         (isOnInnerTrack(S),
         NewLocal #='INNER_TRACK'
         )
         ;
         (isOnRamp(S),
         NewLocal #= 'ON_RAMP'
         )
         ;
         (isOnEdge(S),
         NewLocal #= 'ON_EDGE'
         )
         ;
         (isOnInnerTramp(S),
         NewLocal #= 'INNER_TRAMP'
         )
         ;
         (isOnOuterTramp(S),
         NewLocal #= 'OUTER_TRAMP'
         )
         ;
         (NewLocal #= X
         ),
         update(Z1,[isInPart(Agent, NewLocal)],[isInPart(Agent, X)],Z2).
