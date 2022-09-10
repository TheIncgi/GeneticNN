package com.theincgi.genetic.nn.test.game;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import com.theincgi.genetic.Entity;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.Population;
import com.theincgi.genetic.nn.NeuronBundle;
import com.theincgi.genetic.nn.test.game.TicTacToe.Player;

public class PlayerEntity extends Entity {
	
	protected TicTacToeAgent agent;
	protected Random random;
	
	public PlayerEntity( GeneBundle geneBundle ) {
		super( geneBundle );
		agent = new TicTacToeAgent( random = geneBundle.getRandom() );
		
		getGenes().updateParenting(Optional.empty());
	}
	public PlayerEntity( Random random ) {
		super();
		agent = new TicTacToeAgent(this.random = random);
		setGenes( agent.getNeuronGenes() );
		
		getGenes().updateParenting(Optional.empty());
	}
	
	@Override
	public void live( Population populaton ) {
		for( int i = 0; i < 4; i++) {
			TicTacToe game = new TicTacToe(random);
			var opponent = (PlayerEntity)populaton.getRandom();
			agent.setGame(game);
			opponent.agent.setGame(game);
			
			for(int m = 0; m < 9; m++) {
				if( game.isGameDone() ) break;
				if( game.getTurn().equals(Player.P1) )
					agent.takeTurn();
			}
			score += game.getScore(Player.P1);
			opponent.score += game.getScore(Player.P2);
		}
	}
	
	@Override
	public void reset() {
		score = 0f;
	}
	
	
	
}
