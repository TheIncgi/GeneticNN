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
	
	public TicTacToe(Random random) {
		 turn = RandomUtils.pickRandom(random, Player.values());
	}
	
	public boolean containsMark( int x, int y ) {
		return board[x][y] != null;
	}
	
	/**Return true if it was a valid move*/
	public boolean doTurn( int x, int y ) {
		if( !hasEmpty() )
			return false;
		if( hasWinner() )
			return false;
		if( turn == null )
			return false;
		
		if( containsMark(x, y)  ) {
			scores[turn.ordinal()] -= 100;
			return false;
		}
		scores[turn.ordinal()]++;
		board[x][y] = turn;
		
		if( hasWinner() ) {
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
	
	public boolean hasWinner() {
		if( winner != null )
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
