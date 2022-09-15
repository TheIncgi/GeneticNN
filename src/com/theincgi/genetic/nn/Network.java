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

	protected NeuronBundle neuronBundle;
	
	protected HashMap<Integer, Neuron> neurons = new HashMap<>();
	
	public Network(Random random, int inputs, int outputs) {
		this.neuronBundle = new NeuronBundle(random, inputs, outputs);
		loadAllNeurons();
	}
	
	public Network(NeuronBundle neurons) {
		this.neuronBundle = neurons;
		loadAllNeurons();
	}
	
	public Optional<Neuron> getInputNeuron( int inputNum ) {
		if( neuronBundle.getNeuronType(inputNum).isInput() )
			return getNeuron( inputNum );
		return Optional.empty();
	}
	public Optional<Neuron> getOutputNeuron( int outputNum ) {
		int id = neuronBundle.inputs + outputNum;
		if( neuronBundle.getNeuronType( id ).isOutput() )
			return getNeuron( id );
		return Optional.empty();
	}
	
	protected abstract Neuron createInputNeuron( int inputNum );
	
	public Optional<Neuron> getNeuron( int id ) {
		if(!neurons.containsKey( id ) ) {
			var type = neuronBundle.getNeuronType( id );
			if( type == null ) return Optional.empty();
			switch( type ) {
				case HIDDEN: 
				case OUTPUT: {
					var genes = neuronBundle.getNeuron( id );
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
		return Optional.ofNullable(neurons.get(id));
	}
	
	public void loadAllNeurons() {
		for( int id : neuronBundle.getIDs() )
			getNeuron(id);
	}
	
	public void update() { ///TODO CONCURRENT MOD??
		for( int i = 0; i < inputSize(); i++ )
			neurons.get( Integer.toString(i) ).update();
		
		for( var n : neurons.values() ) {
			n.update();
		}
	}
	
	public void reset() {
		for( var n : neurons.values() ) //TODO hash order may be source of uncontrolled random
			n.reset();
	}

	public int inputSize() {
		return neuronBundle.inputs;
	}
	
	public int outputSize() {
		return neuronBundle.outputs;
	}
	
	public List<Float> getOutputs() {
		ArrayList<Float> outputs = new ArrayList<>( outputSize() );
		for( int i = 0; i < outputSize(); i++ ) {
			outputs.add( getOutputNeuron(i).get().getOutput() );
		}
		return outputs;
	}
	
	public NeuronBundle getNeuronBundle() {
		return neuronBundle;
	}
	
}
