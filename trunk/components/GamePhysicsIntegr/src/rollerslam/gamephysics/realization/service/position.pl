:- ['utils'].

%% define the static objects position, being as the center field (0,0) and the measure unit mm.

%% Must be in the In Track
 isOnInTrack(vector(Sx,Sy)):-
                  isPointInEllipse(188000, -63835, 0, 63835, 0, Sx, Sy).

 %% Must be in the Field.
 isOnField(vector(Sx,Sy)):-
                  isPointInEllipse(156000, -63835, 0, 63835, 0, Sx, Sy).


 %% Must be in the Out Track, that is, the ring.
 isOnOutTrack(vector(Sx,Sy)):-
                   isOnInTrack(vector(Sx,Sy)),
                   not isOnField(vector(Sx,Sy)).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% From now on, the functions are splitted in West and East%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

 %% Must be in the In West Tramp. (Left)
 isOnInWestTramp(vector(Sx,Sy)):-
                   isPointInEllipse(10500, -63835, 4950, -63835, -4950, Sx, Sy).

 %% Must be in the In East Tramp. (Right)
 isOnInEastTramp(vector(Sx,Sy)):-
                   isPointInEllipse(10500, 63835, 4950, 63835, -4950, Sx, Sy).
                   
 %% Must be in the Out West Tramp. (Left)
 isOnOutWestTramp(vector(Sx,Sy)):-
                   isPointInEllipse(14000, -63835, 6061, -63835, -6061, Sx, Sy),
                   not isOnInWestTramp(vector(Sx,Sy)).

 %% Must be in the Out East Tramp. (Right)
 isOnOutEastTramp(vector(Sx,Sy)):-
                   isPointInEllipse(14000, 63835, 6061, 63835, -6061, Sx, Sy),
                   not isOnInEastTramp(vector(Sx,Sy)).

 %% Must be in the West Ramp Edge. (Left)
 isOnWestEdge(vector(Sx,Sy)):-
                  isPointInEllipse(15500, -63835, 6481, -63835, -6481, Sx, Sy),
                  not isOnOutWestTramp(vector(Sx,Sy)).

 %% Must be in the East Ramp Edge. (Right)
 isOnEastEdge(vector(Sx,Sy)):-
                  isPointInEllipse(15500, 63835, 6481, 63835, -6481, Sx, Sy),
                  not isOnOutEastTramp(vector(Sx,Sy)).

 %% Must be in the West Ramp. (Left)
 isOnWestRamp(vector(Sx,Sy)):-
                 isPointInEllipse(18500, -63835, 7246, -63835, -7246, Sx, Sy),
                 not isOnWestEdge(vector(Sx,Sy)).
                 
 %% Must be in the East Ramp. (Right)
 isOnEastRamp(vector(Sx,Sy)):-
                 isPointInEllipse(18500, 63835, 7246, 63835, -7246, Sx, Sy),
                 not isOnEastEdge(vector(Sx,Sy)).
                 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%% Auxiliary Functions

inTramp(vector(Sx,Sy)):-  isOnInWestTramp(vector(Sx,Sy));
                          isOnInEastTramp(vector(Sx,Sy));
                          isOnOutWestTramp(vector(Sx,Sy));
                          isOnOutEastTramp(vector(Sx,Sy)).
