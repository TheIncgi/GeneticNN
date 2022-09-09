package com.theincgi.genetic.nn;

import static com.theincgi.genetic.nn.GateEnable.ENABLED;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import com.theincgi.genetic.FloatGene;
import com.theincgi.genetic.GeneArrayBundle;
import com.theincgi.genetic.GeneBundle;
import com.theincgi.genetic.GeneHashBundle;
import com.theincgi.genetic.OptionGene;

public class Neuron extends GeneBundle {
	
	public final int id;
	GeneHashBundle connections;
	GeneHashBundle gateConnections;
	
	FloatGene bias;
	FloatGene gateBias;
	FloatGene gateAddFactor;
	
	OptionGene<GateEnable> gateEnabled;
	private Supplier<Connection> connectionFactory;
	
	public Neuron( Random random, NeuronBundle connectionOptions ) {
		super(random);
		
		id = connectionOptions.nextID.getAndIncrement();
		connectionOptions.getIDs().add(id);
		
		this.connectionFactory = ()->{
			Connection c = new Connection(random, connectionOptions.getIDs());
			return c;
		};
		
		connections = new GeneHashBundle(random, null);
		gateConnections = new GeneHashBundle(random, null);
		
		bias = new FloatGene(random);
		gateBias = new FloatGene(random);
		gateAddFactor = new FloatGene(random, .25f, 0f, 1f);
		gateEnabled = new OptionGene<>(random, List.of(GateEnable.values()));
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
		
	}
	
	
	
}
