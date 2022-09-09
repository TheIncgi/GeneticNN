package com.theincgi.genetic.nn;

import java.util.Random;
import java.util.function.Supplier;

import com.theincgi.genetic.FloatGene;
import com.theincgi.genetic.GeneArrayBundle;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.GeneHashBundle;
import com.theincgi.genetic.OptionGene;

public class Neuron extends GeneBundle {
	
	public final int id;
	GeneArrayBundle connections;
	GeneArrayBundle gateConnections;
	
	FloatGene bias;
	FloatGene gateBias;
	
	OptionGene<GateEnable> gateEnabled;
	private Supplier<Connection> connectionFactory;
	
	public Neuron( Random random, NeuronBundle connectionOptions ) {
		super(random);
		
		this.connectionFactory = ()->{
			Connection c = new Connection(random, connectionOptions.getIDs());
			return c;
		};
	}
	
	
	
}
