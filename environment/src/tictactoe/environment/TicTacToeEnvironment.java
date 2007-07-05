package tictactoe.environment;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.infrastructure.annotations.agent;
import rollerslam.infrastructure.annotations.message;
import rollerslam.infrastructure.server.EnvironmentCycleProcessor;

public @agent interface TicTacToeEnvironment extends Serializable, EnvironmentCycleProcessor, Remote {

	@message void movePiece(Marker marker, int positionX, int positionY) throws RemoteException;
	
}
