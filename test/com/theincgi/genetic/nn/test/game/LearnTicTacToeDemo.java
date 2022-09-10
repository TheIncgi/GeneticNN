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
				1000, 
				10000
		);
		
		
		population.epoch();
		population.epoch();
	}
	
}
