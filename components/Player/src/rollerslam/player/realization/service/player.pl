
playerInitializeModel([PlayerID, Team, Oid], 
	[me@[id->PlayerID], 
	 me@[team->Team], 
	 me@[senseCycle->true],
	 me@[oid->OidAtom]]) :-

	 term_string(OidAtom, Oid).
	
playerUpdateModel(Z0, Z1) :- 
	holds(me@[senseCycle->true], Z0),
	update(Z0, [me@[senseCycle->false]], [me@[senseCycle->true]], Z1).

playerUpdateModel(Z0, Z1) :-
	holds(me@[senseCycle->false], Z0),
	update(Z0, [me@[senseCycle->true]], [me@[senseCycle->false]], Z1).	
	

playerComputeNextAction(Z, dash(P, vector(-1, 1))) :-
   	holds(me@[senseCycle->false], Z),
	holds(me@[oid->P], Z).



playerComputeNextAction(Z, ask(ball)) :-
	holds(me@[senseCycle->true], Z).


playerComputeNextAction(Z,A) :- holds(me@[senseCycle->true], Z), withBallBehavior(Z,A).

%% holdBall :: throw lowgoal shoot
withBallBehavior(Z, throwLowGoal(playerId(PID, Z), Direction)) :-
                           playerId(PID, Z), distance(Z, playerId(PID), nearestOpponent, D), D >= 1000,
                           (playerRole(midfield) ; playerRole(forward)),
                           not opponentPlayerBlocking(Z, lowGoal),
                           distance(Z, playerId(PID, Z), lowGoal, DistanceLowGoal), DistanceLowGoal < 10000,
                           position(lowGoal, PositionLowGoal),
                           playerPosition(Z, playerId(PID, Z), SPlayer),
                           subtractVector(SPlayer, PositionLowGoal, Direction).
                           
%% holdBall :: dropAndKick lowgoal shoot
withBallBehavior(Z, dropAndKickLowGoal(playerId(PID, Z), Direction)) :-
                           playerId(PID, Z), distance(Z, playerId(PID), nearestOpponent, D), D >= 1000,
                           (playerRole(midfield) ; playerRole(forward)),
                           opponentPlayerBlocking(Z, lowGoal),
                           distance(Z, playerId(PID, Z), lowGoal, DistanceLowGoal), DistanceLowGoal >= 10000,
                           position(lowGoal, PositionLowGoal),
                           playerPosition(Z, playerId(PID, Z), SPlayer),
                           subtractVector(SPlayer, PositionLowGoal, Direction).

%% holdBall :: Release
withBallBehavior(Z,release(playerId(PID, Z), playerPosition(Z,SPlayer))) :-
                           playerId(PID, Z), playerIsOnFloor(Z, playerId(PID, Z)),
                           distance(Z, playerId(PID, Z), nearestOpponent,D) , D=0,
                           playerPosition(Z,SPlayer).

%% holdBall :: throw highgoal shoot
withBallBehavior(Z,throwHighGoal(player(PID, Z),Direction)) :-
                           playerId(PID, Z), distance(Z, playerId(PID, Z), nearestOpponent, D), D >= 1000,
                           (playerRole(midfield) ; playerRole(forward)),
                           opponentPlayerBlocking(Z,lowGoal), %ver como escreve false
                           isTeamPlayerWinning(Z, playerId(PID, Z)),
                           playerPosition(Z,SPlayer),
                           distance(Z, playerId(PID), highGoal, DistanceHighGoal), DistanceHighGoal<10000,
                           position(highGoal,PositionHighGoal),
                           subtractVector(SPlayer, PositionHighGoal, Direction).

%% holdBall :: dropAndKick highgoal shoot
withBallBehavior(Z,dropAndKickHighGoal(playerId(PID, Z),Direction)) :-
                           playerId(PID, Z), distance(Z, playerId(PID, Z), nearestOpponent, D), D >= 1000,
                           (playerRole(midfield) ; playerRole(forward)),
                           opponentPlayerBlocking(Z,lowGoal), %ver como escreve false
                           isTeamPlayerWinning(Z, playerId(PID, Z)),
                           playerPosition(Z,SPlayer),
                           playerDistance(Z, highGoal, DistanceHighGoal), DistanceHighGoal>=10000,
                           position(highGoal,PositionHighGoal),
                           subtractVector(SPlayer, PositionHighGoal, Direction).

%% holdBall :: throw basket shoot
withBallBehavior(Z,throwBasket(playerId(PID, Z),Direction)) :-
                           playerId(PID, Z), distance(Z, playerId(PID, Z), nearestOpponent, D), D >= 1000,
                           (playerRole(midfield) ; playerRole(forward)),
                           opponentPlayerBlocking(Z,lowGoal), %ver como escreve false
                           isTeamPlayerLoosing(Z, playerId(PID, Z)),
                           playerPosition(Z,SPlayer),
                           distance(Z, playerId(PID, Z), basket, DistanceBasket), DistanceBasket<10000,
                           position(basket,PositionBasket),
                           subtractVector(SPlayer, PositionBasket, Direction).

%% holdBall :: dropAndKick basket shoot
withBallBehavior(Z,dropAndKickBasket(playerId(PID, Z),Direction)) :-
                           playerId(PID, Z), distance(Z, playerId(PID, Z), nearestOpponent, D), D >= 1000,
                           (playerRole(midfield) ; playerRole(forward)),
                           opponentPlayerBlocking(Z,lowGoal), %ver como escreve false
                           isTeamPlayerLoosing(Z, playerId(PID, Z)),
                           playerPosition(Z,SPlayer),
                           distance(Z, playerId(PID, Z), basket, DistanceBasket), DistanceBasket>=10000,
                           position(basket,PositionBasket),
                           subtractVector(SPlayer, PositionBasket, Direction).

%% holdBall :: throw pass
withBallBehavior(Z,throwPass(playerId(PID, Z),Direction)):-
                           playerId(PID, Z),
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D<1000,
                           samePlayerTeamUnmarked(Z, playerId(PID, Z)),
                           playerId(PIDNearestPlayer, Z),
                           distance(Z, playerId(PIDNearestPlayer, Z), nearestOpponent,  DistanceNearestOpponent), DistanceNearestOpponent>=1000,
                           playerPosition(Z,playerId(PID, Z),ThePlayer),
                           playerPosition(Z,playerId(PIDNearestPlayer, Z),NearPlayer),
                           subtractVector(ThePlayer,NearPlayer,Direction).


%% holdBall :: dropAndKick pass
withBallBehavior(Z,dropAndKickPass(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D<1000,
                           playerId(PID, Z),
                           samePlayerTeamUnmarked(Z, playerId(PID), Z),
                           playerId(PIDFarthestPlayer, Z),
                           distance(Z, playerId(PIDFarthestPlayer, Z), nearestOpponent,  DistanceNearestOpponent), DistanceNearestOpponent>=1000,
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           playerPosition(Z,playerId(PIDFarthestPlayer, Z), FarthestPlayer),
                           subtractVector(ThePlayer, FarthestPlayer, Direction).

%% holdBall :: throw sendBall
withBallBehavior(Z,throwSendBall(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D<1000,
                           playerId(PID, Z),
                           not samePlayerTeamUnmarked(Z, playerId(PID, Z)),
                           (playerRole(Z, playerId(PID, Z),midfield);
                           playerRole(Z, playerId(PID, Z),forward)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(highGoal, PositionHighGoal),
                           subtractVector(ThePlayer, PositionHighGoal, Direction).

%% holdBall :: dropAndKick sendBall
withBallBehavior(Z,dropAndKickSendBall(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D<1000,
                           playerId(PID, Z),
                           not samePlayerTeamUnmarked(Z, playerId(PID, Z)),
                           (playerRole(Z, playerId(PID, Z),defense)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(highGoal, PositionHighGoal),
                           subtractVector(ThePlayer, PositionHighGoal, Direction).

%% notHoldBall :: kick behavior :: pass
withBallBehavior(Z,kickPass( playerId(PID, Z) , Direction )):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D<1000,
                           playerId(PID, Z),
                           playerId(PIDNearestPlayer, Z),
                           distance(Z, playerId(PIDNearestPlayer, Z), nearestOpponent,D1), D1>=1000,
                           sameTeamPlayerUnmarked(Z, playerId(PID, Z)),
                           isConducingWithFoot(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           playerPosition(Z, playerId(PIDNearestPlayer, Z), NearPlayer),
                           subtractVector(ThePlayer, NearPlayer, Direction).


%% notHoldBall :: kick behavior :: sendBall
withBallBehavior(Z,kickSendBall(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D<1000,
                           playerId(PID, Z),
                           not samePlayerTeamUnmarked(Z, playerId(PID, Z)),
                           isConducingWithFoot(Z,playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(highGoal, PositionHighGoal),
                           subtractVector(ThePlayer, PositionHighGoal, Direction).

%% notHoldBall :: kick behavior :: lowgoal shoot
withBallBehavior(Z,kickLowGoal(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D>=1000,
                           playerId(PID, Z),
                           not opponentPlayerBlocking(Z,lowGoal),
                           isConduncingWithFoot(Z, playerId(PID, Z)),
                           position(lowGoal, PositionLowGoal),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           subtractVector(ThePlayer, PositionLowGoal, Direction).


%% notHoldBall :: kick behavior :: highgoal shoot
withBallBehavior(Z,kickHighGoal(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D>=1000,
                           playerId(PID, Z),
                           opponentPlayerBlocking(Z, lowGoal),
                           teamWinningGame(Z, playerId(PID, Z)),
                           isConducingWithFoot(Z, playerId(PID, Z)),
                           position(highGoal, PositionHighGoal),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           subtractVector(ThePlayer, PositionHighGoal, Direction).

%% notHoldBall :: kick behavior :: basket shoot
withBallBehavior(Z,kickBasket(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D>=1000,
                           playerId(PID, Z),
                           opponentPlayerBlocking(Z, lowGoal),
                           teamLoosinGame(Z, playerId(PID, Z)),
                           isConducingWithFoot(Z, playerId(PID, Z)),
                           position(basket, PositionBasket),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           subtractVector(ThePlayer, PositionBasket, Direction).

%%notHoldBall :: just received ball :: spike pass
withBallBehavior(Z,spikePass(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D<1000,
                           playerId(PID, Z),
                           playerId(PIDNearestPlayer, Z),
                           sameTeamPlayerUnmarked(Z, playerId(PID, Z)),
                           withoutBallPreviously(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           playerPosition(Z, playerId(PIDNearestPlayer, Z), NearPlayer),
                           subtractVector(ThePlayer, NearPlayer, Direction),
                           ballNearPlayerHand(Z, playerId(PID, Z)).

%%notHoldBall :: just received ball :: volley pass
withBallBehavior(Z,volleyPass(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D<1000,
                           playerId(PID, Z),
                           playerId(PIDNearestPlayer, Z),
                           sameTeamPlayerUnmarked(Z, playerId(PID, Z)),
                           withoutBallPreviously(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           playerPosition(Z, playerId(PIDNearestPlayer, Z), NearPlayer),
                           subtractVector(ThePlayer, NearPlayer, Direction),
                           ballNearPlayerFoot(Z, playerId(PID, Z)).

%%notHoldBall :: just received ball :: spike sendBall
withBallBehavior(Z,spikeSendBall(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D<1000,
                           playerId(PID, Z),
                           ameTeamPlayerUnmarked(Z, playerId(PID, Z)),
                           withoutBallPreviously(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(highGoal,PositionHighGoal),
                           subtractVector(ThePlayer, PositionHighGoal, Direction),
                           ballNearPlayerHand(Z, playerId(PID, Z)).

%%notHoldBall :: just received ball :: volley sendBall
withBallBehavior(Z,volleySendBall(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D<1000,
                           playerId(PID, Z),
                           not sameTeamPlayerUnmarked(Z, playerId(PID, Z)),
                           withoutBallPreviously(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(highGoal,PositionHighGoal),
                           subtractVector(ThePlayer, PositionHighGoal, Direction),
                           ballNearPlayerFoot(Z, playerId(PID, Z)).

%%notHoldBall :: just received ball :: volley lowGoal shoot
withBallBehavior(Z,volleyLowGoal(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D>=1000,
                           playerId(PID, Z),
                           not opponentPlayerBlocking(Z, lowGoal),

                           withoutBallPreviously(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(lowGoal,PositionLowGoal),
                           subtractVector(ThePlayer, PositionLowGoal, Direction),
                           ballNearPlayerFoot(Z, playerId(PID, Z)).

%%notHoldBall :: just received ball :: spike lowGoal shoot
withBallBehavior(Z,spikeLowGoal(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D>=1000,
                           playerId(PID, Z),
                           not opponentPlayerBlocking(Z, lowGoal),

                           withoutBallPreviously(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(lowGoal,PositionLowGoal),
                           subtractVector(ThePlayer, PositionLowGoal, Direction),
                           ballNearPlayerHand(Z, playerId(PID, Z)).

%%notHoldBall :: just received ball :: volley highGoal shoot
withBallBehavior(Z,volleyHighGoal(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D>=1000,
                           playerId(PID, Z),
                           opponentPlayerBlocking(Z, lowGoal),
                           teamWinningGame(Z, playerId(PID, Z)),

                           withoutBallPreviously(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(highGoal,PositionHighGoal),
                           subtractVector(ThePlayer, PositionHighGoal, Direction),
                           ballNearPlayerFoot(Z, playerId(PID, Z)).

%%notHoldBall :: just received ball :: spike highGoal shoot
withBallBehavior(Z,spikeHighGoal(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D>=1000,
                           playerId(PID, Z),
                           opponentPlayerBlocking(Z, lowGoal),
                           teamWinningGame(Z, playerId(PID, Z)),

                           withoutBallPreviously(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(highGoal,PositionHighGoal),
                           subtractVector(ThePlayer, PositionHighGoal, Direction),
                           ballNearPlayerHand(Z, playerId(PID, Z)).

%%notHoldBall :: just received ball :: volley basket shoot
withBallBehavior(Z,volleyBasket(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D>=1000,
                           playerId(PID, Z),
                           opponentPlayerBlocking(Z, lowGoal),
                           teamLoosingGame(Z, playerId(PID, Z)),

                           withoutBallPreviously(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(basket,PositionBasket),
                           subtractVector(ThePlayer, PositionBasket, Direction),
                           ballNearPlayerFoot(Z, playerId(PID, Z)).

%%notHoldBall :: just received ball :: spike basket shoot
withBallBehavior(Z,spikeBasket(playerId(PID, Z),Direction)):-
                           distance(Z,playerId(PID, Z), nearestOpponent, D), D>=1000,
                           playerId(PID, Z),
                           opponentPlayerBlocking(Z, lowGoal),
                           teamLoosingGame(Z, playerId(PID, Z)),

                           withoutBallPreviously(Z, playerId(PID, Z)),
                           playerPosition(Z, playerId(PID, Z), ThePlayer),
                           position(basket,PositionBasket),
                           subtractVector(ThePlayer, PositionBasket, Direction),
                           ballNearPlayerHand(Z, playerId(PID, Z)).

                           
playerProcessAction(Z, _, Z).
                       