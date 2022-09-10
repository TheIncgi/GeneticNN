package com.theincgi.genetic.nn.test.game;

import static com.theincgi.commons.CollectionUtils.bestMatch;

import java.util.Random;

import com.theincgi.commons.CollectionUtils;
import com.theincgi.commons.Pair;
import com.theincgi.genetic.nn.Network;
import com.theincgi.genetic.nn.Neuron;

public class TicTacToeAgent extends Network {

	TicTacToe.Player player;
	private TicTacToe game;

	public TicTacToeAgent( Random random ) {
		super( random, 9, 9 );
	}
	
	public TicTacToe.Player getPlayer() {
		return player;
	}
	
	public void takeTurn() {
		for(int i = 0; i<4; i++) //# of updates might be better as a gene with a penalty for higher values
			update();
		var best = bestMatch(getOutputs(), (a,b)->{return a < b;});
		int x = indexToX( best.t() );
		int y = indexToY( best.t() );
		
		game.doTurn(x, y);
	}
	
	@Override
	protected Neuron createInputNeuron(int inputNum) {
		int x = indexToX( inputNum );
		int y = indexToY( inputNum );
		return new BoardInputNeuron( this, x, y );
	}
	
	//[x][y]
	public static int indexToX( int index ) {
		return index / 3;
	}
	public static int indexToY( int index ) {
		return index % 3;
	}

	public TicTacToe getGame() {
		return game;
	}
	
	public void setGame(TicTacToe game) {
		this.game = game;
	}
	
}
