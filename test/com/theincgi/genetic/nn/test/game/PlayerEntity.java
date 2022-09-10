package com.theincgi.genetic.nn.test.game;

import java.util.Optional;
import java.util.Random;

import com.theincgi.genetic.Entity;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.Population;
import com.theincgi.genetic.nn.NeuronBundle;

public class PlayerEntity extends Entity {
	
	protected TicTacToeAgent agent;
	
	
	public PlayerEntity( GeneBundle geneBundle ) {
		super( geneBundle );
		agent = new TicTacToeAgent( geneBundle.getRandom() );
		 
		getGenes().updateParenting(Optional.empty());
	}
	public PlayerEntity( Random random ) {
		super();
		agent = new TicTacToeAgent(random);
		setGenes( agent.getNeuronGenes() );
		
		getGenes().updateParenting(Optional.empty());
	}
	
	@Override
	public void live( Population populaton ) {
		for( int i = 0; i < 4; i++) {
			var opponent = (PlayerEntity)populaton.getRandom();
			for(int m = 0; m < 10; m++) {
				//TODO fix m, do turns
			}
			//TODO fetch & combine score
		}
	}
	
	@Override
	public void reset() {
		score = 0f;
	}
	
	
	
}
