:- ['position'].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%      Ramification          %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

%% The Vectors components are definined in the following way:
%% For a Vector with module V:
%% Vx = V Cos @
%% Vy = V Sin @
%% Where Vx is the component in axis x, Vy is the component is axis y and @ = 45º
%% And to calculate a module based on his components: V = sqrt(Vx² + Vy²)
%% The following idea is update the Fluent Object speed and position based on his acceleration.

speedPositionRamification(FluentObject, Ax, Ay, NewVx, NewVy, NewSx, NewSy, Z) :-
       holds(FluentObject@[speed->vector(Vix, Viy)], Z),
       holds(FluentObject@[position->vector(Six, Siy)], Z),
       Ax2 is (Ax*Ax),
       Ay2 is (Ay*Ay),
       VectorA is sqrt(Ax2 + Ay2),
       VectorA2 is VectorA * 2,
       NewVx is Vix + VectorA2,
       NewVy is Viy + VectorA2,
       Sx is Six + (Vix * 2) + VectorA2,
       Sy is Siy + (Viy * 2) + VectorA2,
       existsSomeone(vector(Six,Siy), vector(Sx,Sy),Z,vector(MidSx, MidSy)),
       outBoundary(vector(MidSx, MidSy),vector(NewSx,NewSy)).

%% The following idea is update the position by considering that the player is jumping horizontally on the field

%% Sx(t) = (Vix Cos @) t
%% Sy(t) = (Viy Cos @) t
%% Hy(t)= (Vi Sin @)t - (gt)²div 2
%% @ = 45º, t=2, g=10

jumpH(vector(Vx,Vy),CurrentState,vector(NewSx,NewSy)):-
       Sqr2 is sqrt(2),
       HalfSqr2 is Sqr2/2,
       Sx is Vx * 2 * HalfSqr2,
       Sy is Vy * 2 * HalfSqr2,
       existsSomeone(vector(Sx,Sy),CurrentState,vector(MidSx, MidSy)),
       outBoundary(vector(MidSx, MidSy),vector(NewSx,NewSy)).

%% The following idea is update the position by considering that the player is jumping vertically on the ramp

%% Sx(t) = (Vix Cos @) t
%% Sy(t) = (Viy Cos @) t
%% Hy(t)= (Vi Sin @)t - (gt)²div 2
%% @ = 90º, t=2, g=10

jumpV(vector(Six,Siy),CurrentState,vector(NewSx,NewSy)):-
       Sx = Six,
       Sy = Siy,
       existsSomeone(vector(Sx,Sy),CurrentState,vector(MidSx, MidSy)),
       outBoundary(vector(MidSx, MidSy),vector(NewSx,NewSy)).

%% To check if exist someone in the new localization. In this case, the position is updated to a near location.

existsSomeone(vector(Six,Siy),vector(Sx,Sy),State,vector(NewSx, NewSy)):-
       (
         holds(position(_,vector(NewSx,NewSy)),State),
         pertencesToLine(vector(Six,Siy),vector(Sx,Sy),vector(NewSx, NewSy)),
         Sx #= Sx + 1,
         Sy #= Sy + 1,
         existsSomeone(vector(Six,Siy),vector(Sx,Sy),State,vector(NewSx, NewSy))
        );
       (NewSx = Sx, NewSy = Sy).

%% Verifies if the point (Pi,Pf) belongs to the line that crosses the points (Xi,Yi) and (Xf,Yf)

pertencesToLine(vector(Xi,Yi), vector(Xf,Yf), vector(Pi,Pf)):-
       Dify is Yf - Yi,
       Difx is Xf - Xi,
       A is Dify / Difx,
       AXi is A * Xi,
       B is Yi - AXi,
       APi is A * Pi,
       Pf = APi + B,
       ((Xf >= Xi, Xf >= Pi);(Xf < Xi,Pi < Xi)),
       ((Yf >= Yi, Yf >= Pf);(Yf < Yi,Pf < Yi)).

%%  Colision response (what happens after the colision)

%% Newton principle of conservation of Kinetic Energy: the sum of their masses times their respective velocities before the impact is equal
%% to the sum of their masses times their respective velocities after the impact:
                                         %% m1v1i + m2v2i = m1v1f + m2v2f
%% Elatic colision - conservation of Kinetic Energy
%% Coeficiente of restitution: e = -(v1f - v2f)/(v1i - v2i)
%% Perfect elastic colision: e=1
%% No deformation/ No rotation

%% Considering the player impact, we keep the same vector direction and change the sense

outBoundary(vector(MidSx, MidSy),vector(NewSx,NewSy)):-
       (
        isOnInTrack(vector(MidSx,MidSy)),
        NewSx = MidSx,
        NewSy = MidSy
       );
       (
       NewSx is MidSx * (-1),
       NewSy is MidSy * (-1)
       ).



