package com.theincgi.genetic.nn.visualize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.theincgi.commons.Pair;
import com.theincgi.genetic.nn.Connection;
import com.theincgi.genetic.nn.Network;
import com.theincgi.genetic.nn.visualize.NodeSpacer.Link;
import com.theincgi.genetic.nn.visualize.NodeSpacer.Node;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

public class FXNetwork {
	
	ScrollPane scroll;
	Pane pane;
	NodeSpacer spacer;
	Network network;
	protected ArrayList<FXNeuron> fxNeurons = new ArrayList<>();
	protected HashMap<Integer, FXNeuron> fxNeuronLookup = new HashMap<>();
	protected ArrayList<FXNeuronConnection> fxConnections = new ArrayList<>();
	
	public static final int NODE_HEIGHT = 150;
	public static final int NODE_MARGIN = 15;
	
	public FXNetwork() {
		pane = new Pane();
		scroll = new ScrollPane(pane);
	}
	
	public ScrollPane getPane() {
		return scroll;
	}
	
	public void setNetwork( Network network ) {
		this.network = network;
		pane.getChildren().clear();
		fxNeurons.clear();
		fxNeuronLookup.clear();
		List<NodeSpacer.Node> nodes = new ArrayList<>();
		List<NodeSpacer.Link> links = new ArrayList<>();
		Pair<Float, Float> sortDir = new Pair<>( 1f, 0f ); 
		Random rand = new Random(456);
		
		for( int inID = 0; inID < network.inputSize(); inID++ ) {
			FXNeuron n = new FXNeuron( network.getInputNeuron(inID).get() );
			n.x = 5;
			n.y = 5 + ( NODE_HEIGHT + NODE_MARGIN ) * inID;
			n.anchor();
			nodes.add(n);
			fxNeurons.add( n );
			fxNeuronLookup.put( inID, n );
			pane.getChildren().add(n.getRoot());
		}
		
		for( int outID = 0; outID < network.outputSize(); outID++ ) {
			FXNeuron n = new FXNeuron( network.getOutputNeuron(outID).get() );
			n.x = 30;
			n.y = 5 + ( NODE_HEIGHT + NODE_MARGIN ) * outID;
			nodes.add(n);			
			fxNeurons.add( n );
			
			fxNeuronLookup.put( network.inputSize() + outID, n );
			pane.getChildren().add(n.getRoot());
		}
		
		for( int hidID = 0; hidID < network.hiddenSize(); hidID++ ) {
			var optNeuron = network.getHiddenNeuron(hidID);
			if(optNeuron.isEmpty()) continue;
			FXNeuron n = new FXNeuron( optNeuron.get() );
			n.x = 15;
			n.y = 5 + ( NODE_HEIGHT + NODE_MARGIN ) * hidID;
			fxNeurons.add( n );
			nodes.add(n);
			fxNeuronLookup.put( network.inputSize()+network.outputSize() + hidID, n );
			pane.getChildren().add(n.getRoot());
		}
		
		for( var n : network.getNeuronSet() ) {
			int toID = n.getKey();
			var to = fxNeuronLookup.get(toID);
			for( var connection : n.getValue().getConnections() ) {
				int fromID = connection.getInputID();
				var from = fxNeuronLookup.get(fromID);
//				float weight = connection.getWeight();
				
				NodeSpacer.Link link = new FXNeuronConnection(from, to, connection);
				links.add(link);
			}
		}
		
		spacer = new NodeSpacer(nodes, links, Optional.of(sortDir));
	}
	
	public static class FXNeuronConnection extends NodeSpacer.Link {
		Connection connection;
		public FXNeuronConnection(Node from, Node to, Connection c) {
			super(from, to);
			this.connection = c;
		}
	}
	
	public void stepSpacer() {
		if(spacer != null)
			spacer.itterate();
	}
}
