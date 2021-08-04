package com.github.pireba.itunesrenamer.fxml.control;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.pireba.itunesrenamer.model.Track;
import com.github.pireba.utils.Utils;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * A TableView to display the tracks to be renamed.
 */
public class TracksTableView extends TableView<Track> implements Initializable {
	
	/**
	 * TableColumn to display the old name of the track.
	 */
	@FXML private TableColumn<Track, String> oldName;
	
	/**
	 * TableColumn to display the new name of the track.
	 */
	@FXML private TableColumn<Track, String> newName;
	
	/**
	 * Creates a new TracksTableView.
	 * 
	 * @throws IOException
	 * 		If an error occurs when loading the FXML file.
	 */
	public TracksTableView() throws IOException {
		Utils.loadFXMLAsRoot(this);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.setPlaceholder(new Label());
		
		this.newName.setCellFactory((param) -> new TracksTableCell());
		
		this.oldName.setCellValueFactory((param) -> param.getValue().initialNameProperty());
		this.newName.setCellValueFactory((param) -> param.getValue().newNameProperty());
	}
}