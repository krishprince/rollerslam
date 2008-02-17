:- ['fluent'].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Follow is the list of possible actions   %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% STAND UP ACTION
state_update(Z1,standUp(Agent),Z2,[]) :-
           poss(stand_up(Agent),Z1),
           update(Z1,[],[inGround(Agent)],Z2).

%% By considering that exists two kind of jump actions, a vertical one and a horizontal one.

%% JUMP HORIZONTAL ACTION
state_update(Z1, jumpH(Agent), Z2, []) :-
            poss(jumpH(Agent),Z1),
            holds(position(Agent, vector(Sx, Sy)), Z1),
            holds(stamina(Agent,Stamina),Z1),
            calcNewPosition(Stamina, vector(Sx,Sy), vector(NSx,NSy)),
            insideBoundary(vector(NSx,NSy)),
            update(Z1,[position(Agent, vector(NSx, NSy)],[position(Agent, vector(Sx, Sy)],Z2).


%% JUMP VERTICAL ACTION
state_update(Z1, jumpV(Agent), Z2, []) :-
            poss(jumpH(Agent),Z1),
            update(Z1,[onAir(Agent)],[],Z2).


%% SKATE ACTION
%TODO boundary_collision (colisão com os limites do campo - possible_position).
%TODO player_collision (colisão com outro jogador - possible_position).
%TODO stand_up
state_update(Z1,skate(Agent, vector(Axa, Aya)), Z2, []) :-
            holds(acceleration(Agent,vector(Old)), Z1),
            update(Z1,[acceleration(Agent, vector(Axa, Aya))],[acceleration(Agent,vector(Old))],Z2).

%% Actions with ball

%% RELEASE ACTION
state_update(Z1, release(Agent), Z2, [Whistle]):-
             poss(release(Agent),Z1)
             Whistle = true,
             update(Z1,[free(Ball)],[hasBall(Agent)],Z2).


%% CATCH ACTION
state_update(Z1, catch(Agent), Z2, []):-
            poss(catch(Agent),Z1),
            holds(position(Agent, vector(Sx, Sy), Z1),
            holds(position(ball, vector(Sbx, Sby)), Z1),
            (
              closer(Sx, Sy, Sbx, Sby, 500),            %% the ball is near
              update(Z1,[hasBall(Agent)],[],Z2)
            );
            (
              Z2 = Z1
            ).


%% DROP AND KICK ACTION
state_update(Z1, dropAndKick(Agent), Z2, []):-
             holds(hasBall(Agent),Z1),                          %% o agente deve possuir a bola
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength,
             Yb #= Syb * Strength,
             NewStamina #= Strength * (1/10),
             update(Z1,[position(Ball, vector(Xb, Yb)), stamina(Agent, NewStamina)],[hasBall(Agent),position(Ball, vector(Sxb, Syb))],Z2).


%% KICK ACTION
state_update(Z1,kick(Agent), Z2,[]):-
             holds(hasBall(Agent),Z1),                          %% o agente deve possuir a bola
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball, vector(Sxb, Syb)), Z1),
             Xb #= Sxb * Strength,
             Yb #= Syb * Strength,
             NewStamina #= Strength * (1/10),
             update(Z1,[position(Ball, vector(Xb, Yb)), stamina(Agent, NewStamina)],[position(Ball, vector(Sxb, Syb))],Z2).


%% THROW ACTION
state_update(Z1, throw(Agent), Z2, []):-
             holds(hasBall(Agent),Z1),                          %% o agente deve possuir a bola
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
                 holds(hasBall(Agent),Z1),              %% o agente deve possuir a bola
                 holds(pass(Agent,AgentB),Z1),
                 holds(position(AgentB,vector(S)),Z1),
                 holds(position(Ball,vector(Sb)),Z1),
                 update(Z1,[position(Ball,vector(S))],[position(Ball,vector(Sb)),pass(Agent,AgentB)],Z2).


%% SHOOT ACTION
state_update(Z1, shoot(Agent), Z2, []):-
                 holds(stamina(Agent,Strength),Z1),
                 (
                 holds(hasBall(Agent,HAND),Z1),       %% o agente deve possuir a bola
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
                  holds(hasBall(Agent,FOOT),Z1),       %% o agente deve possuir a bola
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
                 ).


%% SCREEN ACTION
state_update(Z1, screen(Agent), Z2,[]):-
            holds(screen(Agent, AgentB),Z1),
            holds(stamina(Agent,StrengthA), Z1),
            holds(stamina(Agent,StrengtB),Z1),
            (StrengthA > StrengthB,
              update(Z1,[inGround(AgentB),acceleration(Agent,vector(0,0)), speed(Agent,vector(0,0))],[screen(Agent,AgentB)], Z2));
            (update(Z1,[acceleration(Agent,vector(0,0)), speed(Agent,vector(0,0)),acceleration(AgentB,vector(0,0)), speed(AgentB,vector(0,0))],[screen(Agent,AgentB)],Z2)).


%% COUNTERTACKLE ACTION
%TODO has_ball. (?????????)
%TODO recieve_tackle.
state_update(Z1, counterTackle(Agent), Z2, []):-
            hols(stamina(Agent,Strength), Z1),
            (Strength >= 10000,
             NewStamina #= Strength * (1/10),
              update(Z1, [counterTackle(Agent), stamina(Agent, NewStamina)], [stamina(Agent, Strength)], Z2));
              (Z2=Z1) .


%% TACKLE ACTION
state_update(Z1, tackle(Agent), Z2, []):-
            holds(hasBall(AgentB),Z1),                %% o oponente está com a bola
            holds(tackle(Agent,AgentB),Z1),
            holds(stamina(Agent,StrengthA), Z1),
            holds(stamina(Agent,StrengtB),Z1),
             ( holds(isInPart(Agent,ON_FIELD),
               holds(isInPart(AgentB,ON_FIELD),
               (StrengthA > StrengthB,
              update(Z1,[inGround(AgentB),acceleration(Agent,vector(0,0)), speed(Agent,vector(0,0))],[tackle(Agent,AgentB)], Z2));
            (update(Z1,[acceleration(Agent,vector(0,0)), speed(Agent,vector(0,0)),acceleration(AgentB,vector(0,0)), speed(AgentB,vector(0,0))],[tackle(Agent,AgentB)],Z2)).
             )
             ;
             (
             holds(isInPart(Agent,ON_FIELD),Z1),
             holds(isInPart(AgentB, ON_INNER_TRACK ),Z1),
             update(Z1,[inGround(AgentB)],[tackle(Agent, AgentB)],Z2)
             )
             ;
             (
             holds(isInPart(Agent,ON_INNER_TRACK),Z1),
             holds(isInPart(AgentB,ON_FIELD),Z1),
             update(Z1,[],[tackle(Agent,AgentB)],Z2)
             )
             ;
             (
             holds(isInPart(Agent,ON_INNER_TRACK),Z1),
             holds(isInPart(AgentB, ON_INNER_TRACK),Z1),
             holds(position(Agent,vector(Sxa,Sya),Z1),
             holds(position(Agent,vector(Sxb,Syb),Z1),
             (((Sya > Syb),(Sya < 0));((Sya<Syb),(Sya>0)), update(Z1,[inGround(B)],[tackle(Agent, AgentB)],Z2));
             (update(Z1,[inGround(Agent), inGround(AgentB)],[tackle(Agent,AgentB)],Z2))
             )
             ;
             (Z1=Z2);
