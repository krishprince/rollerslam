:- ['utils'].
%% FOCUS A: (-63835,0)
%% FOCUS B:(63835,0)

poss(interpretEnvModel, _).

%% Interpreting the Environment Model...

state_update(Z1, interpretEnvModel, Z2, _):-
 (  %% when the Agent is near the ball, but not so closer
    holds(position(Ball,vector(Sxb, Syb)), Z1),
    holds(position(Agent, vector(Sxa,Sya)), Z1),
    closer(Sxb, Syb, Sxa, Sya, 10000),
    not closer (Sxb, Syb, Sxa, Sya, 1000),
    update(Z1,[nearBall(Agent)],[], Z2)
 )
 ;
 (   %% when the ball is closer and nobody has it
    holds(position(Ball,vector(Sxb, Syb)), Z1),
    holds(position(Agent, vector(Sxa,Sya)), Z1),
    holds(free(Ball), Z1),
    closer(Sxb, Syb, Sxa, Sya, 1000),
    update(Z1,[closeBall(Agent)],[], Z2)
 )
 ;
 (  %% when the the enemy has the ball and he is closer
   holds(position(Agent, vector(Sxa, Sya)), Z1),
   holds(position(AgentB, vector(Sxb, Syx)), Z1),
   not holds(teammate(Agent, AgentB), Z1),
   holds(hasBall(AgentB), Z1),
   closer(Sxb, Syb, Sxa, Sya, 5000),
   update(Z1,[hasToTackle(Agent, AgentB)],[], Z2)
 )
 ;
 (  %% when a player need to protect a teammate who has the ball and exist a closer enemy
   holds(position(Agent,vector(Sxa, Sya)), Z1),
   holds(position(AgentB,vector(Sxb, Syb)), Z1),
   holds(position(AgentC, vector(Sxc, Syc)),Z1),
   holds(position(Ball,vector(Sxd, Syd)), Z1),
   holds(hasBall(Agent), Z1),
   holds(teammate(Agent, AgentB), Z1),
   not holds(teammate(Agent, AgentC), Z1,
   closer(Sxa, Sya, Sxb, Syb, 5000),
   closer(Sxb, Syb, Sxc, Syc, 5000),
   update(Z1,[hasToScreen(Agent, AgentC)],[],Z2)
 )
 ;
 (  %% strategie to shoot the ball
   holds(position(Agent, vector(Sxa, Sya)),Z1),
   holds(position(Ball,vector(Sxb, Syb)), Z1),
   holds(hasBall(Agent), Z1),
   holds(teamSide(Agent,SidePart),Z1),
      (
       SidePart = 'West',
       closer(Sxa, Sya, 63835, 0, 10000)
       )
       ;
       (
       SidePart = 'East',
        closer(Sxa, Sya, -63835,0,10000)
        ),
   update(Z1,[hasToShoot(Agent)],[],Z2)
 )
 ;
 ( %% strategic to go to goal or to pass the ball
  holds(position(Agent, vector(Sxa, Sya)), Z1),
  holds(position(Ball,vector(Sxb, Syb)), Z1)
  holds(hasBall(Agent),Z1),
  holds(teamSide(Agent,SidePart),Z1),
      (
       SidePart = 'West',
       not closer(Sxa, Sya, 63835, 0, 10000),
       (
         (
          Sxa>=0,
          update(Z1, [hasToGoToGoal(Agent)], [], Z2)
          )
          ;
          (
           update(Z1, [hasToPassTheBall(Agent)], [], Z2)
          )
        )
       )
       ;
      (
       SidePart = 'East',
       not closer(Sxa, Sya, -63835, 0, 10000),
       (
         (
          Sxa<0,
          update(Z1, [hasToGoToGoal(Agent)], [], Z2)
          )
          ;
          (
           update(Z1, [hasToPassTheBall(Agent)], [], Z2)
          )
        )
       )
 ).
