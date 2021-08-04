package com.github.pireba.itunesrenamer.model;

import java.util.HashMap;

import com.github.pireba.itunesrenamer.fxml.action.InsertNumbering;

/**
 * A map for the numbering format used in the {@linkplain InsertNumbering} action.
 * This action allows the user to select the numbering format to be inserted.
 * This describes with how many digits the numbers are inserted.
 * This map stores a display name and an associated format string.
 * <br><br>
 * For example:<br>
 * Key:   "0001, 0002, 0003, ..."<br>
 * Value: "%04d"
 */
public class NumberingFormat extends HashMap<String, String> {

	/**
	 * Default serial version ID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new map for the numbering format.
	 */
	public NumberingFormat() {
		this.put("1, 2, 3, ...", "%01d");
		this.put("01, 02, 03, ...", "%02d");
		this.put("001, 002, 003, ...", "%03d");
		this.put("0001, 0002, 0003, ...", "%04d");
		this.put("00001, 00002, 00003, ...", "%05d");
		this.put("000001, 000002, 000003, ...", "%06d");
		this.put("0000001, 0000002, 0000003, ...", "%07d");
		this.put("00000001, 00000002, 00000003, ...", "%08d");
		this.put("000000001, 000000002, 000000003, ...", "%09d");
	}
}