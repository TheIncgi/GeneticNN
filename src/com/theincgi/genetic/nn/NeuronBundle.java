package com.theincgi.genetic.nn;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import com.theincgi.genetic.FloatGene;
import com.theincgi.genetic.Gene;
import com.theincgi.genetic.GeneArrayBundle;

public class NeuronBundle extends GeneArrayBundle {
	
	public AtomicInteger nextID = new AtomicInteger(0);
	private final ArrayList<Integer> idList = new ArrayList<>();
	
	public NeuronBundle(Random random, Supplier<Gene> geneFactory, FloatGene addRemoveFactor) {
		super(random, geneFactory, addRemoveFactor);
	}

	public NeuronBundle(Random random, Supplier<Gene> geneFactory) {
		super(random, geneFactory);
	}
	
	public List<Integer> getIDs() {
		//must always be same list instance, used as options for connection OptionGene
		return idList;
	}
	
}
