package com.github.pireba.itunesrenamer.model;

import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a track in iTunes.<br>
 * A track has an id, an initial name and a new name.
 */
public class Track {
	
	/**
	 * The ID of the track.
	 * Each track has a unique ID in iTunes called "Persistent ID".
	 */
	private final StringProperty id = new SimpleStringProperty();
	
	/**
	 * The initial name of the track.
	 */
	private final StringProperty initialName = new SimpleStringProperty();
	
	/**
	 * The new name of the track.
	 * This name will be applied to iTunes.
	 */
	private final StringProperty newName = new SimpleStringProperty();
	
	/**
	 * Creates a track object.
	 * 
	 * @param id
	 * 		The persistent id of the track.
	 * @param name
	 * 		The initial name of the track.
	 */
	public Track(String id, String name) {
		this.setId(id);
		this.setInitialName(name);
		this.setNewName(name);
	}
	
	/**
	 * Returns true if the initial and new names are not the same, otherwise false.
	 * 
	 * @return
	 * 		true if the initial and new names are not the same, otherwise false.
	 */
	public boolean isNameChanged() {
		return ! this.getInitialName().equals(this.getNewName());
	}
	
	@Override
	public String toString() {
		return this.getInitialName()+" -> "+this.getNewName();
	}
	
	
	@SuppressWarnings("javadoc")
	public ReadOnlyStringProperty idProperty() {
		return this.id;
	}
	
	@SuppressWarnings("javadoc")
	public String getId() {
		return this.id.get();
	}
	
	@SuppressWarnings("javadoc")
	public void setId(String id) {
		this.id.set(id);
	}
	
	@SuppressWarnings("javadoc")
	public ReadOnlyStringProperty initialNameProperty() {
		return this.initialName;
	}
	
	@SuppressWarnings("javadoc")
	public String getInitialName() {
		return this.initialName.get();
	}
	
	@SuppressWarnings("javadoc")
	public void setInitialName(String initialName) {
		this.initialName.set(initialName);
	}
	
	@SuppressWarnings("javadoc")
	public StringProperty newNameProperty() {
		return this.newName;
	}
	
	@SuppressWarnings("javadoc")
	public String getNewName() {
		return this.newName.get();
	}
	
	@SuppressWarnings("javadoc")
	public void setNewName(String newName) {
		this.newName.set(newName);
	}
}