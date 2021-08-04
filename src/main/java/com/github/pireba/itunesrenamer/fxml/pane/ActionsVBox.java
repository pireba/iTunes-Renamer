package com.github.pireba.itunesrenamer.fxml.pane;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.github.pireba.itunesrenamer.fxml.action.Action;
import com.github.pireba.itunesrenamer.model.ITunes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;

/**
 * A VBox to display and arrange the actions.
 */
public class ActionsVBox extends VBox {
	
	/**
	 * A list of Future objects.
	 * A Future represents a pending renaming action.
	 * The renaming Task adds the renaming actions to this list in order.
	 * After that they are processed in that order.
	 * If the user starts a new renaming Task and the old one is not yet completed, all pending Future objects are canceled.
	 */
	private final List<Future<String[]>> listFutures = new ArrayList<>();
	
	/**
	 * Creates a new ActionsVBox.
	 */
	public ActionsVBox() {
		this.addOnActionDragDropped();
		this.addOnActionDragOver();
	}
	
	/**
	 * Adds the drag dropped event to this VBox.
	 */
	private void addOnActionDragDropped() {
		this.setOnDragDropped((event) -> {
			Action draggedAction = (Action) event.getGestureSource();
			
			this.getChildren().add(draggedAction);
			
			event.setDropCompleted(true);
			event.consume();
		});
	}
	
	/**
	 * Adds the drag over event to this VBox.
	 */
	private void addOnActionDragOver() {
		this.setOnDragOver((event) -> {
			event.acceptTransferModes(TransferMode.MOVE);
			
			event.consume();
		});
	}
	
	/**
	 * Get a Task that renames the tracks.
	 * The result of each renaming action is applied to the tracks one by one.
	 * 
	 * @return
	 * 		The Task.
	 */
	public Task<String[]> getRenameTask() {
		return new Task<String[]>() {
			@Override
			protected String[] call() throws Exception {
				// Use a new List to prevent ConcurrentModificationException.
				List<Future<String[]>> futures = new ArrayList<>(ActionsVBox.this.listFutures);
				for ( Future<String[]> future : futures ) {
					future.cancel(true);
					ActionsVBox.this.listFutures.remove(future);
				}
				ActionsVBox.this.listFutures.clear();
								
				ExecutorService executor = Executors.newSingleThreadExecutor();
				
				String[] result = ITunes.getInitialNames();
				// Use a new List to prevent ConcurrentModificationException.
				ObservableList<Node> nodes = FXCollections.observableArrayList(ActionsVBox.this.getChildren());
				for ( Node node : nodes ) {
					Action action = (Action) node;
					Future<String[]> future = executor.submit(action.getRenamingTask(result));
					ActionsVBox.this.listFutures.add(future);
					
					try {
						result = future.get();
					} catch ( CancellationException e ) {
						this.cancel(true);
					} catch ( InterruptedException e ) {
						this.cancel(true);
					}
				}
				
				executor.shutdown();
				return result;
			}
		};
	}
}