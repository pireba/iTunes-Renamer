package com.github.pireba.itunesrenamer.fxml.action;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import com.github.pireba.itunesrenamer.fxml.control.ITunesTagComboBox;
import com.github.pireba.itunesrenamer.model.ITunes;
import com.github.pireba.itunesrenamer.model.ITunesTag;
import com.github.pireba.utils.Utils;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.ToggleButton;

/**
 * A renaming action that inserts tags loaded from iTunes.
 * The user can specify the position where the tags are inserted.
 * The following tags can be retrieved:
 * <ul>
 *  <li>Album</li>
 *  <li>Album Artist</li>
 *  <li>Artist</li>
 *  <li>BPM</li>
 *  <li>Comment</li>
 *  <li>Composer</li>
 *  <li>Disc Count</li>
 *  <li>Disc Number</li>
 *  <li>Genre</li>
 *  <li>Grouping</li>
 *  <li>Name</li>
 *  <li>Sort Album</li>
 *  <li>Sort Album Artist</li>
 *  <li>Sort Artist</li>
 *  <li>Sort Composer</li>
 *  <li>Sort Name</li>
 *  <li>Track Count</li>
 *  <li>Track Number</li>
 *  <li>Year</li>
 * </ul>
 */
public class InsertITunesTag extends Action implements Initializable {
	
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
	 * A ComboBox to select the iTunes tag.
	 */
	@FXML private ITunesTagComboBox tag;
	
	/**
	 * Creates a new InsertTag action.
	 * 
	 * @throws IOException
	 * 		If an error occurs when loading the FXML file.
	 */
	public InsertITunesTag() throws IOException {
		super();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Utils.initializeIntegerSpinner(this.position);
	}
	
	@Override
	public void addControlChangeListener(final ChangeListener<Object> listener) {
		this.position.getValueFactory().valueProperty().addListener(listener);
		this.left.selectedProperty().addListener(listener);
		this.right.selectedProperty().addListener(listener);
		this.tag.valueProperty().addListener(listener);
	}
	
	@Override
	public Callable<String[]> getRenamingTask(String[] input) {
		return () -> {
			String[] result = new String[input.length];
			
			ITunesTag tag = this.tag.getValue();
			String[] trackTags = ITunes.loadTag(tag);
			
			for ( int i=0; i<input.length; i++ ) {
				if ( Thread.currentThread().isInterrupted() ) {
					break;
				}
				
				String currentName = input[i];
				String currentProperty = trackTags[i];
				
				int position = InsertITunesTag.this.position.getValue();
				if ( InsertITunesTag.this.right.isSelected() ) {
					position = currentName.length() - position;
				}
				
				if ( position > currentName.length() ) {
					position = currentName.length();
				} else if ( position < 0 ) {
					position = 0;
				}
				
				StringBuilder newName = new StringBuilder();
				newName.append(currentName.substring(0, position));
				newName.append(currentProperty);
				newName.append(currentName.substring(position));
				
				result[i] = newName.toString();
			}
			
			return result;
		};
	}
}