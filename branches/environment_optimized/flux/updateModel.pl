%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%      Ramification          %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% The Vectors components are defininf in the following way
%% For a Vector with module V:
%% Vx = V Cos @
%% Vy = V Sin @
%% Where Vx is the component in axis x, Vy is the component is axis y and @ = 45º
%% And to calculate a module based on his components: V = sqrt(Vx² + Vy²)

%% The following idea is update the Fluent Object speed based on his acceleration.

causes(acceleration(FluentObject,vector(Ax,Ay)), speed(FluentObject,vector(Vx,Vy)),Z):-
                                                 VectorA #= sqrt((Ax*Ax)+(Ay*Ay))
                                                 Vx #= Ax * 2,
                                                 Vy #= Ay * 2.

%% The following idea is update the Fluent Object position based on hid speed.

causes(speed(FluentObject, vector(Vx,Vy)), position(FluentObject,vector(Sx,Sy)),Z):-
                                                 Sx #= Sx * 2,
                                                 Sy #= Sy * 2.
                                                 
causes(position(FluentObject,vector(Sx,Sy)),position(FluentObject,vector(NSx,NSy))).

%% The following idea is update the position by considering that the player is jumping horizontally on the field

%% Sx(t) = (Vix Cos @) t
%% Sy(t) = (Viy Cos @) t
%% Hy(t)= (Vi Sin @)t - (gt)²div 2
%% @ = 45º, t=2, g=10

causes(jumpingH(FluentObject,vector(Vx,Vy)),position(FluentObject,vector(Sx,Sy)),Z):-
                                                         Sx #= Vx * 0.71 * 2,
                                                         Sy #= Vy * 0.71 * 2.

%% The following idea is update the position by considering that the player is jumping vertically on the ramp

%% Sx(t) = (Vix Cos @) t
%% Sy(t) = (Viy Cos @) t
%% Hy(t)= (Vi Sin @)t - (gt)²div 2
%% @ = 90º, t=2, g=10

causes(jumpingV(FluentObject,vector(Six,Siy)),position(FluentObject,vector(Sx,Sy)),Z):-
                                                         Sx = Six,
                                                         Sy = Siy.

