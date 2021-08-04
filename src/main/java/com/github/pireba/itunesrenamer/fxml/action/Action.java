package com.github.pireba.itunesrenamer.fxml.action;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.github.pireba.itunesrenamer.fxml.control.ActionComboBox;
import com.github.pireba.utils.Utils;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.Separator;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 * A renaming action.
 */
public abstract class Action extends VBox {
	
	/**
	 * The default padding for an action.
	 */
	private static final Insets DEFAULT_PADDING = new Insets(10.0, 10.0, 0.0, 10.0);
	
	/**
	 * A ComboBox to select the action.
	 */
	private final ActionComboBox actionComboBox = new ActionComboBox(this.getClass());
	
	/**
	 * Indicates whether this action is selected.
	 * The user can select actions to be able to delete them.
	 */
	private final BooleanProperty selected = new SimpleBooleanProperty(false);
	
	/**
	 * Creates a new action.
	 * This loads the action FXML file and adds an action ComboBox and a separator.
	 * 
	 * @throws IOException
	 * 		 If an error occurs when loading the FXML file.
	 */
	protected Action() throws IOException {
		this.getChildren().add(this.actionComboBox);
		this.getChildren().add(Utils.loadFXML(this));
		this.getChildren().add(new Separator());
		
		this.setSpacing(10.0);
		this.setPadding(DEFAULT_PADDING);
		
		this.addActionSelectionEvent();
		this.addDragEvents();
	}
	
	@SuppressWarnings("javadoc")
	public BooleanProperty selectedProperty() {
		return this.selected;
	}
	
	@SuppressWarnings("javadoc")
	public boolean isSelected() {
		return this.selected.get();
	}
	
	@SuppressWarnings("javadoc")
	public void setSelected(boolean selected) {
		this.selected.set(selected);
	}
	
	/**
	 * Adds a ChangeListener that is notified when an action control is changed.
	 * 
	 * @param listener
	 * 		The listener to register.
	 */
	public abstract void addControlChangeListener(final ChangeListener<Object> listener);
	
	/**
	 * Gets the renaming Task.
	 * Each action has its own renaming logic.
	 * For example, the Convert Case action takes each string from the array and changes the case.
	 * The result of the renaming can be retrieved from the task as a string array.
	 * 
	 * @param input
	 * 		A string array which is processed in the Task.
	 * @return
	 * 		The renaming Task.
	 */
	public abstract Callable<String[]> getRenamingTask(final String[] input);
	
	/**
	 * Adds a mouse click event to select the action.
	 */
	private void addActionSelectionEvent() {
		this.setOnMouseClicked(event -> {
			if ( this.selected.get() ) {
				this.setStyle(null);
			} else {
				this.setStyle("-fx-border-color: -fx-focus-color; -fx-border-width: 2;");
			}
			
			this.selected.set(! this.selected.get());
		});
	}
	
	/**
	 * Adds a ChangeListener that will be notified when another action in the ComboBox is selected.
	 * 
	 * @param listener
	 * 		The listener to register.
	 */
	public void addActionReplaceListener(ChangeListener<Class<? extends Action>> listener) {
		this.actionComboBox.valueProperty().addListener(listener);
	}
	
	/**
	 * Adds the drag and drop events to the action.
	 */
	private void addDragEvents() {
		this.addOnActionDragDetected();
		this.addOnActionDragDropped();
		this.addOnActionDragEntered();
		this.addOnActionDragExited();
		this.addOnActionDragOver();
	}
	
	/**
	 * Adds the drag detected event to the action.
	 */
	private void addOnActionDragDetected() {
		this.setOnDragDetected((event) -> {
			Action draggedAction = (Action) event.getSource();
			Pane pane = (Pane) draggedAction.getParent();
			
			ClipboardContent content = new ClipboardContent();
			content.putString("dummy");
			
			Dragboard dragboard = draggedAction.startDragAndDrop(TransferMode.MOVE);
			dragboard.setContent(content);
			dragboard.setDragView(new ImageView(draggedAction.snapshot(null, null)).getImage());
			
			pane.getChildren().remove(draggedAction);
			event.consume();
		});
	}
		
	/**
	 * Adds the drag dropped event to the action.
	 */
	private void addOnActionDragDropped() {
		this.setOnDragDropped((event) -> {
			Action draggedAction = (Action) event.getGestureSource();
			Action droppedOnAction = (Action) event.getSource();
			Pane pane = (Pane) droppedOnAction.getParent();
						
			int index = pane.getChildren().indexOf(droppedOnAction);
			pane.getChildren().add(index, draggedAction);
			
			event.setDropCompleted(true);
			event.consume();
		});
	}
	
	/**
	 * Adds the drag entered event to the action.
	 */
	private void addOnActionDragEntered() {
		this.setOnDragEntered((event) -> {
			Action draggedAction = (Action) event.getGestureSource();
			Action enteredOnAction = (Action) event.getSource();
			
			double top = draggedAction.getHeight();
			double right = DEFAULT_PADDING.getRight();
			double bottom = DEFAULT_PADDING.getBottom();
			double left = DEFAULT_PADDING.getLeft();
			enteredOnAction.setPadding(new Insets(top, right, bottom, left));
			
			event.consume();
		});
	}
	
	/**
	 * Adds the drag exited event to the action.
	 */
	private void addOnActionDragExited() {
		this.setOnDragExited((event) -> {
			Action exitedOnAction = (Action) event.getSource();
			exitedOnAction.setPadding(DEFAULT_PADDING);
			
			event.consume();
		});
	}
	
	/**
	 * Adds the drag over event to the action.
	 */
	private void addOnActionDragOver() {
		this.setOnDragOver((event) -> {
			event.acceptTransferModes(TransferMode.MOVE);
			
			event.consume();
		});
	}
}