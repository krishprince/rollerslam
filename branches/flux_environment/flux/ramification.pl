:- ['util'].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%% RAMIFYING ANIMATED OBJECTS %%%%%%%%%%%%%%%            
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

ramify_animated_object(Z1, Object, Z2):-
   holds(acceleration(Object, vector(Ax0,Ay0)),Z1),
   holds(speed(Object, vector(Vx0,Vy0)),Z1),
   holds(position(Object, vector(Sx0,Sy0)),Z1),
   holds(maxSpeed(Object, Max),Z1),
  Vx1 is Vx0 + Ax0,
  Vy1 is Vy0 + Ay0,
  Sx1 is Sx0 + Vx0,
  Sy1 is Sy0 + Vy0,
  checkModule(vector(Vx1, Vy1), Max, VR),
  
  (
  
  (
     isPointNotInEllipse(188000, -63835, 0,  63835, 0, Sx1, Sy1),
    update(Z1,[acceleration(Object, vector(0,0)), speed(Object, vector(0,0))],
                      [acceleration(Object, vector(Ax0,Ay0)), speed(Object, vector(Vx0,Vy0))],Z2)
   )
  ;  
  (
  update(Z1,[ speed(Object, VR), position(Object, vector(Sx1,Sy1))],
                    [ speed(Object, vector(Vx0,Vy0)), position(Object, vector(Sx0,Sy0))], Z2)            
   )            
  ).
            
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%% RAMIFYING BALL %%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
            
ramify_ball(Z1,  Z3):-
  ramify_animated_object(Z1,ball,Z2),
  
  (
  
  (holds(hasBall(player(A)), Z2),
  holds(position(player(A), vector(Xa,Ya)), Z2),
  holds(position(ball, vector(Xba,Yba)), Z2),

  update(Z2, [position(ball, vector(Xa, Ya))], [position(ball, vector(Xba, Yba))], Z3))
  ; 
  
  (not holds(hasBall(player(_)), Z2),
  holds(speed(ball, vector(X,Y)), Z2),
  getModule(vector(X,Y), VR),
  VR > 0,
  holds(attrition(Attr), Z2),
  X2 is X / 1,
  Y2 is Y / 1,  
  update(Z2, [speed(ball, vector(X2, Y2))], [speed(ball, vector(X, Y))], Z3))
  
  ;
  
  (Z3 = Z2)
  
  ).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%% RAMIFYING OUT-OF-BOUNDARIES %%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%            

stop_object(Z1,Object,Z2) :- 
  holds(acceleration(Object, vector(Ax0,Ay0)),Z1),
  holds(speed(Object, vector(Vx0,Vy0)),Z1),
  update(Z1,[acceleration(Object, vector(0,0)), speed(Object, vector(0,0))],
            [acceleration(Object, vector(Ax0,Ay0)), speed(Object, vector(Vx0,Vy0))],Z2).

object_cantmove(Object, Z1) :- 
	 (Object = player(P), holds(inGround(player(P)),Z1)).

ramify_cantmove(Z1,Object,Z2) :- 
	(object_cantmove(Object,Z1), stop_object(Z1,Object,Z2)) 
	; 	
	(Z1=Z2). 
ramify_tackle(Z1, Object, Z2):- 
      (holds(counterTackle(Object), Z1),
      update(Z1,[],[counterTackle(Object)],Z2))
      ; 	
	(Z1=Z2).	

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%%%%%%%% RAMIFICATION %%%%%%%%%%%%%%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%            

ramify_object(Z1, Object, Z2):- (Object = player(_), ramify_object_player(Z1,Object, Z2)); (Object = ball, ramify_object_ball(Z1,Object, Z2)).
ramify_object_ball(Z1, ball, Z3):- ramify_ball(Z1, Z2), ramify_cantmove(Z2,ball, Z3). 
ramify_object_player(Z1, player(P), Z4):- Player = player(P), ramify_animated_object(Z1, Player, Z2), ramify_cantmove(Z2,Player, Z3), ramify_tackle(Z3, Player, Z4). 
