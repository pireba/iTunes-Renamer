package com.github.pireba.itunesrenamer.model;

import com.github.pireba.applescript.AppleScript;
import com.github.pireba.applescript.AppleScriptException;
import com.github.pireba.applescript.AppleScriptList;
import com.github.pireba.applescript.AppleScriptObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

/**
 * A static class that provides functions to interact with iTunes.
 * With the method "loadSelectedTracks" all tracks selected in iTunes are retrieved and stored in a static ObservableList.
 * The method "getRenameTask" creates a task that renames all previously retrieved tracks in iTunes.
 */
public class ITunes {
	
	/**
	 * The ObservableList that stores the loaded tracks.
	 */
	private static final ObservableList<Track> TRACKS = FXCollections.observableArrayList();
	
	/**
	 * Creates an AppleScript command to retrieve a tag from the selected tracks in iTunes.
	 * 
	 * @param tag
	 * 		The {@linkplain ITunesTag} to retrieve.
	 * @return
	 * 		The AppleScript command.
	 */
	private static String[] createLoadCommand(ITunesTag tag) {
		String[] command = new String[3];
		
		command[0] = "tell application \"iTunes\"";
		command[1] = "get {persistent id, "+tag.getKey()+"} of selection";
		command[2] = "end tell";
		
		return command;
	}
	
	/**
	 * Creates an AppleScript command to rename a track in iTunes.
	 * 
	 * @param tag
	 * 		The {@linkplain ITunesTag} to be renamed.
	 * @param track
	 * 		The {@linkplain Track} to be renamed.
	 * @return
	 * 		The AppleScript command.
	 */
	private static String[] createRenameCommand(ITunesTag tag, Track track) {
		String[] command = new String[3];
		String id = track.getId();
		String name = track.getNewName();
		
		command[0] = "tell application \"iTunes\"";
		command[1] = "set "+tag.getKey()+" of track 1 whose persistent ID is \""+id+"\" to \""+name+"\"";
		command[2] = "end tell";
		
		return command;
	}
	
	/**
	 * Get the initial names of the loaded tracks.
	 * 
	 * @return
	 * 		The initial names of the loaded tracks.
	 */
	public static String[] getInitialNames() {
		String[] result = new String[TRACKS.size()];
		
		for ( int i=0; i<TRACKS.size(); i++ ) {
			result[i] = TRACKS.get(i).getInitialName();
		}
		
		return result;
	}
	
	/**
	 * Get a Task that retrieves the selected tracks in iTunes.
	 * This method defines the ObservableList of tracks this class works with.
	 * 
	 * @param tag
	 * 		The {@linkplain ITunesTag} to retrieve.
	 * @return
	 * 		The Task.
	 */
	public static Task<ObservableList<Track>> getLoadTask(ITunesTag tag) {
		return new Task<ObservableList<Track>>() {

			@Override
			protected ObservableList<Track> call() throws Exception {
				TRACKS.setAll(loadFromITunes(tag));
				return TRACKS;
			}
			
		};
	}
	
	/**
	 * Get the number of tracks to rename.
	 * 
	 * @return
	 * 		The number of tracks to rename.
	 */
	public static int getRenameCount() {
		int counter = 0;
		
		for ( Track track : TRACKS ) {
			if ( track.isNameChanged() ) {
				counter++;
			}
		}
		
		return counter;
	}
	
	/**
	 * Get a Task that renames all loaded tracks.
	 * 
	 * @param tag
	 * 		The {@linkplain ITunesTag} to be renamed.
	 * @return
	 * 		The Task.
	 */
	public static Task<Void> getRenameTask(ITunesTag tag) {
		return new Task<Void>() {
			
			@Override
			protected Void call() throws Exception {
				int i = 1;
				
				for ( Track track : TRACKS ) {
					if ( track.isNameChanged() ) {
						String[] command = createRenameCommand(tag, track);
						AppleScript as = new AppleScript(command);
						as.execute();
						
						track.setInitialName(track.getNewName());
					}
					
					this.updateProgress(i++, TRACKS.size());
				}
				
				return null;
			}
			
		};
	}
	
	/**
	 * Retrieve a tag of the selected tracks in iTunes.
	 * 
	 * @param tag
	 * 		The {@linkplain ITunesTag} to retrieve.
	 * @return
	 * 		An ObservableList with the retrieved tracks.
	 * @throws AppleScriptException
	 * 		When an error occurs while retrieving the songs.
	 */
	private static ObservableList<Track> loadFromITunes(ITunesTag tag) throws AppleScriptException {
		ObservableList<Track> result = FXCollections.observableArrayList();
		
		String[] command = createLoadCommand(tag);
		AppleScript as = new AppleScript(command);
		AppleScriptObject object = as.executeAsObject();
		
		AppleScriptList rootList = object.getList();
		AppleScriptList ids = rootList.getList(0);
		AppleScriptList text = rootList.getList(1);
		
		for ( int i=0; i<ids.size(); i++ ) {
			Track track = new Track(ids.getString(i), text.getString(i));
			result.add(track);
		}
		
		return result;
	}
		
	/**
	 * Retrieve a tag of the loaded tracks.
	 * 
	 * @param tag
	 * 		The {@linkplain ITunesTag} to retrieve.
	 * @return
	 * 		An array with the retrieved tags.
	 * @throws AppleScriptException
	 * 		When an error occurs while retrieving the songs.
	 */
	public static String[] loadTag(ITunesTag tag) throws AppleScriptException {
		String[] result = new String[TRACKS.size()];
		
		ObservableList<Track> tempList = loadFromITunes(tag);
		for ( int i=0; i<TRACKS.size(); i++ ) {
			Track track = TRACKS.get(i);
			Track tempTrack = tempList.get(i);
			
			if ( track.getId().equals(tempTrack.getId()) ) {
				result[i] = tempTrack.getNewName();
			} else {
				throw new AppleScriptException("The selection in iTunes has changed.");
			}
		}
		
		return result;
	}
	
	/**
	 * Sets the new name of the loaded tracks.
	 * 
	 * @param names
	 * 		An array with the new names.
	 */
	public static void setNewNames(String[] names) {
		for ( int i=0; i<names.length; i++ ) {
			TRACKS.get(i).setNewName(names[i]);
		}
	}
}