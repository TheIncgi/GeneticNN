package com.theincgi.genetic.nn;

import java.util.HashMap;

import com.theincgi.genetic.GeneHashBundle;
import com.theincgi.genetic.nn.ActivationFunctions.ActivationFunction;

public class Neuron {
	
	protected Network network;
	protected NeuronGenes genes;
	
	protected float output = 0;
	protected float gate = 1;
	
	public Neuron(Network network, NeuronGenes genes) {
		this.network = network;
		this.genes = genes;
	}
	
	/**Input neurons can use this, be sure to override update*/
	protected Neuron() {
	}
	
	
	protected float calculateOutput( GeneHashBundle connections, ActivationFunction activation ) {
		float sum = 0;
		for( var c : connections.getGenesIterable() ) {
			Connection conn = (Connection) c;
			int fromID = conn.getInputID();
			var optInput = network.getNeuron( fromID );
			
			if( optInput.isEmpty() ) continue; //possible an old gene may point to a non-existant input, skipped if so
			
			var input = optInput.get();
			
			sum += input.getOutput() * conn.getWeight();
		}
		
		return activation.apply( sum );
	}
	
	public void update() {
		if( genes.isGateEnabled() ) {
			gate = calculateOutput( genes.getGateConnections(), genes.getGateActivationFunction());
			if( gate < .5 )
				return;
		}
		
		output = calculateOutput( genes.getConnections(), genes.getActivationFunction() );
	}
	
	public void reset() {
		output = 0;
		gate = 1;
	}
	
	public float getOutput() {
		return output;
	}
	
	public float getGateOutput() {
		return gate;
	}
}
