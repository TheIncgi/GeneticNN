package com.theincgi.genetic.nn;

import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import com.theincgi.genetic.Entity;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.GeneHashBundle;

public class Network {

	NeuronBundle neuronGenes;
	
	protected HashMap<Integer, Neuron> neurons;
	
	
	public Network(NeuronBundle neurons) {
		this.neuronGenes = neurons;
	}
	
	
	public Optional<Neuron> getNeuron( int id ) {
		if(!neurons.containsKey( id ) ) {
			var genes = neuronGenes.getNeuron( id );
			if( genes.isPresent() ) {
				var neuron = new Neuron(this, genes.get());
				neurons.put(id, neuron);
			}
		}
		return Optional.of(neurons.get(id));
	}
	
	
}
