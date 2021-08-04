package com.github.pireba.itunesrenamer.fxml.action;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;

import com.github.pireba.itunesrenamer.model.NumberingFormat;
import com.github.pireba.itunesrenamer.model.TextFormat;
import com.github.pireba.utils.Utils;

import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

/**
 * A rename action that inserts a numbering and an optional additional text.
 * The user can specify the numbering format, start value and step value.
 * He can also define the order in which the old name, numbering and additional text will be arranged.
 */
public class InsertNumbering extends Action implements Initializable {
	
	/**
	 * A map for the numbering format.
	 */
	private static final NumberingFormat MAP_NUMBERING_FORMAT = new NumberingFormat();
	
	/**
	 * A map for the text format.
	 */
	private static final TextFormat MAP_TEXT_FORMAT = new TextFormat();
	
	/**
	 * A ComboBox to select the numbering format.
	 */
	@FXML private ComboBox<String> numberingFormat;
	
	/**
	 * A Spinner for entering the start value.
	 */
	@FXML private Spinner<Integer> startValue;
	
	/**
	 * A Spinner for entering the step value.
	 */
	@FXML private Spinner<Integer> stepValue;
	
	/**
	 * A TextField for entering the insertion text.
	 */
	@FXML private TextField text;
	
	/**
	 * A ComboBox to select the text format.
	 */
	@FXML private ComboBox<String> textFormat;
	
	/**
	 * Creates a new InsertNumbering action.
	 * 
	 * @throws IOException
	 * 		If an error occurs when loading the FXML file.
	 */
	public InsertNumbering() throws IOException {
		super();
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		ObservableList<String> numberingList = FXCollections.observableArrayList(MAP_NUMBERING_FORMAT.keySet());
		FXCollections.sort(numberingList, Comparator.reverseOrder());
		this.numberingFormat.setItems(numberingList);
		this.numberingFormat.getSelectionModel().select(0);
		
		Utils.initializeIntegerSpinner(this.startValue, 1);
		Utils.initializeIntegerSpinner(this.stepValue, 1);
		
		ObservableList<String> textList = FXCollections.observableArrayList(MAP_TEXT_FORMAT.keySet());
		FXCollections.sort(textList);
		this.textFormat.setItems(textList);
		this.textFormat.getSelectionModel().select(0);
	}
	
	@Override
	public void addControlChangeListener(final ChangeListener<Object> listener) {
		this.numberingFormat.valueProperty().addListener(listener);
		this.startValue.getValueFactory().valueProperty().addListener(listener);
		this.stepValue.getValueFactory().valueProperty().addListener(listener);
		this.text.textProperty().addListener(listener);
		this.textFormat.valueProperty().addListener(listener);
	}
	
	@Override
	public Callable<String[]> getRenamingTask(String[] input) {
		return () -> {
			String[] result = new String[input.length];
			
			String numberingFormat = MAP_NUMBERING_FORMAT.get(this.numberingFormat.getValue());
			int startValue = this.startValue.getValue();
			int stepValue = this.stepValue.getValue();
			String text = this.text.getText();
			String textFormat = MAP_TEXT_FORMAT.get(this.textFormat.getValue());
			
			int counter = startValue;
			for ( int i=0; i<input.length; i++ ) {
				if ( Thread.currentThread().isInterrupted() ) {
					break;
				}
				
				String currentName = input[i];
				String number = String.format(numberingFormat, counter);
				String newName = String.format(textFormat, currentName, number, text);
				
				counter += stepValue;
				result[i] = newName;
			}
			
			return result;
		};
	}
}