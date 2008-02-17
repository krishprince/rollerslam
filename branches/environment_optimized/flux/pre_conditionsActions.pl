:- ['fluent'].


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% The main purpose of this file is define all the Actions pre-conditions%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% A player need to stand-up when he is on the floor.
%% STAND-UP PRE-CONDITION
poss(stand_up(Agent),Z1):- holds(inGround(Agent),Z1).

%% A player can only jump horizontally when he has a stamina higher than 10000
%% JUMP HORIZONTAL PRE-CONDITION
poss(jumpH(Agent),Z1):- holds(stamina(Agent,Stamina),Z1),
                        Stamina > 10000.

%% A player can only jump vertically when he is in some ramp:(west or east, in ramp or out ramp).
%% JUMP VERTICAL PRE-CONDITION
poss(jumpV(Agent),Z1):-  holds(position(Agent, vector(Sx,Sy),Z1),
                         holds(inTramp(Agent, vector(Sx,Sy)),Z1).
                         
%% Release action does not have pre-condition. It just checks the whistlle perception.
poss(release(Agent),Z1).

%% A player can catch a ball only if he knows that nobody else has it.
poss(catch(Agent),Z1):- holds(free(Ball), Z1).
