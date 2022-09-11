package com.theincgi.genetic.nn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import com.theincgi.genetic.FloatGene;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.GeneHashBundle;
import com.theincgi.genetic.nn.ActivationFunctions.ActivationFunction;

public class NeuronBundle extends GeneHashBundle {
	
	public AtomicInteger nextID = new AtomicInteger(0);
	private final ArrayList<Integer> idList = new ArrayList<>();
	public ArrayList<ActivationFunction> activationFunctions;
	public final int inputs, outputs;
	
	public NeuronBundle(Random random, int inputs, int outputs, FloatGene addRemoveFactor) {
		super(random, null, addRemoveFactor);
		activationFunctions = loadActivationFunctions();
		this.inputs = inputs;
		this.outputs = outputs;
		nextID.set(inputs);
		setGeneFactory(mkGeneFactory());
		addOutputGenes();
	}


	public NeuronBundle(Random random, int inputs, int outputs ) {
		super(random, null);
		activationFunctions = loadActivationFunctions();
		this.inputs = inputs;
		this.outputs = outputs;
		nextID.set(inputs);
		setGeneFactory(mkGeneFactory());
		addOutputGenes();
	}
	
	public NeuronBundle( NeuronBundle copyFrom ) {
		super(copyFrom.random, copyFrom.getGeneFactory());
		this.activationFunctions = copyFrom.activationFunctions;
		this.inputs = copyFrom.inputs;
		this.outputs = copyFrom.outputs;
		this.nextID.set( copyFrom.nextID.get() );
		this.idList.addAll( copyFrom.idList );
		
		for( var g : copyFrom.getGenes().entrySet() )
			this.getGenes().put(g.getKey(), g.getValue().copy());
		
		addOutputGenes(); //probably won't need to do anything, unless new inputs were adding later
	}
	
	protected Supplier<NamedGene> mkGeneFactory() {
		return ()->{
			try {
				return new NamedGene( Integer.toString( nextID.get()), new NeuronGenes(random, this) );
			}finally {
				nextID.getAndIncrement();
			}
		};
	}
	
	protected void addOutputGenes() {
		while( nextID.get() < inputs + outputs ) {
			addGene();
		}
	}
	
	public List<Integer> getIDs() {
		//must always be same list instance, used as options for connection OptionGene
		return idList;
	}
	
	/**null or type of neuron*/
	public NeuronType getNeuronType( int id ) {
		if( id < 0) return null;
		if( id < inputs ) return NeuronType.INPUT;
		if( id < inputs + outputs ) return NeuronType.OUTPUT;
		return NeuronType.HIDDEN;
	}
	
	/**Empty for input neurons or out non existant*/
	public Optional<NeuronGenes> getNeuron( int id ) {
		var genes = getGenes().get( Integer.toString(id) );
		if( genes != null )
			return Optional.of( (NeuronGenes) genes );
		return  Optional.empty();
	}
	
	public int sizeHidden() {
		return size() - outputs;
	} 
	
	public int sizeConnections() {
		int totalConnections = 0;
		for( var g : getGenes().values() ) {
			totalConnections += ((NeuronGenes)g).sizeConnections();
		}
		return totalConnections;
	}
	
	public float strongestWeight() {
		float strongest = 0;
		for( var g : getGenes().values() ) {
			strongest = Math.max( strongest , ((NeuronGenes)g).strongestWeight() );
		}
		return strongest;
	}
	
	@Override
	public void addGene() {
		int key = nextID.get();
		super.addGene();
		idList.add( key );
	}
	
	@Override
	public void removeGene() {
		var genes = getGenes();
		if( genes.size() <= inputs + outputs ) return;
		int key = random.nextInt(inputs + outputs, genes.size());
		genes.remove( Integer.toString(key) );
		idList.remove( (Integer) key );
	}
	
	@Override
	public NeuronBundle copy() {
		return new NeuronBundle( this );
	}

	@SuppressWarnings("static-method") //this method is meant to be overloaded
	public ArrayList<ActivationFunction> loadActivationFunctions() {
		return ActivationFunctions.loadActivationFunctions(new ArrayList<>());
	}
	
	public enum NeuronType {
		INPUT,  //no genes
		HIDDEN, //genes, may be removed by mutation
		OUTPUT; //genes, may not be removed by mutation
		
		public boolean isInput() {return this.equals(INPUT);}
		public boolean isHidden() {return this.equals(HIDDEN);}
		public boolean isOutput() {return this.equals(OUTPUT);}
	}

	

	
	
}
