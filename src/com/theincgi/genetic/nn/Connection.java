package com.theincgi.genetic.nn;

import java.util.List;
import java.util.Random;

import com.theincgi.genetic.FloatGene;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.GeneHashBundle;
import com.theincgi.genetic.OptionGene;

public class Connection extends GeneHashBundle {
	
	OptionGene<Integer> connectionSourceID;
	FloatGene weight;
	
	public Connection(Random random) {
		super(random, null);
		weight = new FloatGene(random);
		weight.mutate();
	}
	
	public Connection(Random random, List<Integer> connectionSourceIDs) {
		super(random, null);
		this.connectionSourceID = new OptionGene<>(random, connectionSourceIDs);
		weight = new FloatGene(random);
		weight.mutate();
	}
	
	public Connection(Random random, List<Integer> connectionSourceIDs, FloatGene weight, OptionGene<Integer> connectionSourceID) {
		super(random, null);
		this.connectionSourceID = new OptionGene<>(random, connectionSourceIDs);
		weight = new FloatGene(random);
		weight.mutate();
	}
	
	public Connection( Connection copyFrom ) {
		super( copyFrom.random, null );
		this.connectionSourceID = copyFrom.connectionSourceID.copy();
		this.weight 			= copyFrom.weight.copy();
	}
	
	@Override
	public void addGene() {	
	}
	@Override
	public void removeGene() {	
	}
	
	@Override
	public boolean canMutate() {
		return true;
	}
	
	@Override
	public Connection copy() {
		return new Connection( this );
	}
	
	@Override
	public void mutate() {
		if(connectionSourceID.shouldMutateNow())
			connectionSourceID.mutate();
		if(weight.shouldMutateNow())
			weight.mutate();
	}

	public int getInputID() {
		return connectionSourceID.getValue();
	}
	
	public float getWeight() {
		return weight.getValue();
	}
}
