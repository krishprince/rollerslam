package tictactoe.display;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.display.Display;
import rollerslam.infrastructure.server.Message;
import tictactoe.StateMessage;

public class TicTacToeDisplay implements Display {

	public void update(Message arg0) throws RemoteException {
		System.out.println("\n" + ((StateMessage)arg0).state.toString());
	}

	public static void main(String[] args) throws RemoteException, AlreadyBoundException {
		ClientFacadeImpl.init(args[0]);		
		ClientFacade facade = ClientFacadeImpl.getInstance();
		
		facade.getDisplayRegistry().register((Display) facade.exportObject(new TicTacToeDisplay()));		
	}
}
