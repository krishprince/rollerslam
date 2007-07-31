package tictactoe.agent;


import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import rollerslam.infrastructure.annotations.agent;
import rollerslam.infrastructure.annotations.message;
import tictactoe.BoardState;
import tictactoe.Marker;

public @agent interface TicTacToeAgent extends Serializable, Remote {
	@message void boardStateChanged(BoardState state) throws RemoteException;
	@message void youWon() throws RemoteException;
	@message void youLost() throws RemoteException;
	@message void wrongMove() throws RemoteException;
	@message void gameStarted(Marker you) throws RemoteException;
}
