package tictactoe.environment;

import java.io.Serializable;

public class BoardState implements Serializable {
	public Marker[][] board = new Marker[3][3];
	public Marker currentPlayer = Marker.EMPTY;
		
	public BoardState() {
		for(int i=0;i<board.length;++i) {
			for(int j=0;j<board[i].length;++j) {
				board[i][j] = Marker.EMPTY  ;
			}
		}
	}
	public BoardState clone() {
		BoardState state = new BoardState();
		for(int i=0;i<board.length;++i) {
			for(int j=0;j<board[i].length;++j) {
				state.board[i][j] = board[i][j];
			}
		}
		return state;
	}
	
	public String toString() {
		String ret = "";
		for(int i=0;i<board.length;++i) {
			for(int j=0;j<board[i].length;++j) {
				ret += "" + (board[i][j]==Marker.EMPTY? " " : 
							(board[i][j]==Marker.PLAYER_A? "A" :"B")) + "|";
			}
			ret += "\n";
		}
		return ret;
	}
}
