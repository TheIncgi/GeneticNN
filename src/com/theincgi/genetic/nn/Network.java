package com.theincgi.genetic.nn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.theincgi.genetic.Entity;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.GeneHashBundle;

abstract public class Network {

	protected NeuronBundle neuronGenes;
	
	protected HashMap<Integer, Neuron> neurons;
	
	public Network(Random random, int inputs, int outputs) {
		this.neuronGenes = new NeuronBundle(random, inputs, outputs);
	}
	
	public Network(NeuronBundle neurons) {
		this.neuronGenes = neurons;
	}
	
	public Optional<Neuron> getInputNeuron( int inputNum ) {
		if( neuronGenes.getNeuronType(inputNum).isInput() )
			return getNeuron( inputNum );
		return Optional.empty();
	}
	public Optional<Neuron> getOutputNeuron( int outputNum ) {
		int id = neuronGenes.inputs + outputNum;
		if( neuronGenes.getNeuronType( id ).isOutput() )
			return getNeuron( id );
		return Optional.empty();
	}
	
	protected abstract Neuron createInputNeuron( int inputNum );
	
	public Optional<Neuron> getNeuron( int id ) {
		if(!neurons.containsKey( id ) ) {
			var type = neuronGenes.getNeuronType( id );
			if( type == null ) return Optional.empty();
			switch( type ) {
				case HIDDEN: 
				case OUTPUT: {
					var genes = neuronGenes.getNeuron( id );
					if( genes.isPresent() ) {
						var neuron = new Neuron(this, genes.get());
						neurons.put(id, neuron);
					}
				} break;
				case INPUT:
					neurons.put(id, createInputNeuron(id));
					break;
			}
			
		}
		return Optional.of(neurons.get(id));
	}
	
	public void loadAllNeurons() {
		for( int id : neuronGenes.getIDs() )
			getNeuron(id);
	}
	
	public void update() {
		for( var n : neurons.values() ) //TODO hash order may be source of uncontrolled random
			n.update();
	}

	public int inputSize() {
		return neuronGenes.inputs;
	}
	
	public int outputSize() {
		return neuronGenes.outputs;
	}
	
	public List<Float> getOutputs() {
		ArrayList<Float> outputs = new ArrayList<>( outputSize() );
		for( int i = 0; i < outputSize(); i++ ) {
			outputs.add( getOutputNeuron(i).get().getOutput() );
		}
		return outputs;
	}
	
	public NeuronBundle getNeuronGenes() {
		return neuronGenes;
	}
	
}
