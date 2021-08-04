package com.github.pireba.itunesrenamer.fxml.action;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

/**
 * A renaming action with search and replace function.
 * The user can enable a case-sensitive search.
 * He can also enable search and replace with regular expressions.
 */
public class SearchAndReplace extends Action {
	
	/**
	 * A CSS style that adds a red background color to a TextField.
	 * This is added to the find and/or replace TextField when the user enters an invalid regular expression.
	 */
	private static final String REGEX_ERROR_STYLE = "-fx-background-color: derive(red, 100%);";
	
	/**
	 * A TextField where the user can enter the search pattern.
	 */
	@FXML private TextField search;
	
	/**
	 * A TextField where the user can enter the replace pattern.
	 */
	@FXML private TextField replace;
	
	/**
	 * A CheckBox to enable the case sensitive search.
	 */
	@FXML private CheckBox caseSensitive;
	
	/**
	 * A CheckBox to enable search and replace with regular expressions.
	 */
	@FXML private CheckBox regularExpression;
	
	/**
	 * Creates a new SearchAndReplace action.
	 * 
	 * @throws IOException
	 * 		If an error occurs when loading the FXML file.
	 */
	public SearchAndReplace() throws IOException {
		super();
	}
	
	@Override
	public void addControlChangeListener(final ChangeListener<Object> listener) {
		this.search.textProperty().addListener(listener);
		this.replace.textProperty().addListener(listener);
		this.caseSensitive.selectedProperty().addListener(listener);
		this.regularExpression.selectedProperty().addListener(listener);
	}
	
	@Override
	public Callable<String[]> getRenamingTask(String[] input) {
		return () -> {
			String[] result = new String[input.length];
			
			String search = this.search.getText();
			String replace = this.replace.getText();
			
			this.search.setStyle(null);
			this.replace.setStyle(null);
			
			if ( search.isEmpty() ) {
				return input;
			}
			
			if ( this.regularExpression.isSelected() ) {
				try {
					Pattern.compile(this.search.getText());
				} catch ( PatternSyntaxException e ) {
					this.search.setStyle(REGEX_ERROR_STYLE);
					return input;
				}
			} else {
				search = Pattern.quote(this.search.getText());
				replace = Matcher.quoteReplacement(this.replace.getText());
			}
			
			if ( ! this.caseSensitive.isSelected() ) {
				search = "(?i)"+search;
			}
			
			for ( int i=0; i<input.length; i++ ) {
				if ( Thread.currentThread().isInterrupted() ) {
					break;
				}
				
				String currentName = input[i];
				String newName = currentName;
				
				try {
					newName = currentName.replaceAll(search, replace);
				} catch ( IllegalArgumentException | IndexOutOfBoundsException e ) {
					this.replace.setStyle(REGEX_ERROR_STYLE);
					return input;
				}
				
				result[i] = newName;
			}
			
			return result;
		};
	}
}