:- ['fluent'].

%% closer(x1, y1, x2, y2, 7374)
%% near_ball() :-

%% holds(free(Ball), Z1)
%% free_ball() :-

%% holds(hasBall(Agent),Z1)
%% has_ball() :-

possible_position(Ax, Ay) :-
                      isOnInnerTrack(vector(Ax, Ay));
                      isOnField(vector(Ax, Ay));
                      isOnRamp(vector(Ax, Ay));
                      isOnEdge(vector(Ax, Ay));
                      isOnInnerTramp(vector(Ax, Ay));
                      isOnOuterTramp(vector(Ax, Ay)).

boundary_collision() :-

player_collision() :-

stand_up() :-

recieve_tackle() :-

target_has_ball() :-

