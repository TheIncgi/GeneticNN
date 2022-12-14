package com.theincgi.genetic.nn;

import static com.theincgi.commons.RandomUtils.pickRandom;
import static com.theincgi.commons.RandomUtils.pickRandomOfVarargs;
import static com.theincgi.genetic.nn.GateEnable.ENABLED;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import com.theincgi.commons.CollectionUtils;
import com.theincgi.commons.MultiIterator;
import com.theincgi.commons.Pair;
import com.theincgi.commons.RandomUtils;
import com.theincgi.genetic.FloatGene;
import com.theincgi.genetic.Gene;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.GeneHashBundle;
import com.theincgi.genetic.OptionGene;
import com.theincgi.genetic.nn.ActivationFunctions.ActivationFunction;
import com.theincgi.genetic.nn.ActivationFunctions.NamedActivationFunction;

public class NeuronGenes extends GeneBundle {
	
	public final int id;
	GeneHashBundle connections;
	GeneHashBundle gateConnections;
	
	FloatGene bias;
	FloatGene gateBias;
	FloatGene gateAddFactor;
	
	OptionGene<GateEnable> gateEnabled;
	OptionGene<ActivationFunctions.NamedActivationFunction> activationFunction;
	OptionGene<ActivationFunctions.NamedActivationFunction> gateActivationFunction;
	
	private Supplier<Connection> connectionFactory;
	private NeuronBundle options;
	
	public NeuronGenes( Random random, NeuronBundle bundle ) {
		super(random);
		this.options = bundle;
		
		id = bundle.nextID.get();
		
		this.connectionFactory = ()->{
			Connection c = new Connection(random, getConnectionOptions());
			return c;
		};
		
		connections = new GeneHashBundle(random, null);
		gateConnections = new GeneHashBundle(random, null);
		
		bias = new FloatGene(random);
		gateBias = new FloatGene(random);
		gateAddFactor = new FloatGene(random, .25f, 0f, 1f);
		gateEnabled = new OptionGene<>(random, List.of(GateEnable.values()));
		activationFunction = new OptionGene<>(random, bundle.activationFunctions);
		gateActivationFunction = new OptionGene<>(random, bundle.activationFunctions);
	}
	
	
	public NeuronGenes(NeuronGenes copyFrom) {
		super( copyFrom.random );
		this.id = copyFrom.id;
//		this.options = copyFrom.options; //TODO options needs to refer to bundle this neuron is contained in...
		this.connections = copyFrom.connections.copy();
		this.gateConnections = copyFrom.connections.copy();
		this.bias 			 = copyFrom.bias.copy();
		this.gateBias        = copyFrom.gateBias.copy();
		this.gateAddFactor   = copyFrom.gateAddFactor.copy();
		this.gateEnabled     = copyFrom.gateEnabled.copy();
		this.activationFunction = copyFrom.activationFunction.copy();
		this.gateActivationFunction = copyFrom.gateActivationFunction.copy();
		this.connectionFactory = ()->{
			Connection c = new Connection(random, getConnectionOptions());
			return c;
		};
	}
	
	public List<Integer> getConnectionOptions() {
		if( getParent().isPresent() && getParent().get() instanceof NeuronBundle )
			return ((NeuronBundle)getParent().get()).getIDs();
		if( getParent().isEmpty() )
			throw new NullPointerException("Missing parent, be sure to call Entity#updateGeneParenting() after constructor");
		throw new IllegalStateException("Neuron must be a child gene to a NueronBundle to generate connections");
	}
	
	/**Adds a new connection, handled here instead of in connection*/
	@Override
	public void addGene() {
		for(int i = 1; i<=4; i++) {
			var c = connectionFactory.get();
			var cid = c.connectionSourceID.getValue();
			var cidStr = Integer.toString(cid);
			if( gateEnabled.getValue().equals(ENABLED) && random.nextFloat() < gateAddFactor.getValue() ) {
				if( gateConnections.getValue().containsKey(cidStr) )
					continue;
				gateConnections.getValue().put(cidStr, c);
				return;
			} 
			//else
			if( connections.getValue().containsKey(cidStr) )
				continue;
			connections.getValue().put(cidStr, c);
			return;
		}
	}
	
	@Override
	public void removeGene() {
		var area = RandomUtils.pickRandomOfVarargs(random, connections, gateConnections);
		var key = RandomUtils.pickRandom(random, area.getGenes().keySet());
		if( key!=null )
			area.getGenes().remove(key);
	}
	
	@Override
	public int size() {
		return connections.size() + gateConnections.size();
	}
	
	/**connections, gateConnnections*/
	@Override
	public Pair<GeneHashBundle, GeneHashBundle> getValue() {
		return new Pair<GeneHashBundle, GeneHashBundle>(connections, gateConnections);
	}
	
	public GeneHashBundle getConnections() {
		return connections;
	}
	
	public GeneHashBundle getGateConnections() {
		return gateConnections;
	}
	
	public boolean isGateEnabled() {
		return false;//gateEnabled.getValue().isEnabled();
	}
	
	public float getBias() {
		return bias.getValue();
	}
	
	public float getGateBias() {
		return gateBias.getValue();
	}
	
	public FloatGene getBiasGene() {
		return bias;
	}
	
	public FloatGene getGateBiasGene() {
		return gateBias;
	}
	
	public OptionGene<GateEnable> getGateEnabledGene() {
		return gateEnabled;
	}
	
	public OptionGene<NamedActivationFunction> getActivationFunctionGene() {
		return activationFunction;
	}
	
	public OptionGene<NamedActivationFunction> getGateActivationFunctionGene() {
		return gateActivationFunction;
	}
	
	public NamedActivationFunction getActivationFunction() {
		return activationFunction.getValue();
	}
	
	public NamedActivationFunction getGateActivationFunction() {
		return gateActivationFunction.getValue();
	}
	
	//chance that a connection gene is added to the gate connection pool instead of the regular connections
	public FloatGene getGateAddFactorGene() {
		return gateAddFactor;
	}
	
	@Override
	public Iterable<Gene> getGenesIterable() {
		return new MultiIterator<Gene>( 
			connections.getGenes().values().iterator(), 
			gateConnections.getGenes().values().iterator() 
		);
	}
	
	@Override
	public boolean canMutate() {
		return true;
	}
	
	@Override
	public void mix(List<? extends GeneBundle> parentBundles) {
		var keepFactor = 1 / (1+parentBundles.size());
		
		ArrayList<NeuronGenes> parentNeurons = CollectionUtils.addAllWhereExtends(new ArrayList<>(), parentBundles, NeuronGenes.class);
		
		if( parentNeurons.size() != parentBundles.size() )
			throw new IllegalStateException("Parent bundles were not of type NeuronGenes");
		
		ArrayList<GeneHashBundle> parentConnections     = CollectionUtils.collect(parentNeurons.stream().map(x->x.connections), ArrayList::new );
		ArrayList<GeneHashBundle> parentGateConnections = CollectionUtils.collect(parentNeurons.stream().map(x->x.gateConnections), ArrayList::new);
		
		connections.mix(parentConnections);
		
		gateConnections.mix(parentGateConnections);
		
		if( random.nextFloat() > keepFactor )
			bias = pickRandom(random, parentNeurons).bias.copy();
		
		if( random.nextFloat() > keepFactor )
			gateAddFactor = pickRandom(random, parentNeurons).gateAddFactor.copy();
		
		if( random.nextFloat() > keepFactor )
			gateEnabled = pickRandom(random, parentNeurons).gateEnabled.copy();
		
		if( random.nextFloat() > keepFactor )
			gateBias = pickRandom(random, parentNeurons).gateBias.copy();

		if( random.nextFloat() > keepFactor )
			activationFunction = pickRandom(random, parentNeurons).activationFunction.copy();
		
		if( random.nextFloat() > keepFactor )
			gateActivationFunction = pickRandom(random, parentNeurons).gateActivationFunction.copy();
	}
	
	@Override
	public NeuronGenes copy() {
		return new NeuronGenes(this);
	}


	public int sizeConnections() {
		return connections.size();
	}
	
	/**abs of strongest non gate weight*/
	public float strongestWeight() {
		float max = 0;
		for( var c : connections.getGenes().values() ) {
			Connection conn = (Connection) c;
			max = Math.max(max, Math.abs( conn.weight.getValue() ));
		}
		return max;
	}
}
