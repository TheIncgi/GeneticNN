package com.theincgi.genetic.nn.test.reallyBasic;

import java.util.Random;
import java.util.concurrent.ExecutionException;

import com.theincgi.genetic.Entity;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.Population;
import com.theincgi.genetic.nn.Network;
import com.theincgi.genetic.nn.Neuron;
import com.theincgi.genetic.nn.NeuronBundle;
import com.theincgi.genetic.nn.visualize.NetworkVisualizer;

import javafx.application.Platform;

public class CopyInputDemoFX {
	
	protected static  NetworkVisualizer netVis; 
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		netVis = NetworkVisualizer.getInstance();
		
		Random random = new Random(1235);
		System.out.println("Generating population");
		Population pop = new Population(random, r->{ return new CopyEntity(r); }, gb->{ return new CopyEntity(gb); }, 50, 200);
		
		System.out.println("Gen 0 best:");
		showBest(pop);
		
		netVis.epoch.setOnAction(e->{
			pop.epoch();
			System.out.printf( "Gen %4d Best Score: %10.6f\n", pop.getGenerations(), pop.getBest().getScore() );
			showBest(pop);
		});
		
	}

	protected static void showBest(Population pop) {
		var best = (CopyEntity)pop.getBest();
		var net = new CopyNetwork((NeuronBundle)best.getGenes());
		var n = net.getOutputNeuron(0).get();
		System.out.println( n );
		for( int i = 0; i<10; i++ ) {
			float target = i / 10f;
			
			InputNeuron.value = target;
			
			net.reset();
			net.update();
//			net.update();
			
			float out = net.getOutputNeuron(0).get().getOutput();
			
			if(i==0)
				netVis.setNetwork( net );
			
			System.out.printf("%2d: out: %5.2f | target: %5.2f\n", i, out, target);
//			score += 1 - Math.abs( out - target );
		}
	}
	
	public static class CopyEntity extends Entity {
		
		public CopyEntity(Random random) {
			super( new NeuronBundle(random, 1, 1) );
			updateGeneParenting();
		}
		public CopyEntity(GeneBundle gb) {
			super( gb );
			updateGeneParenting();
		}
		
		@Override
		public void live(Population population) {
			CopyNetwork net = new CopyNetwork( (NeuronBundle)getGenes() );
			
			for( int i = 0; i<10; i++ ) {
				float target = i / 10f;
				
				InputNeuron.value = target;
				
				net.update();
				net.update();
				
				float out = net.getOutputNeuron(0).get().getOutput();
				
				score += 1 - Math.abs( out - target );
			}
		}

		@Override
		public void reset() {
			score = 0f;
		}
		
	}
	
	public static class CopyNetwork extends Network {
		public CopyNetwork(Random random) {
			super(random,1,1);
		}
		public CopyNetwork(NeuronBundle neurons) {
			super(neurons);
		}
		
		@Override
		protected Neuron createInputNeuron(int inputNum) {
			return new InputNeuron();
		}
	}
	
	public static class InputNeuron extends Neuron {
		public static float value = 0;
		public InputNeuron() {
		}
		
		@Override
		public void update() {
		}
		
		@Override
		public float getOutput() {
			return value;
		}
		
	}
	
}
