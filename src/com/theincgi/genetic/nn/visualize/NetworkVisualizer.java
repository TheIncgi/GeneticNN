package com.theincgi.genetic.nn.visualize;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import com.theincgi.genetic.nn.Network;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class NetworkVisualizer extends Application {
	
	private static NetworkVisualizer instance;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public BorderPane root;
	public FXNetwork networkViewer;
	public Button stepSpacer;
	public Button epoch;
	
	public ToolBar toolbar;
	
	/**Use {@link #getInstance()}*/
	@Deprecated(forRemoval = false)
	public NetworkVisualizer() {
	}
	
	private void setupNodes() {
		root = new BorderPane();
		networkViewer = new FXNetwork();
		stepSpacer = new Button("Step Spacer");
		epoch = new Button("Epoch");
		toolbar = new ToolBar(stepSpacer, epoch);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		setupNodes();
		primaryStage.setScene(new Scene(root, 400, 400));
		
		root.setCenter( networkViewer.getPane() );
		root.setTop( toolbar );
		
		setupControls();
		
		instance = this;
		primaryStage.show();
		System.out.println("FX Network Visualizer launched");
	}
	
	private void setupControls() {
		stepSpacer.setOnAction(e->{
			networkViewer.stepSpacer();
		});
	}

	public static NetworkVisualizer getInstance() throws InterruptedException, ExecutionException {
		if( instance == null )
			return CompletableFuture.supplyAsync(()->{
				Thread t = new Thread(()->{
					launch(NetworkVisualizer.class);					
				});
				t.setDaemon(false);
				t.start();
				while( instance == null )
					Thread.onSpinWait();
				return instance;
			}).get();
		return instance;
	}
	
	
	public void setNetwork( Network network ) {
		Platform.runLater(()->{
			networkViewer.setNetwork( network );			
		});
	}
}
