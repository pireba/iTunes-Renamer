package com.github.pireba.itunesrenamer.fxml.control;

import java.util.LinkedHashMap;
import java.util.Map;

import com.github.pireba.itunesrenamer.fxml.action.Action;
import com.github.pireba.itunesrenamer.fxml.action.ClearName;
import com.github.pireba.itunesrenamer.fxml.action.ConvertCase;
import com.github.pireba.itunesrenamer.fxml.action.InsertNumbering;
import com.github.pireba.itunesrenamer.fxml.action.InsertITunesTag;
import com.github.pireba.itunesrenamer.fxml.action.InsertText;
import com.github.pireba.itunesrenamer.fxml.action.OverwriteText;
import com.github.pireba.itunesrenamer.fxml.action.RemoveText;
import com.github.pireba.itunesrenamer.fxml.action.SearchAndReplace;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

/**
 * A ComboBox that displays all the renaming {@linkplain Action}s.
 */
public class ActionComboBox extends ComboBox<Class<? extends Action>> {
	
	/**
	 * A Map that stores {@linkplain Action} class objects and the associated display names.
	 */
	private static final Map<Class<? extends Action>, String> MAP = new LinkedHashMap<>();
	static {
		MAP.put(ClearName.class, "Clear Name");
		MAP.put(ConvertCase.class, "Convert Case");
		MAP.put(InsertNumbering.class, "Insert Numbering");
		MAP.put(InsertITunesTag.class, "Insert iTunes Tag");
		MAP.put(InsertText.class, "Insert Text");
		MAP.put(OverwriteText.class, "Overwrite Text");
		MAP.put(RemoveText.class, "Remove Text");
		MAP.put(SearchAndReplace.class, "Search And Replace");
	}
	
	/**
	 * Creates a new ActionComboBox and select the given {@linkplain Action}.
	 * 
	 * @param clazz
	 * 		An {@linkplain Action} class object to be selected.
	 */
	public ActionComboBox(Class<? extends Action> clazz) {
		ObservableList<Class<? extends Action>> list = FXCollections.observableArrayList(MAP.keySet());
		this.setItems(list);
		this.setMaxWidth(Double.MAX_VALUE);
		this.getSelectionModel().select(clazz);
		this.setConverter(this.getActionStringConverter());
	}
	
	/**
	 * Returns a StringConverter that converts an {@linkplain Action} class object to the display name.
	 * 
	 * @return
	 * 		The StringConverter.
	 */
	private StringConverter<Class<? extends Action>> getActionStringConverter() {
		return new StringConverter<Class<? extends Action>>() {
			
			@Override
			public String toString(Class<? extends Action> object) {
				return MAP.get(object);
			}
			
			@Override
			public Class<? extends Action> fromString(String string) {
				return null;
			}
		};
	}
}