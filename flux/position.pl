:- ['utils'].

%% define the static objects position, being as the center field (0,0) and the measure unit mm.

%% Must be in the In Track
 isOnInTrack(vector(Sx,Sy)):-
                  isPointInEllipse(188000, -642206, 0, 642206, 0, Sx, Sy).
                  
 %% Must be in the Field.
 isOnField(vector(Sx,Sy)):-
                  isPointInEllipse(156000, -642206, 0, 642206, 0, Sx, Sy).


 %% Must be in the Out Track, that is, the ring.
 isOnOutTrack(vector(Sx,Sy)):-
                   isOnInTrack(vector(Sx,Sy)),
                   not isOnField(vector(Sx,Sy)).

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% From now on, the functions are splitted in West and East%%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

 %% Must be in the In West Tramp.
 isOnInWestTramp(vector(Sx,Sy)):-
                   isPointInEllipse(10500, F1x, F1y, F2x, F2y, Sx, Sy).
                   
 %% Must be in the In East Tramp.
 isOnInEastTramp(vector(Sx,Sy)):-
                   isPointInEllipse(10500, F1x, F1y, F2x, F2y, Sx, Sy).
                   
 %% Must be in the Out West Tramp.
 isOnOutWestTramp(vector(Sx,Sy)):-
                   isPointInEllipse(14000, F1x, F1y, F2x, F2y, Sx, Sy),
                   not isOnInWestTramp(vector(Sx,Sy)).
                   
 %% Must be in the Out East Tramp.
 isOnOutEastTramp(vector(Sx,Sy)):-
                   isPointInEllipse(14000, F1x, F1y, F2x, F2y, Sx, Sy),
                   not isOnInEastTramp(vector(Sx,Sy)).
                   
 %% Must be in the West Ramp Edge.
 isOnWestEdge(vector(Sx,Sy)):-
                  isPointInEllipse(15500, F1x, F1y, F2x, F2y, Sx, Sy),
                  not isOnOutWestTramp(vector(Sx,Sy))).
                  
 %% Must be in the East Ramp Edge.
 isOnEastEdge(vector(Sx,Sy)):-
                  isPointInEllipse(15500, F1x, F1y, F2x, F2y, Sx, Sy),
                  not isOnOutEastTramp(vector(Sx,Sy))).

 %% Must be in the West Ramp.
 isOnWestRamp(vector(Sx,Sy)):-
                 isPointInEllipse(18500, F1x, F1y, F2x, F2y, Sx, Sy),
                 not isOnWestEdge(vector(Sx,Sy)).
                 
 %% Must be in the East Ramp.
 isOnEastRamp(vector(Sx,Sy)):-
                 isPointInEllipse(18500, F1x, F1y, F2x, F2y, Sx, Sy),
                 not isOnEastEdge(vector(Sx,Sy)).
                 
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

