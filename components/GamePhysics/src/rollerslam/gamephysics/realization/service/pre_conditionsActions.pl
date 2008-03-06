:- ['position'].


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% The main purpose of this file is define all the Actions pre-conditions%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% A player need to stand-up when he is on the floor.
poss(stand_up(P),Z1):- holds(P@[inGround->true],Z1).

%% A player can only jump horizontally when he has a stamina higher than 10000.
poss(jumpH(P),Z1):- holds(P@[stamina->Stamina],Z1),
                        Stamina > 10000.

%% A player can only jump vertically when he is in some ramp:(west or east, in ramp or out ramp).
poss(jumpV(P),Z1):-  holds(P@[position->vector(Sx,Sy)],Z1),
                     holds(inTramp(vector(Sx,Sy)),Z1).

%% Release action does not have pre-condition. It just checks the whistlle perception.
poss(release(P),Z1).

%% A player can catch a ball only if he knows that nobody else has it.
poss(catch(P),Z1):- holds(Ball@[free->true], Z1),
                    holds(P@[position->vector(Sx, Sy)], Z1),
                    holds(Ball@[position->vector(Sbx, Sby)], Z1),
                    closer(Sx, Sy, Sbx, Sby, 500).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%% HOLDING BALL PRE-CONDITIONS %%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

poss(dropAndKick(P),Z1):- holds(P@[hasBall->true],Z1).

poss(kick(P),Z1):-  holds(P@[position->vector(Sx, Sy)], Z1),
                    holds(Ball@[position->vector(Sbx, Sby)], Z1),
                    closer(Sx, Sy, Sbx, Sby, 500).

poss(throw(P),Z1):- holds(P@[hasBall->true],Z1).

poss(spike(P),Z1):- holds(P@[position->vector(Sx, Sy)], Z1),
                    holds(Ball@[position->vector(Sbx, Sby)], Z1),
                    closer(Sx, Sy, Sbx, Sby, 500).

poss(volley(P),Z1):- holds(P@[position->vector(Sx, Sy)], Z1),
                     holds(Ball@[position->vector(Sbx, Sby)], Z1),
                     closer(Sx, Sy, Sbx, Sby, 500).

poss(screen(P),Z1).

poss(counterTackle(P),Z1):- holds(P@[hasBall->true],Z1).

poss(dunk(P),Z1):- holds(P@[onAir->true],Z1).

poss(tackle(P),Z1):- holds(P@[hasBall->true],Z1).
