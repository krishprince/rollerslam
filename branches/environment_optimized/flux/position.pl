:- ['utils'].

%% define the static objects position.


%% Functions to find out where is the agent.

 %% Must be out of Field.
 isOnInnerTrack(vector(Sx,Sy)):-
                  not isPointInEllipse(MajorAxisField, F1x, F1y, F2x, F2y, Sx, Sy).


 %% Must be in the Field and out of Edge, OutTramp, InnerTramp and Ramp.
 isOnField(vector(Sx,Sy)):-
                  not isPointInEllipse(MajorAxisEdge, F1x, F1y, F2x, F2y, Sx, Sy),
                  not isPointInEllipse(MajorAxisOutTramp, F1x, F1y, F2x, F2y, Sx, Sy),
                  not isPointInEllipse(MajorAxisInnerTramp, F1x, F1y, F2x, F2y, Sx, Sy),
                  not isPointInEllipse(MajorAxisRamp, F1x, F1y, F2x, F2y, Sx, Sy),
                  isPointInEllipse(MajorAxisField, F1x, F1y, F2x, F2y, Sx, Sy).


 %% Must be in the Ramp and out of Edge, OutTramp and InnerTramp.
 isOnRamp(vector(Sx,Sy)):-
                 not isPointInEllipse(MajorAxisEdge, F1x, F1y, F2x, F2y, Sx, Sy),
                 not isPointInEllipse(MajorAxisOutTramp, F1x, F1y, F2x, F2y, Sx, Sy),
                 not isPointInEllipse(MajorAxisInnerTramp, F1x, F1y, F2x, F2y, Sx, Sy),
                 isPointInEllipse(MajorAxisRamp, F1x, F1y, F2x, F2y, Sx, Sy).


 %% Must be in the Edge and out of OutTramp and InnerTramp.
 isOnEdge(vector(Sx,Sy)):-
                  not isPointInEllipse(MajorAxisOutTramp, F1x, F1y, F2x, F2y, Sx, Sy),
                  not isPointInEllipse(MajorAxisInnerTramp, F1x, F1y, F2x, F2y, Sx, Sy),
                  isPointInEllipse(MajorAxisEdge, F1x, F1y, F2x, F2y, Sx, Sy).


 %% Must be in the InnerTramp.
 isOnInnerTramp(vector(Sx,Sy)):-
                       isPointInEllipse(MajorAxisInTramp, F1x, F1y, F2x, F2y, Sx, Sy).


 %% Must be in the OutTramp and out of InnerTramp.
 isOnOuterTramp(vector(Sx,Sy)):-
                       not isPointInEllipse(MajorAxisInTramp, F1x, F1y, F2x, F2y, Sx, Sy),
                       isPointInEllipse(MajorAxisOutTramp, F1x, F1y, F2x, F2y, Sx, Sy).
