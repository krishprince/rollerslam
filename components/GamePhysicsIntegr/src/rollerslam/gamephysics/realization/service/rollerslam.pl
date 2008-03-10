:- ['pre_conditionsActions'].
:- ['updateModel'].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Follow is the list of possible actions   %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% STAND UP ACTION
state_update(Z1,standUp(P),Z2,[]) :-
           poss(stand_up(P),Z1),
           update(Z1,[],[P@[inGround->true]],Z2).

%% By considering that the player may execute two kind of jump actions, a vertical one and a horizontal one.

%% JUMP HORIZONTAL ACTION
state_update(Z1, jumpH(P), Z2, []) :-
            poss(jumpH(P),Z1),
            holds(P@[speed->Spd], Z1),
            jumpH(Spd,Z1,vector(NewSx,NewSy)),
            update(Z1,[P@[position->vector(NewSx,NewSy)]],[],Z2).


%% JUMP VERTICAL ACTION
state_update(Z1, jumpV(P), Z2, []) :-
            poss(jumpV(P),Z1),
            holds(P@[position->Pos], Z1),
            jumpV(Pos,Z1,vector(NewSx,NewSy)),
            update(Z1,[P@[position->vector(NewSx,NewSy)]],[],Z2).


%% SKATE ACTION

state_update(Z1,skate(P, Error), Z2, []) :-
            poss(skate(P),Z1),
            holds(P@[acceleration->vector(Ax0,Ay0)], Z1),
            holds(P@[speed->vector(Vx0,Vy0)],Z1),
            holds(P@[position->vector(Sx0,Sy0)],Z1),
            holds(P@[stamina->Strength], Z1),
            moduleAcc(Error,Strength, vector(Ax0,Ay0), vector(NewAx,NewAy)),
            speedPositionRamification(P, NewAx, NewAy, NewVx, NewVy, NewSx, NewSy, Z1),
            NewStamina #= Strength - (Strength/100),
            update(Z1,
                   [P@[acceleration->vector(NewAx, NewAy)],
                    P@[speed->vector(NewVx,NewVy)],
                    P@[position->vector(NewSx,NewSy)],
                    P@[stamina->NewStamina]],
                   [P@[acceleration->vector(Ax0, Ay0)],
                    P@[speed->vector(Vx0,Vy0)],
                    P@[position->vector(Sx0,Sy0)],
                    P@[stamina->Strength]],
                   Z2).

%% Actions with ball

%% RELEASE ACTION
state_update(Z1, release(P), Z2, [Whistle]):-
             poss(release(P),Z1),
             Whistle = true,
             update(Z1,[Ball@[free->true]],[P@[hasBall->false]],Z2).


%% CATCH ACTION
state_update(Z1, catch(P), Z2, []):-
            poss(catch(P),Z1),
            update(Z1,[P@[hasBall->true]],[Ball@[free->false]],Z2).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%% HOLDING BALL ACTIONS %%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% DROP AND KICK ACTION
state_update(Z1, dropAndKick(P,Error,vector(Vx,Vy)), Z3, []):-
             poss(dropAndKick(P),Z1),
             holds(P@[stamina->Strength], Z1),
             holds(Ball@[position->OldPos],Z1),
             holds(Ball@[speed->OldSpd],Z1),
             moduleAcc(Error,Strength, vector(Vx,Vy), vector(Ax,Ay)),
             NewStamina #= Strength - (Strength * (1/10)),
             update(Z1,[Ball@[acceleration->vector(Ax, Ay)], P@[stamina->NewStamina], Ball@[free->true]],[P@[hasBall->false]],Z2),
             ramify(Z2,[Ball@[acceleration->vector(Ax, Ay)],Ball@[position->OldPos],Ball@[speed->OldSpd]],[],Z3).


%% KICK ACTION
state_update(Z1,kick(P,Error), Z2,[]):-
             holds(P@[stamina->Strength], Z1),
             holds(P@[acceleration->vector(Ax,Ay)], Z1),
             holds(Ball@[acceleration->OldAcc],Z1),
             holds(Ball@[position->vector(Sbx,Sby)],Z1),
             holds(Ball@[speed->OldSpd],Z1),
             poss(kick(P,vector(Sbx,Sby)),Z1),
             moduleAcc(Error, Strength, vector(Ax,Ay), vector(NewPAx,NewPAy)),
             speedPositionRamification(Ball, NewPAx, NewPAy, NewBVx, NewBVy, NewBSx, NewBSy, Z1),
             NewStamina is Strength - (Strength/10),
             update(Z1,
                    [Ball@[acceleration->vector(NewPAx,NewPAy)],
                     Ball@[speed->vector(NewBSx,NewBSy)],
                     Ball@[position->vector(NewBVx,NewBVy)],
                     P@[stamina->NewStamina]],
                    [P@[hasBall->false],
                     P@[stamina->Strength],
                     Ball@[acceleration->OldAcc],
                     Ball@[position->vector(Sbx,Sby)],
                     Ball@[speed->OldSpd]],
                    Z2).

%% THROW ACTION
state_update(Z1, throw(P), Z3, []):-
             poss(throw(P,Error,vector(Vx,Vy)),Z1),
             holds(P@[stamina->Strength], Z1),
             holds(Ball@[position->OldPos],Z1),
             holds(Ball@[speed->OldSpd],Z1),
             moduleAcc(Error,Strength, vector(Vx,Vy), vector(Ax,Ay)),
             NewStamina #= Strength - (Strength * (1/5)),
             update(Z1,[Ball@[acceleration->vector(Ax, Ay)], P@[stamina->NewStamina],Ball@[free->true]],[P@[hasBall->false]],Z2),
             ramify(Z2,[Ball@[acceleration->vector(Ax, Ay)],Ball@[position->OldPos],Ball@[speed->OldSpd]],[],Z3).


%% VOLLEY ACTION
state_update(Z1,volley(P,Error, vector(Vx,Vy)), Z3,[]):-
             holds(P@[stamina->Strength], Z1),
             holds(Ball@[position->OldPos],Z1),
             holds(Ball@[speed->OldSpd],Z1),
             poss(volley(P,OldPos),Z1),
             moduleAcc(Error,Strength, vector(Vx,Vy), vector(Ax,Ay)),
             NewStamina #= Strength - (Strength * (1/10)),
             update(Z1,[Ball@[acceleration->vector(Ax, Ay)], P@[stamina->NewStamina]],[],Z2),
             ramify(Z2,[Ball@[acceleration->vector(Ax, Ay)],Ball@[position->OldPos],Ball@[speed->OldSpd]],[],Z3).


%% SPIKE KICK ACTION
state_update(Z1,spike(P, Error, vector(Vx,Vy)), Z3,[]):-
             holds(P@[stamina->Strength], Z1),
             holds(Ball@[position->OldPos],Z1),
             holds(Ball@[speed->OldSpd],Z1),
             poss(spike(P,OldPos),Z1),
             moduleAcc(Error,Strength, vector(Vx,Vy), vector(Ax,Ay)),
             NewStamina #= Strength - (Strength * (1/5)),
             update(Z1,[Ball@[acceleration->vector(Ax, Ay)], P@[stamina->NewStamina]],[],Z2),
             ramify(Z2,[Ball@[acceleration->vector(Ax, Ay)],Ball@[position->OldPos],Ball@[speed->OldSpd]],[],Z3).



%% SCREEN ACTION
state_update(Z1, screen(P,Pb), Z3,[]):-
            holds(P@[stamina->StrengthA], Z1),
            holds(Pb@[stamina->StrengthB],Z1),
            (StrengthA > StrengthB,
              update(Z1,[Pb@[inGround->true],P@[acceleration->vector(0,0)]],[], Z2),
              ramify(Z2,[P@[acceleration->vector(0,0)]],[],Z3)
              );
            (
              update(Z1,[P@[acceleration->vector(0,0)], Pb@[acceleration->vector(0,0)]],[],Z2),
              ramify(Z2,[P@[acceleration->vector(0,0)], Pb@[acceleration->vector(0,0)]],[],Z3)
              ).


%% COUNTERTACKLE ACTION
state_update(Z1, counterTackle(P), Z2, []):-
            poss(counterTackle(P),Z1),
            hols(P@[stamina->Strength], Z1),
            (Strength >= 10000,
             NewStamina #= Strength - (Strength * (1/10)),
              update(Z1, [P@[counterTackle->true],P@[stamina->NewStamina]], [P@[stamina->Strength]], Z2));
              (Z2=Z1).


%% TACKLE ACTION
state_update(Z1, tackle(P,Pb), Z2, []):-
            poss(tackle(P),Z1),
            holds(P@[stamina->StrengthA], Z1),
            holds(Pb@[stamina->StrengthB], Z1),
            holds(P@[position->PosA],Z1),
            holds(Pb@[position->PosB],Z1),
             ( isOnField(PosA),
               isOnField(PosB),
               (StrengthA > StrengthB,
                update(Z1,[Pb@[inGround->true],P@[acceleration->vector(0,0)], P@[speed->vector(0,0)]],[], Z2)
                );
              (
                update(Z1,[P@[acceleration->vector(0,0)], P@[speed->vector(0,0)],Pb@[acceleration->vector(0,0)], Pb@[speed->vector(0,0)]],[],Z2)
              )
             )
             ;
             (
             isOnField(PosA),
             isOnInTrack(PosB),
             update(Z1,[Pb@[inGround->true]],[],Z2)
             )
             ;
             (
             isOnInTrack(PosA),
             isOnField(PosB),
             update(Z1,[],[],Z2)
             )
             ;
             (
             isOnInTrack(PosA),
             isOnInTrack(PosB),
             (((Sya > Syb),(Sya < 0));((Sya<Syb),(Sya>0)), update(Z1,[Pb@[inGround->true]],[],Z2));
             (update(Z1,[P@[inGround->true], Pb@[inGround->true]],[],Z2))
             )
             ;
             (Z1=Z2).

%% DUNK ACTION
state_update(Z1, dunk(P,Dir), Z2, []):-
               poss(dunk(P),Z1),
               holds(Ball@[position->OldPos],Z1),
               update(Z1,[Ball@[position->Dir]],[Ball@[position->OldPos], P@[hasBall->false]],Z2).


