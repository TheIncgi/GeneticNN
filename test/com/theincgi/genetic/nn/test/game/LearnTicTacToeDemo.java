package com.theincgi.genetic.nn.test.game;

import java.util.Random;

import com.theincgi.genetic.Population;

public class LearnTicTacToeDemo {
	
	public static void main(String[] args) {
		Random random = new Random(1235);
		Population population = new Population(
				random, 
				(r)->{ return new PlayerEntity(r); }, 
				(gb)->{ return new PlayerEntity(gb); }, 
				200, 
				1000
		);
		
//		population.setNumParents(2);
		
		for( int i = 0; i < 30000; i++ ) {
			System.out.println( "Gen: " + (population.getGenerations() + 1) );
			population.epoch();
			PlayerEntity a = (PlayerEntity)population.getBest();
			showEntityStats(a);
			for( int j = 0; j < 3; j++) {
				PlayerEntity b = (PlayerEntity)population.getBetterRandom();
				showGame(a, b);
			}
		}
	}
	
	
	public static void showGame( PlayerEntity a, PlayerEntity b ) {
		System.out.println( a.playGame( b, false ) );
		System.out.println();
	}
	
	public static void showEntityStats( PlayerEntity e ) {
		var stats = e.getStats();
		System.out.printf(
			"age:           %d\n"+
			"maxMutation:   %6.2f\n" +
			"avgMutation:   %6.2f\n" + 
			"In/Hidden/Out: %3d / %3d / %3d\n" + 
			"Connections:   %d\n" +
			"Strongest Wt:  %6.2f\n" +
			"Games Played:  %d\n" +
			"Score:         %6.2f\n",
			e.age,
			stats.maxMutation(), stats.avgMutation(),
			stats.inputs(), stats.hidden(), stats.outputs(),
			stats.connections(),
			stats.strongestWeight(),
			e.gamesPlayed,
			stats.score()
		);
	}
}
