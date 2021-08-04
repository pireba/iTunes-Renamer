package com.github.pireba.itunesrenamer.fxml.control;

import com.github.pireba.itunesrenamer.model.ITunesTag;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;

/**
 * A ComboBox that displays all the {@linkplain ITunesTag}s.
 */
public class ITunesTagComboBox extends ComboBox<ITunesTag> {
	
	/**
	 * An ObservableList to store the items.
	 */
	private final ObservableList<ITunesTag> items = FXCollections.observableArrayList(ITunesTag.values());
	
	/**
	 * Creates a new ITunesTagComboBox and select the default tag "NAME".
	 */
	public ITunesTagComboBox() {
		this.setItems(this.items);
		this.getSelectionModel().select(ITunesTag.NAME);
	}
}