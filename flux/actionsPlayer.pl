:- ['position'].

%% *****************************************************************************************
%% *****************************    Pre-Conditions   ***************************************
%% *****************************************************************************************

%% Agents java layer.


%% *****************************************************************************************
%% ************************************   Actions   ****************************************
%% *****************************************************************************************

%% STAND UP ACTION

 state_update(Z1,standUp(player(X)),Z2,[]):-
           update(Z1,[],[inGround(player(X))],Z2).

%% JUMP ACTION

state_update(Z1, jump(player(X)), Z2, []):-
            update(Z1,[onAir(player(X))],[],Z2).


%% SKATE ACTION

state_update(Z1,skate(player(X), vector(Axa, Aya)), Z2, []) :-
            holds(acceleration(player(X),vector(Old)), Z1),
            holds(stamina(player(X),Strength), Z1),
            (
             holds(hasBall(player(X),BodyPart),Z1),
             NewStamina = Strength - (Srength * 1/10)
            )
            ;
            (
             NewStamina = Strength + (Srength * 1/100)
            ),
            update(Z1,[acceleration(player(X), vector(Axa, Aya)), stamina(player(X), NewStamina)],[acceleration(Agent,vector(Old)),stamina(player(X), Strength)],Z2).


%% RELEASE ACTION

state_update(Z1, release(player(X)), Z2, []):-
             update(Z1,[],[hasBall(player(X)), BodyPart],Z2).

%% CATCH ACTION

state_update(Z1, catchBall(player(X)), Z2, []):-
             holds(stamina(player(X),Strength),Z1),
             (
              Strength >= 10000,
              BodyPart #= 'FOOT'
              )
              ;
             (
              BodPart #= 'HAND'
             ),
             update(Z1,[hasBall(player(X), BodyPart)],[],Z2).

%% PASS ACTION

state_update(Z1, pass(player(X), player(Y)), Z2, []):-
                 holds(position(player(Y),vector(Sp)),Z1),
                 holds(position(ball,vector(Sb)),Z1),
                 update(Z1,[position(ball,vector(Sp))],[position(ball,vector(Sb))],Z2).

%% SHOOT ACTION

state_update(Z1, shoot(player(X)), Z2, []):-
                 holds(stamina(player(X),Strength),Z1),
                 (
                  holds(hasBall(player(X),BodyPart),Z1),
                   (
                    BodyPart = 'HAND',
                     ( Strength>5000,execute(dropAndKick(player(X)),Z1,Z2))
                     ;
                     ( execute(throw(player(X)),Z1,Z2))
                   )
                   ;
                   (
                    BodyPart = 'FOOT',
                    execute(kick(player(X)),Z1,Z2)
                   )
                  )
                  ;
                 (
                  ( Strength>10000,execute(voleyKick(player(X)),Z1,Z2))
                   ;
                  ( execute(spike(player(X)),Z1,Z2))
                 ).

%% SCREEN ACTION

state_update(Z1, screen(player(X),player(Y)), Z2,[]):-
            holds(stamina(player(X),StrengthA), Z1),
            holds(stamina(player(Y),StrengtB),Z1),
            NewStamina #= StrengthA - (StrengthA * (1/10)),
            NewStaminaB #=StrengthB - (StrengthB * (1/10)),
            (StrengthA > StrengthB,
              update(Z1,[inGround(player(Y)),speed(player(X),vector(0,0)), stamina(player(X),NewStamina)],[], Z2));
            (update(Z1,[speed(player(X),vector(0,0)), speed(player(Y),vector(0,0)),stamina(player(X),NewStamina), stamina(player(Y),NewStaminaB)],[],Z2)).

%% COUNTERTACKLE ACTION

state_update(Z1, counterTackle(player(X)), Z2, []):-
            hols(stamina(player(X),Strength), Z1),
            (Strength >= 10000,
             NewStamina #= Strength - (Strength * (1/10)),
              update(Z1, [counterTackle(player(X)), stamina(player(X), NewStamina)], [stamina(player(X), Strength)], Z2));
              (Z2=Z1).

%% TACKLE ACTION

state_update(Z1, tackle(player(X),player(Y)), Z2, []):-
            holds(stamina(player(X),StrengthA), Z1),
            holds(stamina(player(Y),StrengthB),Z1),
            holds(position(player(X),vector(Sxa,Sya)), Z1),
            holds(position(player(Y),vector(Sxb,Syb)), Z1),
            NewStaminaA #= StrengthA - (StrengthA * (1/10)),
            NewStaminaB #= StrengthB - (StrengthB * (1/10)),
             ( 
               holds(counterTackle(player(Y)),Z1), 
               Z1=Z2
             )
             ;
             (
               isOnField(vector(Sxa,Sya)),
               isOnField(vector(Sxb,Syb)),
               (
                StrengthA > StrengthB,
                update(Z1,[stamina(player(X),NewStaminaA),stamina(player(Y),NewStaminaB),inGround(player(Y)),speed(player(Y),vector(0,0))],[], Z2)
                )
                ;
               (
               update(Z1,[stamina(player(X),NewStaminaA),stamina(player(Y),NewStaminaB),speed(player(X),vector(0,0)),speed(player(Y),vector(0,0))],[],Z2)
               )
             )
             ;
             (
              isOnField(vector(Sxa,Sya)),
              isOnInnerTrack(vector(Sxb,Syb)),
              update(Z1,[stamina(player(X),NewStaminaA),stamina(player(Y),NewStaminaB),inGround(player(B))],[],Z2)
             )
             ;
             (
              isOnInnerTrack(vector(Sxa,Sya)),
              isOnField(vector(Sxb,Syb)),
              update(Z1,[stamina(player(X),NewStaminaA),stamina(player(Y),NewStaminaB)],[],Z2)
             )
             ;
             (
              isOnInnerTrack(vector(Sxa,Sya)),
              isOnInnerTrack(vector(Sxb,Syb)),
              (
                (
                 (Sya > Syb, Sya < 0)
                ;
                 (Sya<Syb, Sya>0)
                ),
                 update(Z1,[stamina(player(X),NewStaminaA),stamina(player(Y),NewStaminaB),inGround(player(Y))],[],Z2)
              )
              ;
              (
               update(Z1,[stamina(player(X),NewStaminaA),stamina(player(Y),NewStaminaB),inGround(player(X)), inGround(player(Y))],[],Z2)
               )
             ).



%% DROP AND KICK ACTION

state_update(Z1, dropAndKick(player(X)), Z2, []):-
             holds(stamina(player(X),Strength), Z1),
             holds(position(ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength,
             Yb #= Syb * Strength,
             NewStamina #= Strength - (Strength * (1/10)),
             update(Z1,[position(ball, vector(Xb, Yb)), stamina(player(X), NewStamina)],[hasBall(player(X)),position(ball, vector(Sxb, Syb))],Z2).


%% KICK ACTION

state_update(Z1,kick(player(X)), Z2,[]):-
             holds(stamina(player(X),Strength), Z1),
             holds(position(ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength,
             Yb #= Syb * Strength,
             NewStamina #= Strength - (Strength * (1/10)),
             update(Z1,[position(ball, vector(Xb, Yb)), stamina(player(X), NewStamina)],[position(ball, vector(Sxb, Syb))],Z2).

%% THROW ACTION
state_update(Z1, throw(player(X)), Z2, []):-
             holds(stamina(player(X),Strength), Z1),
             holds(position(ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength * (1/2),
             Yb #= Syb * Strength * (1/2),
             NewStamina #= Strength - (Strength * (1/5)),
             update(Z1,[position(ball, vector(Xb, Yb)), stamina(player(X), NewStamina)],[hasBall(player(X)),position(ball, vector(Sxb, Syb))],Z2).

%% SPIKE ACTION

state_update(Z1,spike(player(X)), Z2,[]):-
             holds(stamina(player(X),Strength), Z1),
             holds(position(ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength,
             Yb #= Syb * Strength,
             NewStamina #= Strength - (Strength * (1/10)),
             update(Z1,[position(ball, vector(Xb, Yb)), stamina(player(X), NewStamina)],[position(ball, vector(Sxb, Syb))],Z2).

%% VOLEY KICK ACTION

state_update(Z1,spike(player(X)), Z2,[]):-
             holds(stamina(player(X),Strength), Z1),
             holds(position(ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength * (1/2),
             Yb #= Syb * Strength * (1/2),
             NewStamina #= Strength - (Strength * (1/5)),
             update(Z1,[position(ball, vector(Xb, Yb)), stamina(player(X), NewStamina)],[position(ball, vector(Sxb, Syb))],Z2).

%% TOUCHDOWN ACTION

%% state_update(Z1, touchDown(), Z2, []).

