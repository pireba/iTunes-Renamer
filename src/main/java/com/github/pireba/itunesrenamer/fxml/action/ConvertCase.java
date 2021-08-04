package com.github.pireba.itunesrenamer.fxml.action;

import java.io.IOException;
import java.util.concurrent.Callable;

import org.apache.commons.text.WordUtils;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleButton;

/**
 * A renaming action that converts between lowercase, UPPERCASE and Start Case.
 */
public class ConvertCase extends Action {
	
	/**
	 * A ToggleButton to select lower case.
	 */
	@FXML private ToggleButton lowerCase;
	
	/**
	 * A ToggleButton to select upper case.
	 */
	@FXML private ToggleButton upperCase;
	
	/**
	 * A ToggleButton to select start case.
	 */
	@FXML private ToggleButton startCase;
	
	/**
	 * Creates a new ConvertCase action.
	 * 
	 * @throws IOException
	 * 		If an error occurs when loading the FXML file.
	 */
	public ConvertCase() throws IOException {
		super();
	}
	
	@Override
	public void addControlChangeListener(final ChangeListener<Object> listener) {
		this.lowerCase.selectedProperty().addListener(listener);
		this.upperCase.selectedProperty().addListener(listener);
		this.startCase.selectedProperty().addListener(listener);
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
				
				String newName = "";
				if ( this.lowerCase.isSelected() ) {
					newName = currentName.toLowerCase();
				} else if ( this.upperCase.isSelected() ) {
					newName = currentName.toUpperCase();
				} else if ( this.startCase.isSelected() ) {
					newName = WordUtils.capitalizeFully(currentName);
				} else {
					newName = currentName;
				}
				
				result[i] = newName;
			}
			
			return result;
		};
	}
}