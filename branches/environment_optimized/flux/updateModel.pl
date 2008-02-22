%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%      Ramification          %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

** The following idea is update the Fluent Object speed based on his acceleration.

causes(acceleration(FluentObject,vector(Ax,Ay)), speed(FluentObject,vector(Vx,Vy)),Z):-
                                                 Vx #= Ax * 2,
                                                 Vy #= Ay * 2.

** The following idea is update the Fluent Object position based on hid speed.

causes(speed(FluentObject, vector(Vx,Vy)), position(FluentObject,vector(Sx,Sy)),Z):-
                                                 Sx #= Sx * 2,
                                                 Sy #= Sy * 2.
                                                 
causes(position(FluentObject,vector(Sx,Sy)),position(FluentObject,vector(NSx,NSy))).