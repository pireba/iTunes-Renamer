package com.github.pireba.itunesrenamer.fxml.action;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import com.github.pireba.utils.Utils;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;

/**
 * A renaming action that removes characters.
 * The user can specify the area where the characters will be deleted.
 */
public class RemoveText extends Action implements Initializable {
	
	/**
	 * A Spinner for entering the start position.
	 */
	@FXML private Spinner<Integer> startPosition;
	
	/**
	 * A ToggleButton to define the start position from the left.
	 */
	@FXML private ToggleButton startLeft;
	
	/**
	 * A ToggleButton to define the start position from the right.
	 */
	@FXML private ToggleButton startRight;
	
	/**
	 * A Spinner for entering the end position.
	 */
	@FXML private Spinner<Integer> endPosition;
	
	/**
	 * A ToggleButton to define the end position from the left.
	 */
	@FXML private ToggleButton endLeft;
	
	/**
	 * A ToggleButton to define the end position from the left.
	 */
	@FXML private ToggleButton endRight;
	
	/**
	 * Creates a new RemoveCharacters action.
	 * 
	 * @throws IOException
	 * 		If an error occurs when loading the FXML file.
	 */
	public RemoveText() throws IOException {
		super();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Utils.initializeIntegerSpinner(this.startPosition);
		Utils.initializeIntegerSpinner(this.endPosition);
	}
	
	@Override
	public void addControlChangeListener(final ChangeListener<Object> listener) {
		this.startPosition.valueProperty().addListener(listener);
		this.startLeft.selectedProperty().addListener(listener);
		this.startRight.selectedProperty().addListener(listener);
		this.endPosition.valueProperty().addListener(listener);
		this.startLeft.selectedProperty().addListener(listener);
		this.startRight.selectedProperty().addListener(listener);
	}
	
	@Override
	public Callable<String[]> getRenamingTask(String[] input) {
		return () -> {
			String[] result = new String[input.length];
			
			for ( int i=0; i<input.length; i++ ) {
				if ( Thread.currentThread().isInterrupted() ) {
					break;
				}
				
				String currentName = input[i];
				
				int from = this.startPosition.getValue();
				int to = this.endPosition.getValue();
				
				if ( this.startRight.isSelected() ) {
					from = currentName.length() - from;
				}
				
				if ( this.endRight.isSelected() ) {
					to = currentName.length() - to;
				}
				
				if ( from > currentName.length() ) {
					from = currentName.length();
				} else if ( from < 0 ) {
					from = 0;
				}
				
				if ( to > currentName.length() ) {
					to = currentName.length();
				} else if ( to < 0 ) {
					to = 0;
				}
				
				if ( from > to ) {
					int temp = from;
					from = to;
					to = temp;
				}
				
				StringBuilder newName = new StringBuilder();
				newName.append(currentName.substring(0, from));
				newName.append(currentName.substring(to));
				
				result[i] = newName.toString();
			}
			
			return result;
		};
	}
}