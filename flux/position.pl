:- ['flux'].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Functions to find out where is the agent %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

 %% when the agent is in the smallest ellipse on the field, but they are not at any other static object
 %% like the ramps, the tramps, and so on.
 isOnField(S).

 %% the agent is in the donnut where the players skate
 isOnInnerTrack(S).

 %% the agent is at the Ramp
 isOnRamp(S).

 %% the agent is at the Ramps Edge
 isOnEdge(S).

 %% the agent is at the internal tramp
 isOnInnerTramp(S).

 %% the agent is at the external tramp
 isOnOuterTramp(S).
