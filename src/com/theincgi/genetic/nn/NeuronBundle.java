package com.theincgi.genetic.nn;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import com.theincgi.genetic.FloatGene;
import com.theincgi.genetic.Gene;
import com.theincgi.genetic.GeneArrayBundle;
import com.theincgi.genetic.GeneHashBundle;
import com.theincgi.genetic.nn.ActivationFunctions.ActivationFunction;

public class NeuronBundle extends GeneHashBundle {
	
	public AtomicInteger nextID = new AtomicInteger(0);
	private final ArrayList<Integer> idList = new ArrayList<>();
	public ArrayList<ActivationFunction> activationFunctions = loadActivationFunctions();
	
	public NeuronBundle(Random random, Supplier<NamedGene> geneFactory, FloatGene addRemoveFactor) {
		super(random, geneFactory, addRemoveFactor);
	}

	public NeuronBundle(Random random, Supplier<NamedGene> geneFactory) {
		super(random, geneFactory);
	}
	
	public List<Integer> getIDs() {
		//must always be same list instance, used as options for connection OptionGene
		return idList;
	}
	
	public Optional<NeuronGenes> getNeuron( int id ) {
		var genes = getGenes().get( Integer.toString(id) );
		if( genes != null )
			return Optional.of( (NeuronGenes) genes );
		return  Optional.empty();
	}

	public ArrayList<ActivationFunction> loadActivationFunctions() {
		return ActivationFunctions.loadActivationFunctions(new ArrayList<>());
	}
	
}
