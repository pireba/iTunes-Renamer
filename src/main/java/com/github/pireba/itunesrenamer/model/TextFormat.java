package com.github.pireba.itunesrenamer.model;

import java.util.HashMap;

/**
 * A map for the text format used in the {@linkplain com.github.pireba.itunesrenamer.fxml.action.InsertNumbering InsertNumbering} action.
 * This action allows the user to select the text format to be inserted.
 * This describes the order in which the old name, numbering and additional text are arranged.
 * This map stores a display name and an associated format string.
 * <br><br>
 * For example:<br>
 * Key:   "Name - Number - Text"<br>
 * Value: "%1$s%2$s%3$s"
 */
public class TextFormat extends HashMap<String, String> {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new map for the text format.
	 */
	public TextFormat() {
		String name = "Name";
		String number = "Number";
		String text = "Text";
		
		this.put(name+" - "+number+" - "+text, "%1$s%2$s%3$s");
		this.put(name+" - "+text+" - "+number, "%1$s%3$s%2$s");
		this.put(number+" - "+name+" - "+text, "%2$s%1$s%3$s");
		this.put(number+" - "+text+" - "+name, "%2$s%3$s%1$s");
		this.put(text+" - "+name+" - "+number, "%3$s%1$s%2$s");
		this.put(text+" - "+number+" - "+name, "%3$s%2$s%1$s");
	}
}