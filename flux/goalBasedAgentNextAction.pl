:- ['goalBasedAgentUpdate'].


poss(computeNextAxtion,_).

%% Computing the next action

state_update(Z1, computeNextAction, Z2, []):-
  holds(goal(Agent,CurrentGoal), Z1),
 ( CurrentGoal = 'GO_TO_BALL',
  update[Z1, [], [goal(Agent,CurrentGoal)], Z2],
  execute(skate(Agent,A),Z2,Z3)
 )
 ;
 (
  CurrentGoal = 'CATCH_BALL',
  update[Z1, [], [goal(Agent,CurrentGoal)], Z2],
  execute(catch(Agent),Z2,Z3)
 )
 ;
 (
  CurrentGoal = 'TACKLE_PLAYER',
  update[Z1, [], [goal(Agent,CurrentGoal)], Z2],
  execute(tackle(Agent),Z2,Z3)
 )
 ;
 (
  CurrentGoal = 'COUNTERTACKLE',
  update[Z1, [], [goal(Agent,CurrentGoal)], Z2],
  execute(countertackle(Agent),Z2,Z3)
 )
 ;
 (
  CurrentGoal = 'STAND_UP',
  update[Z1, [], [goal(Agent,CurrentGoal)], Z2],
  execute(standUp(Agent),Z2,Z3)
 )
 ;
 (
  CurrentGoal = 'JUMP',
  update[Z1, [], [goal(Agent,CurrentGoal)], Z2],
  execute(jump(Agent),Z2,Z3)
 )
 ;
 (
  CurrentGoal = 'SCREEN',
  update[Z1, [], [goal(Agent,CurrentGoal)], Z2],
  execute(screen(Agent),Z2,Z3)
 )
 ;
 (
  CurrentGoal = 'SHOOT',
  update[Z1, [], [goal(Agent,CurrentGoal)], Z2],
  execute(shoot(Agent),Z2,Z3)
 );
 (
  CurrentGoal = 'GO_TO_GOAL',
  update[Z1, [], [goal(Agent,CurrentGoal)], Z2],
  execute(skate(Agent),Z2,Z3)
 );
 (
  CurrentGoal = 'PASS',
  update[Z1, [], [goal(Agent,CurrentGoal)], Z2],
  execute(pass(Agent),Z2,Z3)
 ).