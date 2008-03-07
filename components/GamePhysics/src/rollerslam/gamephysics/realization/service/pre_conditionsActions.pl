:- ['position'].


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% The main purpose of this file is define all the Actions pre-conditions%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% A player need to stand-up when he is on the floor.
poss(stand_up(P),Z):- holds(P@[inGround->true],Z).

%% A player can only jump horizontally when he has a stamina higher than 10000.
poss(jumpH(P),Z):- holds(P@[stamina->Stamina],Z),
                        Stamina > 10000.

%% A player can only jump vertically when he is in some ramp:(west or east, in ramp or out ramp).
poss(jumpV(P),Z):-  holds(P@[position->vector(Sx,Sy)],Z),
                     holds(inTramp(vector(Sx,Sy)),Z).

%% Release action does not have pre-condition. It just checks the whistlle perception.
poss(release(P),Z).

%% A player can catch a ball only if he knows that nobody else has it.
poss(catch(P),Z):- holds(Ball@[free->true], Z),
                    holds(P@[position->vector(Sx, Sy)], Z),
                    holds(Ball@[position->vector(Sbx, Sby)], Z),
                    closer(Sx, Sy, Sbx, Sby, 500).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%% HOLDING BALL PRE-CONDITIONS %%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

poss(dropAndKick(P),Z):- holds(P@[hasBall->true],Z).

poss(kick(P),Z):-  holds(P@[position->vector(Sx, Sy)], Z),
                   holds(Ball@[position->vector(Sbx, Sby)], Z),
                   closer(Sx, Sy, Sbx, Sby, 500).

poss(throw(P),Z):- holds(P@[hasBall->true],Z).

poss(spike(P),Z):- holds(P@[position->vector(Sx, Sy)], Z),
                   holds(Ball@[position->vector(Sbx, Sby)], Z),
                   closer(Sx, Sy, Sbx, Sby, 500).

poss(volley(P),Z):- holds(P@[position->vector(Sx, Sy)], Z),
                    holds(Ball@[position->vector(Sbx, Sby)], Z),
                    closer(Sx, Sy, Sbx, Sby, 500).

poss(screen(P),Z).

poss(counterTackle(P),Z):- holds(P@[hasBall->true],Z).

poss(dunk(P),Z):- holds(P@[onAir->true],Z).

poss(tackle(P),Z):- holds(P@[hasBall->true],Z).

poss(skate(P),Z):- holds(P@[onAir->false],Z).
