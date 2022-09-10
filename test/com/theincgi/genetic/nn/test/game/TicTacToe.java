package com.theincgi.genetic.nn.test.game;

import java.util.EnumMap;
import java.util.Random;

import com.theincgi.commons.RandomUtils;

public class TicTacToe {
	
	
	enum Player {
		P1, P2;
		public Player next() {
			return switch (this) {
				case P1: yield P2;
				case P2: yield P1;
				default: yield null;
			};
		}
	}
	
	/*[x][y]*/
	Player[][] board = new Player[3][3];
	
	private float[] scores = new float[2];
	Player turn;
	Player winner = null;

	private int failures = 0;
	
	public TicTacToe(Random random) {
		 turn = RandomUtils.pickRandomOfVarargs(random, Player.values());
	}
	
	public boolean containsMark( int x, int y ) {
		return board[x][y] != null;
	}
	
	/**Return true if it was a valid move*/
	public boolean doTurn( int x, int y ) {
		if( !hasEmpty() )
			return false;
		if( isGameDone() )
			return false;
		if( turn == null )
			return false;
		if( failures == 2 )
			return false; //game is dead
		
		if( containsMark(x, y)  ) {
			scores[turn.ordinal()] -= 100;
			failures ++;
			turn = turn.next();  //if the other agent can place a move that this one couldn't, it gets a 5pt bonus
			return false;
		}
		scores[turn.ordinal()] += 1 + (5*failures);
		board[x][y] = turn;
		
		if( isGameDone() ) {
			scores[ turn.ordinal() ] += 1000; //gg
			turn = null;
		}else {
			turn = turn.next();
		}
		
		return true;
	}
	
	public boolean hasEmpty() {
		for( var col : board )
			for( var x : col )
				if( x == null )
					return true;
		return false;
	}
	
	public Player getWinner() {
		return winner;
	}
	
	public boolean isGameDone() {
		if( winner != null || failures >= 2 )
			return true;
		
		for( int i = 0; i < 3; i++) {
			if( checkLine(i, 0, 0, 1) ) { //down
				winner = board[i][0];
				return true;
			}
			if( checkLine(0, i, 1, 0)) { //right
				winner = board[0][i];
				return true;
			}
		}
		if( checkLine(0,0,1,1) ) {
			winner = board[0][0];
			return true;
		}
		
		if( checkLine(2, 0, -1, 0) ) {
			winner = board[2][0];
			return true;
		}
		
		return false;
	}
	
	protected boolean checkLine( int x, int y, int dx, int dy ) {
		return board[x][y].equals( board[x+dx][y+dy]) &&
			   board[x][y].equals( board[x+dx*2][y+dy*2] );
	}
	
	public Player getTurn() {
		return turn;
	}
	
	public float getScore( Player p ) {
		return scores[p.ordinal()];
	}
	
}
