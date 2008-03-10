:- ['pre_conditionsActions'].
:- ['updateModel'].

maxAcceleration(600).
maxSpeed(1000).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%% NOT HOLDING BALL ACTIONS %%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% STAND UP ACTION
gamePhysicsProcessAction(Z1,standUp(P),Z2) :-
           poss(stand_up(P),Z1),
           update(Z1,[],[P@[inGround->true]],Z2).

%% By considering that the player may execute two kind of jump actions, a vertical one and a horizontal one.

%% JUMP HORIZONTAL ACTION
gamePhysicsProcessAction(Z1, jumpH(P), Z2) :-
            poss(jumpH(P),Z1),
            holds(P@[speed->Spd], Z1),
            jumpH(Spd,Z1,vector(NewSx,NewSy)),
            update(Z1,[P@[position->vector(NewSx,NewSy)]],[],Z2).


%% JUMP VERTICAL ACTION
gamePhysicsProcessAction(Z1, jumpV(P), Z2) :-
            poss(jumpV(P),Z1),
            holds(P@[position->Pos], Z1),
            jumpV(Pos,Z1,vector(NewSx,NewSy)),
            update(Z1,[P@[position->vector(NewSx,NewSy)]],[],Z2).


%% DASH ACTION

gamePhysicsProcessAction(Z1,dash(P, Dir), Z2) :-
            poss(dash(P),Z1),
            holds(P@[acceleration->Acc], Z1),
            maxAcceleration(MA),
	        limitModulo(Dir, MA, Dir1),
	        update(Z1, [P@[acceleration->Dir1]], [P@[acceleration->Acc]], Z2).

%% RELEASE ACTION
gamePhysicsProcessAction(Z1, release(P), Z2):-
             poss(release(P),Z1),
             update(Z1,[Ball@[free->true]],[P@[hasBall->false]],Z2).


%% CATCH ACTION
gamePhysicsProcessAction(Z1, catch(P), Z2):-
            poss(catch(P),Z1),
            update(Z1,[P@[hasBall->true]],[Ball@[free->false]],Z2).
            
%% SCREEN ACTION
gamePhysicsProcessAction(Z1, screen(P,Pb), Z2):-
            holds(P@[stamina->StrengthA], Z1),
            holds(Pb@[stamina->StrengthB],Z1),
            (StrengthA > StrengthB,
              update(Z1,[Pb@[inGround->true],P@[acceleration->vector(0,0)], P@[speed->vector(0,0)]],[], Z2)
              );
            (
              update(Z1,[P@[acceleration->vector(0,0)], Pb@[acceleration->vector(0,0)], P@[speed->vector(0,0)], Pb@[speed->vector(0,0)]],[],Z2)
              ).


%% COUNTERTACKLE ACTION
gamePhysicsProcessAction(Z1, counterTackle(P), Z2):-
            poss(counterTackle(P),Z1),
            hols(P@[stamina->Strength], Z1),
            (Strength >= 10000,
             NewStamina #= Strength - (Strength * (1/10)),
              update(Z1, [P@[counterTackle->true],P@[stamina->NewStamina]], [P@[stamina->Strength]], Z2));
              (Z2=Z1).


%% TACKLE ACTION
gamePhysicsProcessAction(Z1, tackle(P,Pb), Z2):-
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
gamePhysicsProcessAction(Z1, dunk(P,Dir), Z2):-
               poss(dunk(P),Z1),
               holds(Ball@[position->OldPos],Z1),
               update(Z1,[Ball@[position->Dir]],[Ball@[position->OldPos], P@[hasBall->false]],Z2).            


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%% HOLDING BALL ACTIONS %%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% DROP AND KICK ACTION
gamePhysicsProcessAction(Z1, dropAndKick(P,Dir), Z2):-
             poss(dropAndKick(P),Z1),
             holds(P@[stamina->Strength], Z1),
             moduleAcc(Strength, Dir, DirPlayer),
             maxAcceleration(MA),
	         limitModulo(DirPlayer, MA, Dir1),
             NewStamina #= Strength - (Strength * (1/10)),
             update(Z1,[Ball@[acceleration->Dir1], 
                        P@[stamina->NewStamina], 
                        Ball@[free->true]],
                       [P@[hasBall->false]],Z2).


%% KICK ACTION
gamePhysicsProcessAction(Z1,kick(P,Dir), Z2):-
             holds(P@[stamina->Strength], Z1),
             poss(kick(P),Z1),
             moduleAcc(Strength, Dir, DirPlayer),
             maxAcceleration(MA),
	         limitModulo(DirPlayer, MA, Dir1),
             NewStamina is Strength - (Strength/10),
             update(Z1,
                    [Ball@[acceleration->Dir1],
                     P@[stamina->NewStamina]],
                    [P@[hasBall->false],
                     P@[stamina->Strength],
                     Ball@[acceleration->OldAcc],
                     Ball@[position->vector(Sbx,Sby)],
                     Ball@[speed->OldSpd]],
                    Z2).

%% THROW ACTION
gamePhysicsProcessAction(Z1, throw(P), Z2):-
             poss(throw(P,Error,vector(Vx,Vy)),Z1),
             holds(P@[stamina->Strength], Z1),
             holds(Ball@[position->OldPos],Z1),
             holds(Ball@[speed->OldSpd],Z1),
             moduleAcc(Error,Strength, vector(Vx,Vy), vector(NewAx,NewAy)),
             NewStamina #= Strength - (Strength * (1/5)),
             update(Z1,[Ball@[acceleration->vector(NewAx, NewAy)],
                        Ball@[speed->vector(NewVx,NewVy)],
                        Ball@[position->vector(NewSx,NewSy)],
                        P@[stamina->NewStamina],
                        Ball@[free->true]],
                       [P@[hasBall->false]],Z2).


%% VOLLEY ACTION
gamePhysicsProcessAction(Z1,volley(P,Error, vector(Vx,Vy)), Z2):-
             holds(P@[stamina->Strength], Z1),
             holds(Ball@[position->OldPos],Z1),
             holds(Ball@[speed->OldSpd],Z1),
             poss(volley(P,OldPos),Z1),
             moduleAcc(Error,Strength, vector(Vx,Vy), vector(NewAx,NewAy)),
             NewStamina #= Strength - (Strength * (1/10)),
             update(Z1,[Ball@[acceleration->vector(NewAx, NewAy)],
                        Ball@[speed->vector(NewVx,NewVy)],
                        Ball@[position->vector(NewSx,NewSy)],
                        P@[stamina->NewStamina]],[],Z2).


%% SPIKE KICK ACTION
gamePhysicsProcessAction(Z1,spike(P, Error, vector(Vx,Vy)), Z2):-
             holds(P@[stamina->Strength], Z1),
             holds(Ball@[position->OldPos],Z1),
             holds(Ball@[speed->OldSpd],Z1),
             poss(spike(P,OldPos),Z1),
             moduleAcc(Error,Strength, vector(Vx,Vy), vector(NewAx,NewAy)),
             NewStamina #= Strength - (Strength * (1/5)),
             update(Z1,[Ball@[acceleration->vector(Ax, Ay)],
                        Ball@[speed->vector(NewVx,NewVy)],
                        Ball@[position->vector(NewSx,NewSy)],
                        P@[stamina->NewStamina]],[],Z2).






