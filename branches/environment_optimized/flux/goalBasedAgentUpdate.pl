:- ['goalBasedAgent'].
:- ['goalBasedAgentPosition'].

poss(updateGoal,_).

%% Update Goal

state_update(Z1, updateGoal, Z2, []):-
  holds(goal(Agent, X),Z1),
(
  holds(nearBall(Agent), Z1),
  CurrentGoal #= 'GO_TO_BALL',
  update(Z1, [goal(Agent, CurrentGoal)], [goal(Agent, X)],Z2)
)
;
(
  holds(closeBall(Agent), Z1),
  CurrentGoal #= 'CATCH_BALL',
  update(Z1, [goal(Agent, CurrentGoal)], [goal(Agent, X)],Z2)
)
;
(
  holds(hasTotackle(Agent, AgentB), Z1),
  holds(goal(AgentB, Y),Z1),
  CurrentGoal #= 'TACKLE_PLAYER',
  CurrentGoalB  #= 'COUNTERTACKLE',
  update(Z1, [tackle(Agent, AgentB), goal(Agent, CurrentGoal), goal(AgentB, CurrentGoalB)], [goal(Agent, X), goal(AgentB, Y)],Z2)
)
;
(
  holds(inGround(Agent), Z1),
  CurrentGoal #= 'STAND_UP',
  update(Z1, [goal(Agent, CurrentGoal)], [goal(Agent, X)], Z2)
)
;
(
  ((holds(isInPart(Agent, AgentLocal),Z1),
    AgentLocal = 'INNER_TRACK');
   (holds(isInPart(Agent, AgentLocal),Z1),
    AgentLocal = 'OUTER_TRACK')),
    CurrentGoal #= 'JUMP',
   update(Z1, [goal(Agent, CurrentGoal)], [goal(Agent,X)], Z2)
)
;
(
 holds(hastoScreen(Agent,AgenB), Z1),
 CurrentGoal #= 'SCREEN',
 update(Z1,[screen(Agent, AgentB), goal(Agent,CurrentGoal)],[goal(Agent,X)],Z2)
)
;
(
 holds(hasToShoot(Agent),Z1),
 CurrentGoal #= 'SHOOT',
 update(Z1,[goal(Agent,CurrentGoal)],[goal(Agent,X)],Z2)
)
;
(
 holds(hasToGoToGoal(Agent), Z1),
 CurrentGoal #= 'GO_TO_GOAL',
 update(Z1, [goal(Agent, CurrentGoal)], [goal(Agent,X)], Z2)
)
;
(
 holds(hasToPassTheBall(Agent), Z1),
 CurrentGoal #= 'PASS',
 update(Z1, [goal(Agent, CurrentGoal)], [goal(Agent,X)], Z2)
).
