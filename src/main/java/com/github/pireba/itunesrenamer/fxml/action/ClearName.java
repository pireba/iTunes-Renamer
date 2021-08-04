package com.github.pireba.itunesrenamer.fxml.action;

import java.io.IOException;
import java.util.concurrent.Callable;

import javafx.beans.value.ChangeListener;

/**
 * A renaming action that clears the name.
 */
public class ClearName extends Action {
	
	/**
	 * Creates a new ClearName action.
	 * 
	 * @throws IOException
	 * 		If an error occurs when loading the FXML file.
	 */
	public ClearName() throws IOException {
		super();
	}
	
	@Override
	public void addControlChangeListener(final ChangeListener<Object> listener) {
		// This action has no controls to which a ChangeListener could be added.
	}
	
	@Override
	public Callable<String[]> getRenamingTask(String[] input) {
		return () -> {
			String[] result = new String[input.length];
			
			for ( int i=0; i<input.length; i++ ) {
				if ( Thread.currentThread().isInterrupted() ) {
					break;
				}
				
				result[i] = "";
			}
			
			return result;
		};
	}
}