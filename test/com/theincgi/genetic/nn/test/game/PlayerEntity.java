package com.theincgi.genetic.nn.test.game;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

import com.theincgi.genetic.Entity;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.Population;
import com.theincgi.genetic.nn.NeuronBundle;
import com.theincgi.genetic.nn.NeuronGenes;
import com.theincgi.genetic.nn.test.game.TicTacToe.Player;

public class PlayerEntity extends Entity {
	
	protected TicTacToeAgent agent;
	protected Random random;
	protected int gamesPlayed = 0;
	
	public PlayerEntity( GeneBundle geneBundle ) {
		super( geneBundle );
		agent = new TicTacToeAgent( random = geneBundle.getRandom() );
		
		updateGeneParenting();
	}
	public PlayerEntity( Random random ) {
		super();
		agent = new TicTacToeAgent(this.random = random);
		setGenes( agent.getNeuronBundle() );
		
		updateGeneParenting();
	}
	
	@Override
	public void live( Population population ) {
		for( int i = 0; i < 12; i++) {
			var opponent = (PlayerEntity)population.getRandom();
			playGame( opponent, true );
		}
	}
	
	@Override
	public void reset() {
		score = 0f;
		gamesPlayed = 0;
	}
	
	public TicTacToe playGame( PlayerEntity opponent, boolean doScoring ) {
		TicTacToe game = new TicTacToe(random);
		agent.setGame(game);
		opponent.agent.setGame(game);
		
		for(int m = 0; m < 9; m++) {
			if( game.isGameDone() ) break;
			if( game.getTurn().equals(Player.P1) )
				agent.takeTurn();
			else if( game.getTurn().equals(Player.P2) )
				opponent.agent.takeTurn();
		}
		if( doScoring ) {
			score += game.getScore(Player.P1);
			opponent.score += game.getScore(Player.P2);
		}
		gamesPlayed++;
		opponent.gamesPlayed++;
		return game;
	}
	
	@Override
	public Float getScore() {
		return score / gamesPlayed 
				+ Math.min( ((NeuronBundle)getGenes()).sizeConnections(), 40 ) * 100 
//				- age 
//				- score < 30 ? 0 : ((NeuronBundle)getGenes()).sizeHidden() / 30f 
//				- score < 30 ? 0 : ((NeuronBundle)getGenes()).sizeConnections() / 60f;
				;
	}
	
	public record PlayerStats( float maxMutation, float avgMutation, int inputs, int outputs, int hidden, int connections, float strongestWeight,  float score ) {}
	public PlayerStats getStats() {
		var ng = (NeuronBundle) getGenes();
		return new PlayerStats(
				ng.getMaxMutationChance(), 
				ng.getAvgMutationChance(),
				ng.inputs,
				ng.outputs,
				ng.sizeHidden(),
				ng.sizeConnections(),
				ng.strongestWeight(),
				getScore()
		);
	}
}
