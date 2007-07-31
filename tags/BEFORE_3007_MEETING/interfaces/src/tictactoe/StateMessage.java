package tictactoe;

import rollerslam.infrastructure.server.Message;

/**
 * Carries the current game state
 * 
 * @author maas
 */
@SuppressWarnings("serial")
public class StateMessage implements Message {

	public BoardState state;
		
}
