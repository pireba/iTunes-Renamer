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
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;

/**
 * A renaming action that inserts characters.
 * The user can specify the position where the characters are inserted.
*/
public class InsertText extends Action implements Initializable {
	
	/**
	 * A Spinner for entering the position.
	 */
	@FXML private Spinner<Integer> position;
	
	/**
	 * A ToggleButton to define the position from the left.
	 */
	@FXML private ToggleButton left;
	
	/**
	 * A ToggleButton to define the position from the right.
	 */
	@FXML private ToggleButton right;
	
	/**
	 * A TextField for entering the text to be inserted.
	 */
	@FXML private TextField text;
	
	/**
	 * Creates a new InsertCharacters action.
	 * 
	 * @throws IOException
	 * 		If an error occurs when loading the FXML file.
	 */
	public InsertText() throws IOException {
		super();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Utils.initializeIntegerSpinner(this.position);
	}
	
	@Override
	public void addControlChangeListener(final ChangeListener<Object> listener) {
		this.position.valueProperty().addListener(listener);
		this.left.selectedProperty().addListener(listener);
		this.right.selectedProperty().addListener(listener);
		this.text.textProperty().addListener(listener);
	}
	
	@Override
	public Callable<String[]> getRenamingTask(String[] input) {
		return () -> {
			String[] result = new String[input.length];
			
			String text = this.text.getText();
			
			for ( int i=0; i<input.length; i++ ) {
				if ( Thread.currentThread().isInterrupted() ) {
					break;
				}
				
				String currentName = input[i];
				
				int position = this.position.getValue();
				if ( this.right.isSelected() ) {
					position = currentName.length() - position;
				}
				
				if ( position > currentName.length() ) {
					position = currentName.length();
				} else if ( position < 0 ) {
					position = 0;
				}
				
				StringBuilder newName = new StringBuilder();
				newName.append(currentName.substring(0, position));
				newName.append(text);
				newName.append(currentName.substring(position));
				
				result[i] = newName.toString();
			}
			
			return result;
		};
	}
}