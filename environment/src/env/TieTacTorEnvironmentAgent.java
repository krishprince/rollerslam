package tictactoe.env;

import java.rmi.RemoteException;
import java.util.Set;
import java.util.Vector;

import rollerslam.agents.Agent;
import rollerslam.infrastructure.server.AgentRegistryExtended;
import rollerslam.infrastructure.server.ProxiedEnvironmentAgent;
import rollerslam.infrastructure.server.ServerFacade;
import test.environment.TestEnvironmentAgent;
import tictactoe.agents.TicTacToeAgent;

public class TieTacTorEnvironmentAgent implements TicTacToeEnvironment {

	BoardState currentBoard = new BoardState();
	boolean gameRunning = false;
	TicTacToeAgent[] agents = new TicTacToeAgent[2];
	
	Vector x;
	
	public void think() throws RemoteException {
		if (!gameRunning) {
			
			System.out.println("WAITING FOR PLAYERS!");
			Object[] regAgents = ((AgentRegistryExtended) ServerFacade
					.getInstance().getAgentRegistry()).getRegisteredAgents();
			
			System.out.println(ServerFacade.getInstance().getAgentRegistry()
					+ "AR");
			if (regAgents.length >= 2) {
				System.out.println("STARTING GAME!");

				agents[0] = (TicTacToeAgent) ServerFacade.getInstance()
						.getProxyForRemoteAgent(TicTacToeAgent.class,
								(Agent) regAgents[0]);
				agents[1] = (TicTacToeAgent) ServerFacade.getInstance()
						.getProxyForRemoteAgent(TicTacToeAgent.class,
								(Agent) regAgents[1]);
				
				gameRunning = true;
				
				currentBoard.currentPlayer = Marker.PLAYER_A;

				agents[0].gameStarted(Marker.PLAYER_A);
				agents[1].gameStarted(Marker.PLAYER_B);				

				agents[1].boardStateChanged(currentBoard);
				agents[0].boardStateChanged(currentBoard);
			} else {
				System.out.println("STILL " + regAgents.length + " player(s).");
			}
		} else {
			
		}
	}
	
	public void movePiece(Marker marker, int positionX, int positionY) throws RemoteException {
		TicTacToeAgent currentAgent = null;
		
		if (currentBoard.currentPlayer == Marker.PLAYER_A) {
			currentAgent = agents[0];
		} else {
			currentAgent = agents[1];			
		}
		
		if (marker == currentBoard.currentPlayer && positionX >= 0
				&& positionY >= 0 && positionX < currentBoard.board.length
				&& positionY < currentBoard.board[0].length &&
				currentBoard.board[positionX][positionY] == Marker.EMPTY) {
			currentBoard.board[positionX][positionY] = marker;
			currentBoard.currentPlayer = switchMarker(marker);
			
			for (TicTacToeAgent ag : agents) {
				ag.boardStateChanged(currentBoard);
			}
			
			System.out.println("BOARD CHANGED");
			System.out.println("" + currentBoard);
		} else {
			currentAgent.wrongMove();
		}
	}

	private Marker switchMarker(Marker marker) {
		if (marker == Marker.PLAYER_A){
			return Marker.PLAYER_B;
		} else if (marker == Marker.PLAYER_B){
			return Marker.PLAYER_A;			
		} else {
			return Marker.EMPTY;
		}
	}

	public static void main(String[] args) throws Exception {
		ServerFacade server = ServerFacade.getInstance();
		server.init(1099, new ProxiedEnvironmentAgent(new TieTacTorEnvironmentAgent()));
	}	

}
