package com.theincgi.genetic.nn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringJoiner;

import com.theincgi.genetic.GeneHashBundle;
import com.theincgi.genetic.nn.ActivationFunctions.ActivationFunction;
import com.theincgi.genetic.nn.ActivationFunctions.NamedActivationFunction;

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
	
	
	protected float calculateOutput( GeneHashBundle connections, ActivationFunction activation, float bias ) {
		float sum = bias;
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
//		if( genes.isGateEnabled() ) {
//			gate = calculateOutput( genes.getGateConnections(), genes.getGateActivationFunction(), genes.getGateBias() );
//			if( gate < .5 )
//				return;
//		}
		
		output = calculateOutput( genes.getConnections(), genes.getActivationFunction(), genes.getBias() );
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
	
	public NamedActivationFunction getActivationFunction() {
		return genes.getActivationFunction();
	}
	
	public NamedActivationFunction getGateActivationFunction() {
		return genes.getGateActivationFunction();
	}
	
	public float getBias() {
		return genes.getBias();
	}
	
	public float getGateBias() {
		return genes.getGateBias();
	}
	
	public NeuronGenes getGenes() {
		return genes;
	}
	
	public boolean isGateEnabled() {
		return genes.isGateEnabled();
	}
	
	public Iterable<Connection> getConnections() {
		ArrayList<Connection> c = new ArrayList<>();
		genes.getConnections().getGenesIterable().forEach(g->{c.add((Connection) g);});
		return c;
	}
	
	@Override
	public String toString() {
		StringJoiner j = new StringJoiner(" + ", getActivationFunction() + "(", ")" );
		j.add("bias: "+getBias());
		
		for( var c : getConnections() ) {
			j.add( "[i%d] * %.3f".formatted(  c.connectionSourceID.getValue(), c.weight.getValue() ) );
		}
		
		return "Neuron[%d]_%s".formatted( getGenes().id, j.toString());
	}
}
