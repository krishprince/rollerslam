package tictactoe.agents;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.util.Random;



import rollerslam.agents.Agent;
import rollerslam.infrastructure.ProxiedAgent;
import rollerslam.infrastructure.client.ClientFacade;
import tictactoe.environment.BoardState;
import tictactoe.environment.Marker;
import tictactoe.environment.TicTacToeEnvironment;

public class TicTacToeRandomAgent implements TicTacToeAgent, Runnable {
	
	// agents memory
	private TicTacToeEnvironment environment;
	private BoardState currentBoardState;
	private Marker playerIAm;
	private boolean gameRunning = false;
	private boolean gameFinished = false;
	
	public TicTacToeRandomAgent(TicTacToeEnvironment env) {
		this.environment = env;		
	}
	
	public void boardStateChanged(BoardState state) {
		currentBoardState = state;
		
		System.out.println("BOARD RECEIVED!!");
		System.out.println("" + state);
	}

	public void gameStarted(Marker player) {
		playerIAm = player;
		gameRunning = true;
		gameFinished = false;

		System.out.println("GAME STARTED! I'M " + player);
	}

	public void wrongMove() {
		System.out.println("SACO!");
	}

	public void youLost() {
		gameRunning = false;		
		gameFinished = true;
		System.out.println("FUCK!!!");
	}

	public void youWon() {
		gameRunning = false;
		gameFinished = true;
		System.out.println("YOOOOOOOOOOHHHRA!!!");
	}

	public void run() {
		Random r = new Random();
		
		try {
			while (!gameFinished) {
				try {
					Thread.sleep(3000 + r.nextInt(1000));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (gameRunning && currentBoardState.currentPlayer == playerIAm) {
					System.out.println("THINKING...");
					int i, j;
					for (i = 0; i < currentBoardState.board.length; ++i) {
						for (j = 0; j < currentBoardState.board[i].length; ++j) {
							if (currentBoardState.board[i][j] == Marker.EMPTY) {
								System.out.println("PLAYING " + i + " " + j);

								environment.movePiece(playerIAm, i, j);
								break;
							}
						}
					}
				} else {
					System.out.println("WAITING...");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Game finished!!!!");
	}

	/**
	 * @param args
	 * @throws RemoteException
	 * @throws AlreadyBoundException
	 */
	public static void main(String[] args) throws RemoteException,
			AlreadyBoundException {
		ClientFacade.init(args[0]);

		ClientFacade server = ClientFacade.getInstance();
		TicTacToeRandomAgent realAgent = new TicTacToeRandomAgent(
				(TicTacToeEnvironment) server.getProxyForRemoteAgent(
						TicTacToeEnvironment.class, 
						server.getSimulationAdmin().getEnvironmentAgent()));
		
		Agent proxiedAgent = new ProxiedAgent(realAgent);
		
		Agent testAgent = (Agent) server.exportObject(proxiedAgent);

		server.getAgentRegistry().register(testAgent);
		
		new Thread(realAgent).run();
	}
	
}
