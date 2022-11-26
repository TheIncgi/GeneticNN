package com.theincgi.genetic.nn.visualize;

import static java.lang.String.format;

import com.theincgi.genetic.nn.Neuron;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

/**Visualization of a GeneticNeuron*/
public class FXNeuron extends NodeSpacer.Node {
	
	protected Pane root;
	protected Circle node;
	protected TableView<GeneInfoRow> table;
	protected TableColumn<GeneInfoRow, String> propNames;
	protected TableColumn<GeneInfoRow, String> values;
	protected TableColumn<GeneInfoRow, String> mutationRates;
	protected Neuron neuron;
	
	@SuppressWarnings("unchecked")
	public FXNeuron( Neuron neuron ) {
		this.neuron = neuron;
		root = new Pane();
		node = new Circle();
		table         = new TableView<>();
		propNames     = new TableColumn<>("Gene");
		values        = new TableColumn<>("Value");
		mutationRates = new TableColumn<>("Mutation Chance");
		
		propNames	 .setCellValueFactory( x -> new SimpleStringProperty( x.getValue().prop     ));
		values	     .setCellValueFactory( x -> new SimpleStringProperty( x.getValue().value    ));
		mutationRates.setCellValueFactory( x -> new SimpleStringProperty( x.getValue().mutation ));
		
		table.getColumns().addAll(propNames, values, mutationRates);
		table.setFixedCellSize(25);
		table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.01)));
	    table.minHeightProperty().bind(table.prefHeightProperty());
	    table.maxHeightProperty().bind(table.prefHeightProperty());
		
	    
		root.getChildren().addAll(
			node,
			table
		);
		var radius = new DoubleBinding() {
			
			{ 
				super.bind(table.widthProperty(), table.heightProperty()); 
			}
			
			@Override
			protected double computeValue() {
				double dx = table.widthProperty().doubleValue();
				double dy = table.heightProperty().doubleValue();
				return Math.sqrt( dx*dx + dy*dy ) / 2 + 8;
			}
			
			public void dispose() {
				super.unbind(table.widthProperty(), table.heightProperty());
			};
		};
		node.radiusProperty().bind( radius );
		root.prefWidthProperty().bind(radius.multiply(2));
		root.prefHeightProperty().bind(radius.multiply(2));
		node.centerXProperty().bind( root.widthProperty().divide(2) );
		node.centerYProperty().bind( root.heightProperty().divide(2) );
		table.layoutXProperty().bind( root.widthProperty().divide(2).subtract(table.widthProperty().divide(2)) );
		table.layoutYProperty().bind( root.heightProperty().divide(2).subtract(table.heightProperty().divide(2)) );
		
		root.setScaleX( .75 );
		root.setScaleY( .75 );	
		
		updateGenes();
	}
	
	public Pane getRoot() {
		return root;
	}
	
	public void updateGenes() {
		var genes = neuron.getGenes();
		table.getItems().clear();
		if( genes != null )
			table.getItems().addAll(
				new GeneInfoRow(
					"Bias", 
					genes.getBias(), 
					genes.getBiasGene().getMutationChance()
				),
				new GeneInfoRow(
					"Activation", 
					genes.getActivationFunction().getName(), 
					genes.getActivationFunctionGene().getMutationChance()
				),
				new GeneInfoRow(
					"Gate Enable", 
					genes.isGateEnabled()? "✅":"❌", 
					genes.getGateEnabledGene().getMutationChance()
				),
				new GeneInfoRow(
					"Gate Bias", 
					genes.getGateBias(), 
					genes.getGateBiasGene().getMutationChance()
				),
				new GeneInfoRow(
					"Gate Activation", 
					genes.getGateActivationFunction().toString(),
					genes.getGateActivationFunctionGene().getMutationChance()
				),
				new GeneInfoRow(
					"GateAddFactor", 
					genes.getGateAddFactorGene().getValue(), 
					genes.getGateAddFactorGene().getMutationChance()
				) //add gate or regular connection
			);
	}
	
	@Override
	public void move() {
		super.move();
		root.setLayoutX( super.x );
		root.setLayoutY( super.y );
	}
	
	
	protected static record GeneInfoRow (String prop, String value, String mutation) {
		public GeneInfoRow(String prop, double value, double mutation) {
			this(prop, format("%.3f", value), format("%.3f", mutation));
		}
		public GeneInfoRow(String prop, String value, double mutation) {
			this(prop, value, format("%.3f", mutation));
		}
	}
	
}
