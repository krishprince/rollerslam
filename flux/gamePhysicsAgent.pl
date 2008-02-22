:- ['rollerslam'].

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%%          Spec for the Physics Agent         %%
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

gamePhyscisProcessAction(Z1,[],Z2):- Z2=Z1.

gamePhysicsProcessAction(Z1,[A|L],Z3):- state_update(Z1,A,Z2,[]),
                                        gamePhysics(Z2,[L],Z3).

