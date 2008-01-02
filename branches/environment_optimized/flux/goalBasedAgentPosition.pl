:- ['position'].

poss(interpretPosition, _).

state_update(Z1, interpretPosition, Z2, []):-
       holds(Object, position(S), Z1),
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
         ),
         update(Z1,[isInPart(Agent, NewLocal)],[isInPart(Agent, X)],Z2).
