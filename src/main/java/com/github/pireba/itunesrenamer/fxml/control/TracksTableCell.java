package com.github.pireba.itunesrenamer.fxml.control;

import org.kordamp.ikonli.boxicons.BoxiconsRegular;
import org.kordamp.ikonli.javafx.FontIcon;

import com.github.pireba.itunesrenamer.model.Track;

import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

/**
 * A custom TableCell to display the tracks to be renamed.
 * Each track whose name is unchanged is displayed in a light gray font color and with a small icon.
 */
public class TracksTableCell extends TableCell<Track, String> {
	
	@Override
	protected void updateItem(String item, boolean empty) {
		super.updateItem(item, empty);
		
		if ( item == null || empty || this.getTableRow().getItem() == null ) {
			this.setGraphic(null);
			this.setStyle(null);
			this.setText(null);
			return;
		}
		
		Track track = this.getTableRow().getItem();
		
		if ( track.isNameChanged() ) {
			this.setGraphic(null);
			this.setStyle(null);
			this.setText(track.getNewName());
		} else {
			this.setGraphic(this.getImage());
			this.setStyle("-fx-text-fill: lightgray;");
			this.setText("No change");
		}
	}
	
	/**
	 * Gets the small icon that indicates that the track name is unchanged.
	 * 
	 * @return
	 * 		The small icon.
	 */
	private FontIcon getImage() {
		FontIcon icon = new FontIcon(BoxiconsRegular.X_CIRCLE);
		icon.setFill(Color.LIGHTGRAY);
		return icon;
	}
}