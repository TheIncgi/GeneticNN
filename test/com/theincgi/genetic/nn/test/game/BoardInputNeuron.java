package com.theincgi.genetic.nn.test.game;

import com.theincgi.genetic.nn.Neuron;

public class BoardInputNeuron extends Neuron {
	
	private TicTacToeAgent agent;
	public final int x, y;
//	float output = 0;
	
	public BoardInputNeuron( TicTacToeAgent agent, int x, int y ) {
		this.agent = agent;
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void update() {
		var ttt = agent.getGame();
		var value = ttt.board[x][y];
		if( value == null )
			output = 0;
		else if( value.equals(agent.getPlayer()) )
			output = 1;
		else
			output = -1;
	}
	
}
