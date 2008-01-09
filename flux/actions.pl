:- ['fluent'].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% After updating the internal state and his goal is time for the agent execute the action.%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Follow is the list of possible actions%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% STAND UP ACTION

 state_update(Z1,standUp(Agent),Z2,[]) :-
           update(Z1,[],[inGround(Agent)],Z2).
           
%% JUMP ACTION

state_update(Z1, jump(Agent), Z2, []) :-
            update(Z1,[onAir(Agent)],[],Z2).


%% SKATE ACTION

state_update(Z1,skate(Agent, vector(Axa, Aya)), Z2, []) :-
            holds(acceleration(Agent,vector(Old)), Z1),
            (
             holds(hasBall(Agent),Z1),
             holds(stamina(Agent,Strength), Z1),
             NewStamina = Strength + (Srength * 1/100),
             update(Z1,[acceleration(Agent, vector(Axa, Aya)), stamina(Agent, NewStamina)],[acceleration(Agent,vector(Old))],Z2)
            )
            ;
            (
            update(Z1,[acceleration(Agent, vector(Axa, Aya))],[acceleration(Agent,vector(Old))],Z2)
            ).


%% RELEASE ACTION

state_update(Z1, release(Agent), Z2, []):-
             update(Z1,[],[hasBall(Agent)],Z2).

%% CATCH ACTION

state_update(Z1, catch(Agent), Z2, []):-
            update(Z1,[hasBall(Agent)],[],Z2).

%% DROP AND KICK ACTION

state_update(Z1, dropAndKick(Agent), Z2, []):-
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength,
             Yb #= Syb * Strength,
             NewStamina #= Strength * (1/10),
             update(Z1,[position(Ball, vector(Xb, Yb)), stamina(Agent, NewStamina)],[hasBall(Agent),position(Ball, vector(Sxb, Syb))],Z2).


%% KICK ACTION

state_update(Z1,kick(Agent), Z2,[]):-
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength,
             Yb #= Syb * Strength,
             NewStamina #= Strength * (1/10),
             update(Z1,[position(Ball, vector(Xb, Yb)), stamina(Agent, NewStamina)],[position(Ball, vector(Sxb, Syb))],Z2).

%% THROW ACTION
state_update(Z1, throw(Agent), Z2, []):-
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength * (1/2),
             Yb #= Syb * Strength * (1/2),
             NewStamina #= Strength * (1/5),
             update(Z1,[position(Ball, vector(Xb, Yb)), stamina(Agent, NewStamina)],[hasBall(Agent),position(Ball, vector(Sxb, Syb))],Z2).

%% SPIKE ACTION

state_update(Z1,spike(Agent), Z2,[]):-
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength,
             Yb #= Syb * Strength,
             NewStamina #= Strength * (1/10),
             update(Z1,[position(Ball, vector(Xb, Yb)), stamina(Agent, NewStamina)],[position(Ball, vector(Sxb, Syb))],Z2).

%% VOLEY KICK ACTION

state_update(Z1,spike(Agent), Z2,[]):-
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength * (1/2),
             Yb #= Syb * Strength * (1/2),
             NewStamina #= Strength * (1/5),
             update(Z1,[position(Ball, vector(Xb, Yb)), stamina(Agent, NewStamina)],[position(Ball, vector(Sxb, Syb))],Z2).

%% TOUCHDOWN ACTION

%% state_update(Z1, touchDown(), Z2, []).

%% PASS ACTION

state_update(Z1, pass(Agent), Z2, []):-
                 holds(pass(Agent,AgentB),Z1),
                 holds(position(AgentB,vector(S)),Z1),
                 holds(position(Ball,vector(Sb)),Z1),
                 update(Z1,[position(Ball,vector(S))],[position(Ball,vector(Sb)),pass(Agent,AgentB)],Z2).

%% SHOOT ACTION

state_update(Z1, shoot(Agent), Z2, []):-
                 holds(stamina(Agent,Strength),Z1),
                 (
                 holds(hasBall(Agent,HAND),Z1),
                 (
                  (
                   (
                    Strength>1000,
                    execute(dropAndKick(Agent),Z1,Z2)
                    )
                    ;
                   (
                    execute(throw(Agent),Z1,Z2)
                   )
                  )
                 )
                 ;
                 (
                  holds(hasBall(Agent,FOOT),Z1),
                  execute(kick(Agent),Z1,Z2)
                 )
                 ;
                 (
                  (
                   Strength>1000,
                   runAction(Z1,voleyKick(Agent),Z2)
                   )
                   ;
                   (
                   runAction(Z1,spike(Agent),Z2)
                   )
                 )).

%% SCREEN ACTION

state_update(Z1, screen(Agent), Z2,[]):-
            holds(screen(Agent, AgentB),Z1),
            holds(stamina(Agent,StrengthA), Z1),
            holds(stamina(Agent,StrengtB),Z1),
            NewStamina #= StrengthA * (1/10),
            (StrengthA > StrengthB,
              update(Z1,[inGround(AgentB),speed(Agent,vector(0,0)), stamina(Agent,NewStamina)],[screen(Agent,AgentB)], Z2));
            (update(Z1,[speed(Agent,vector(0,0)), speed(AgentB,vector(0,0)),stamina(Agent,NewStamina)],[screen(Agent,AgentB)],Z2)).

%% COUNTERTACKLE ACTION

state_update(Z1, counterTackle(Agent), Z2, []):-
            hols(stamina(Agent,Strength), Z1),
            (Strength >= 10000,
             NewStamina #= Strength * (1/10),
              update(Z1, [counterTackle(Agent), stamina(Agent, NewStamina)], [stamina(Agent, Strength)], Z2));
              (Z2=Z1) .

%% TACKLE ACTION

state_update(Z1, tackle(Agent), Z2, []):-
            holds(tackle(Agent,AgentB),Z1),
            holds(stamina(Agent,StrengthA), Z1),
            holds(stamina(Agent,StrengtB),Z1),
            NewStaminaA #= StrengthA * (1/10),
            NewStaminaB #= StrengthB * (1/10),
             ( holds(isInPart(Agent,ON_FIELD),Z1),
               holds(isInPart(AgentB,ON_FIELD),Z1),
               (
                StrengthA > StrengthB,
                update(Z1,[stamina(Agent,NewStaminaA),stamina(AgentB,NewStaminaB),inGround(AgentB),speed(Agent,vector(0,0))],[tackle(Agent,AgentB)], Z2)
                )
                ;
               (
               update(Z1,[stamina(Agent,NewStaminaA),stamina(AgentB,NewStaminaB),speed(Agent,vector(0,0)),speed(AgentB,vector(0,0))],[tackle(Agent,AgentB)],Z2)
               )
             )
             ;
             (
              holds(isInPart(Agent,ON_FIELD),Z1),
              holds(isInPart(AgentB, ON_INNER_TRACK ),Z1),
              update(Z1,[stamina(Agent,NewStaminaA),stamina(AgentB,NewStaminaB),inGround(AgentB)],[tackle(Agent, AgentB)],Z2)
             )
             ;
             (
              holds(isInPart(Agent,ON_INNER_TRACK),Z1),
              holds(isInPart(AgentB,ON_FIELD),Z1),
              update(Z1,[stamina(Agent,NewStaminaA),stamina(AgentB,NewStaminaB)],[tackle(Agent,AgentB)],Z2)
             )
             ;
             (
              holds(isInPart(Agent,ON_INNER_TRACK),Z1),
              holds(isInPart(AgentB, ON_INNER_TRACK),Z1),
              holds(position(Agent,vector(Sxa,Sya)),Z1),
              holds(position(Agent,vector(Sxb,Syb)),Z1),
              (
                (
                 (Sya > Syb, Sya < 0)
                ;
                 (Sya<Syb, Sya>0)
                ),
                 update(Z1,[stamina(Agent,NewStaminaA),stamina(AgentB,NewStaminaB),inGround(B)],[tackle(Agent, AgentB)],Z2)
              )
              ;
              (
               update(Z1,[stamina(Agent,NewStaminaA),stamina(AgentB,NewStaminaB),inGround(Agent), inGround(AgentB)],[tackle(Agent,AgentB)],Z2)
               )
             )
             ;
             (Z1=Z2).
