:- ['pre_conditionsActions'].
:- ['updateModel'].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Follow is the list of possible actions   %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% STAND UP ACTION
state_update(Z1,standUp(Agent),Z2,[]) :-
           poss(stand_up(Agent),Z1),
           update(Z1,[],[inGround(Agent)],Z2).

%% By considering that exists two kind of jump actions, a vertical one and a horizontal one.

%% JUMP HORIZONTAL ACTION
state_update(Z1, jumpH(Agent), Z3, []) :-
            poss(jumpH(Agent),Z1),
            holds(speed(Agent, vector(Vx, Vy)), Z1),
            update(Z1,[jumpingH(Agent, vector(Vx, Vy))],[],Z2),
            ramify(Z2,[jumpingH(Agent, vector(Vx, Vy))],[],Z3).


%% JUMP VERTICAL ACTION
state_update(Z1, jumpV(Agent), Z3, []) :-
            poss(jumpV(Agent),Z1),
            holds(position(Agent, vector(Sx, Sy)), Z1),
            update(Z1,[jumpingV(Agent, vector(Sx, Sy))],[],Z2),
            ramify(Z2,[jumpingV(Agent, vector(Sx, Sy))],[],Z3).


%% SKATE ACTION

state_update(Z1,skate(Agent, vector(Axa, Aya)), Z3, []) :-
            holds(acceleration(Agent,vector(OldA)), Z1),
            holds(speed(Agent,vector(OldV)),Z1),
            holds(position(Agent, vector(OldS)),Z1),
            holds(stamina(Agent,Strength), Z1),
            NewStamina #= Strength + (Strength * (1/100)),
            update(Z1,[acceleration(Agent, vector(Axa, Aya)), stamina(Agent,NewStamina)],[acceleration(Agent,vector(Old)),stamina(Agent,Strength)],Z2),
            ramify(Z2,[acceleration(Agent, vector(Axa,Aya)),speed(Agent,vector(OldV)), position(Agent, vector(OldS))], [],Z3).

%% Actions with ball

%% RELEASE ACTION
state_update(Z1, release(Agent), Z2, [Whistle]):-
             poss(release(Agent),Z1),
             Whistle = true,
             update(Z1,[free(Ball)],[hasBall(Agent)],Z2).


%% CATCH ACTION
state_update(Z1, catch(Agent), Z2, []):-
            poss(catch(Agent),Z1),
            holds(position(Agent, vector(Sx, Sy)), Z1),
            holds(position(ball, vector(Sbx, Sby)), Z1),
            (
              closer(Sx, Sy, Sbx, Sby, 500),
              update(Z1,[hasBall(Agent)],[free(Ball)],Z2)
            );
            (
              Z2 = Z1
            ).


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%% HOLDING BALL ACTIONS %%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% DROP AND KICK ACTION
state_update(Z1, dropAndKick(Agent), Z3, []):-
             poss(dropAndKick(Agent),Z1),
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball,vector(OldS)),Z1),
             holds(speed(Ball,vector(OldV)),Z1),
             moduleX(Strength,Ax),
             moduleY(Strength,Ay),
             NewStamina #= Strength - (Strength * (1/10)),
             update(Z1,[acceleration(Ball, vector(Ax, Ay)), stamina(Agent,NewStamina), free(Ball)],[hasBall(Agent)],Z2),
             ramify(Z2,[acceleration(Ball,vector(Ax,Ay)),position(Ball,vector(OldS)),speed(Ball,vector(OldV))],[],Z3).


%% KICK ACTION
state_update(Z1,kick(Agent), Z3,[]):-
             poss(kick(Agent),Z1),
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball,vector(OldS)),Z1),
             holds(speed(Ball,vector(OldV)),Z1),
             moduleX(Strength,Ax),
             moduleY(Strength,Ay),
             NewStamina #= Strength - (Strength * (1/10)),
             update(Z1,[acceleration(Ball, vector(Ax,Ay)), stamina(Agent, NewStamina)],[hasBall(Agent)],Z2),
             ramify(Z2,[acceleration(Ball,vector(Ax,Ay)),position(Ball,vector(OldS)),speed(Ball,vector(OldV))],[],Z3).


%% THROW ACTION
state_update(Z1, throw(Agent), Z3, []):-
             poss(throw(Agent),Z1),
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball,vector(OldS)),Z1),
             holds(speed(Ball,vector(OldV)),Z1),
             moduleX((Strength * (1/2)),Ax),
             moduleY((Strength * (1/2)),Ay),
             NewStamina #= Strength - (Strength * (1/5)),
             update(Z1,[acceleration(Ball, vector(Ax, Ay)), stamina(Agent, NewStamina),free(Ball)],[hasBall(Agent)],Z2),
             ramify(Z2,[acceleration(Ball,vector(Ax,Ay)),position(Ball,vector(OldS)),speed(Ball,vector(OldV))],[],Z3).


%% VOLLEY ACTION
state_update(Z1,volley(Agent), Z3,[]):-
             poss(volley(Agent),Z1),
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball,vector(OldS)),Z1),
             holds(speed(Ball,vector(OldV)),Z1),
             moduleX(Strength,Ax),
             moduleY(Strength,Ay),
             NewStamina #= Strength - (Strength * (1/10)),
             update(Z1,[acceleration(Ball, vector(Ax, Ay)), stamina(Agent, NewStamina)],[],Z2),
             ramify(Z2,[acceleration(Ball,vector(Ax,Ay)),position(Ball,vector(OldS)),speed(Ball,vector(OldV))],[],Z3).


%% SPIKE KICK ACTION
state_update(Z1,spike(Agent), Z3,[]):-
             poss(spike(Agent),Z1),
             holds(stamina(Agent,Strength), Z1),
             holds(position(Ball,vector(OldS)),Z1),
             holds(speed(Ball,vector(OldV)),Z1),
             moduleX(Strength * (1/2),Ax),
             moduleY(Strength * (1/2),Ay),
             NewStamina #= Strength - (Strength * (1/5)),
             update(Z1,[acceleration(Ball, vector(Ax, Ay)), stamina(Agent, NewStamina)],[],Z2),
             ramify(Z2,[acceleration(Ball,vector(Ax,Ay)),position(Ball,vector(OldS)),speed(Ball,vector(OldV))],[],Z3).


%% TOUCHDOWN ACTION

%% state_update(Z1, touchDown(), Z2, []).



%% SCREEN ACTION
state_update(Z1, screen(Agent), Z3,[]):-
            poss(screen(Agent),Z1),
            holds(screen(Agent, AgentB),Z1),
            holds(stamina(Agent,StrengthA), Z1),
            holds(stamina(Agent,StrengtB),Z1),
            (StrengthA > StrengthB,
              update(Z1,[inGround(AgentB),acceleration(Agent,vector(0,0))],[screen(Agent,AgentB)], Z2),
              ramify(Z2,[acceleration(Agent,vector(0,0))],[],Z3)
              );
            (
              update(Z1,[acceleration(Agent,vector(0,0)), acceleration(AgentB,vector(0,0))],[screen(Agent,AgentB)],Z2),
              ramify(Z2,[acceleration(Agent,vector(0,0)),acceleration(AgentB,vector(0,0))],[],Z3)
              ).


%% COUNTERTACKLE ACTION
state_update(Z1, counterTackle(Agent), Z2, []):-
            poss(counterTackle(Agent),Z1),
            hols(stamina(Agent,Strength), Z1),
            (Strength >= 10000,
             NewStamina #= Strength - (Strength * (1/10)),
              update(Z1, [counterTackle(Agent), stamina(Agent, NewStamina)], [stamina(Agent, Strength)], Z2));
              (Z2=Z1).


%% TACKLE ACTION
state_update(Z1, tackle(Agent), Z2, []):-
            poss(tackle(Agent),Z1),
            holds(tackle(Agent,AgentB),Z1),
            holds(stamina(Agent,StrengthA), Z1),
            holds(stamina(AgentB,StrengthB), Z1),
            holds(position(AgentA, vector(Sxa,Syb)),Z1),
            holds(position(AgentB, vector(Sxb,Syb)),Z1),
             ( isOnField(vector(Sxa,Sya)),
               isOnField(vector(Sxb,Syb)),
               (StrengthA > StrengthB,
              update(Z1,[inGround(AgentB),acceleration(Agent,vector(0,0)), speed(Agent,vector(0,0))],[tackle(Agent,AgentB)], Z2));
            (update(Z1,[acceleration(Agent,vector(0,0)), speed(Agent,vector(0,0)),acceleration(AgentB,vector(0,0)), speed(AgentB,vector(0,0))],[tackle(Agent,AgentB)],Z2))
             )
             ;
             (
             isOnField(vector(Sxa,Sya)),
             isOnInTrack(vector(Sxb,Syb)),
             update(Z1,[inGround(AgentB)],[tackle(Agent, AgentB)],Z2)
             )
             ;
             (
             isOnInTrack(vector(Sxa,Sya)),
             isOnField(vector(Sxb,Syb)),
             update(Z1,[],[tackle(Agent,AgentB)],Z2)
             )
             ;
             (
             isOnInTrack(vector(Sxa,Sya)),
             isOnInTrack(vector(Sxb,Syb)),
             (((Sya > Syb),(Sya < 0));((Sya<Syb),(Sya>0)), update(Z1,[inGround(B)],[tackle(Agent, AgentB)],Z2));
             (update(Z1,[inGround(Agent), inGround(AgentB)],[tackle(Agent,AgentB)],Z2))
             )
             ;
             (Z1=Z2).

%% DUNK ACTION
state_update(Z1, dunk(Agent,Dir), Z2, []):- 
               poss(dunk(Agent),Z1),
               holds(position(Ball,OldDir),Z1),
               update(Z1,[position(Ball,Dir)],[position(Ball,OldDir), hasBall(Agent)],Z2).


