

init(Z0) :- Z0 = [team(team_A), goalLine(line(50, 10, 50, -10), team(team_A)),
                  team(team_B), goalLine(line(-50, 10, -50, -10), team(team_B)),
                  ballPosition(vector(25, 5)), ballSpeed(vector(2, 0)),
                  player(p1), playerTeam(p1, team(team_A)), hasBall(player(p1)), playerPosition(p1, vector(50, 5)), throw(P),
                  shootStreak(p1, kick, 0), shootStreak(p1, throw, 0), shootStreak(p1, spike, 0), shootStreak(p1, volley, 0), shootStreak(p1, dropAndKick, 0),
                  player(p2), playerTeam(p2, team(team_A)), playerPosition(p2, vector(30, 5)),
                  shootStreak(p2, kick, 0), shootStreak(p2, throw, 0), shootStreak(p2, spike, 0), shootStreak(p2, volley, 0), shootStreak(p2, dropAndKick, 0)].

rodarTeste(Z) :- cicle1(Z1), cicle2(Z1, Z).

rodarTeste2(Z) :- cicle1(Z1), cicle2(Z1, Z2), boxscoreUpdateModel(Z2, Z).

cicle1(Z) :- init(Z0), boxscoreUpdateModel(Z0, Z).

cicle2(Z0, Z1) :- update(Z0, [], [throw(P)], Z2), boxscoreUpdateModel(Z2, Z1).


testeShootStreak(Z) :- init(Z0), update(Z0, [action(throw, P)], [throw(P)], Z2), processaRamification(Z2, shootStreak, Z).

actionKick(P, Z0) :- holds(kick(P), Z0).

actionThrow(P, Z0) :- holds(throw(P), Z0).

actionSpike(P, Z0) :- holds(spicke(P), Z0).

actionVolley(P, Z0) :- holds(volley(P), Z0).

actionDropAndKick(P, Z0) :- holds(dropAndKick(P), Z0).

actionTackle(P, P2, Z0) :- holds(tackle(P), Z0).

actionCounter_tackle(P, P2, Z0) :- holds(counter_tackle(P), Z0).

playerTeam(P, Team, Z0) :- holds(playerTeam(P, team(team_A)), Z0).

playerPosition(P, Vector, Z0) :- holds(playerPosition(P, Vector), Z0).

ballSpeed(Vector, Z0) :- holds(ballSpeed(Vector), Z0).

ballPosition(Vector, Z0) :- holds(ballPosition(Vector), Z0).

goalLine(Line, Team, Z0) :- holds(goalLine(Line, Team), Z0)





