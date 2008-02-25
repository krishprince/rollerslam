%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%      Ramification          %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% The Vectors components are definined in the following way:
%% For a Vector with module V:
%% Vx = V Cos @
%% Vy = V Sin @
%% Where Vx is the component in axis x, Vy is the component is axis y and @ = 45�
%% And to calculate a module based on his components: V = sqrt(Vx� + Vy�)

%% The following idea is update the Fluent Object speed based on his acceleration.

causes([acceleration(FluentObject,vector(Ax,Ay)),speed(FluentObject,vector(Vix, Viy)),position(FluentObject,vector(Six, Siy))],
       [speed(FluentObject,vector(Vx,Vy)),position(FluentObject,vector(NewSx,NewSy))],Z):-
                                                  VectorA #= sqrt((Ax*Ax)+(Ay*Ay)),
                                                  Vx #= Vix + VectorA * 2,
                                                  Vy #= Viy + VectorA * 2,
                                                  VectorV #= sqrt((Vx*Vx) + (Vy*Vy)),
                                                  Sx #= Six + Vix * 2 + (VectorA * 4)/2,
                                                  Sy #= Siy + Viy * 2 + (VectorA * 4)/2,
                                                  existsSomeone(vector(Sx,Sy),Z,vector(NewSx, NewSy)).

%% The following idea is update the position by considering that the player is jumping horizontally on the field

%% Sx(t) = (Vix Cos @) t
%% Sy(t) = (Viy Cos @) t
%% Hy(t)= (Vi Sin @)t - (gt)�div 2
%% @ = 45�, t=2, g=10

causes(jumpingH(FluentObject,vector(Vx,Vy)),position(FluentObject,vector(NewSx,NewSy)),Z):-
                                                         Sx #= Vx * 0.71 * 2,
                                                         Sy #= Vy * 0.71 * 2,
                                                         existsSomeone(vector(Sx,Sy),Z,vector(NewSx, NewSy))..

%% The following idea is update the position by considering that the player is jumping vertically on the ramp

%% Sx(t) = (Vix Cos @) t
%% Sy(t) = (Viy Cos @) t
%% Hy(t)= (Vi Sin @)t - (gt)�div 2
%% @ = 90�, t=2, g=10

causes(jumpingV(FluentObject,vector(Six,Siy)),position(FluentObject,vector(Sx,Sy)),Z):-
                                                         Sx = Six,
                                                         Sy = Siy.
                                                         
%% To check if exist someone in the new localization. In this case, the position is updated to a near location.

existsSomeone(vector(Sx,Sy),State,vector(NewSx, NewSy)):-
                                                    (
                                                    holds(position(_,vector(Sx,Sy)),State),
                                                    NewSx #= Sx + 1,
                                                    NewSy #= Sy + 1
                                                     );
                                                    (NewSx = Sx, NewSy = Sy).
                                                    
%%  To change th direction


