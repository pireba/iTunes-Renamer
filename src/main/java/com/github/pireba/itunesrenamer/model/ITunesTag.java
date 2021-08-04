package com.github.pireba.itunesrenamer.model;

/**
 * A collection of track tags that can be retrieved from iTunes.
 * Each element consists of a display name and the AppleScript key to retrieve the tag.
 */
public enum ITunesTag {
	
	/**
	 * The album tag of an iTunes track.
	 */
	ALBUM("Album", "album"),
	
	/**
	 * The album artist tag of an iTunes track.
	 */
	ALBUM_ARTIST("Album Artist", "album artist"),
	
	/**
	 * The artist tag of an iTunes track.
	 */
	ARTIST("Artist", "artist"),
	
	/**
	 * The bpm tag of an iTunes track.
	 */
	BPM("BPM", "bpm"),
	
	/**
	 * The comment tag of an iTunes track.
	 */
	COMMENT("Comment", "comment"),
	
	/**
	 * The composer tag of an iTunes track.
	 */
	COMPOSER("Composer", "composer"),
	
	/**
	 * The disc count tag of an iTunes track.
	 */
	DISC_COUNT("Disc Count", "disc count"),
	
	/**
	 * The disc number tag of an iTunes track.
	 */
	DISC_NUMBER("Disc Number", "disc number"),
	
	/**
	 * The genre tag of an iTunes track.
	 */
	GENRE("Genre", "genre"),
	
	/**
	 * The grouping tag of an iTunes track.
	 */
	GROUPING("Grouping", "grouping"),
	
	/**
	 * The name tag of an iTunes track.
	 */
	NAME("Name", "name"),
	
	/**
	 * The sort album tag of an iTunes track.
	 */
	SORT_ALBUM("Sort Album", "sort album"),
	
	/**
	 * The sort album artist tag of an iTunes track.
	 */
	SORT_ALBUM_ARTIST("Sort Album Artist", "sort album artist"),
	
	/**
	 * The sort artist tag of an iTunes track.
	 */
	SORT_ARTIST("Sort Artist", "sort artist"),
	
	/**
	 * The sort composer tag of an iTunes track.
	 */
	SORT_COMPOSER("Sort Composer", "sort composer"),
	
	/**
	 * The sort name tag of an iTunes track.
	 */
	SORT_NAME("Sort Name", "sort name"),
	
	/**
	 * The track count tag of an iTunes track.
	 */
	TRACK_COUNT("Track Count", "track count"),
	
	/**
	 * The track number tag of an iTunes track.
	 */
	TRACK_NUMBER("Track Number", "track number"),
	
	/**
	 * The year tag of an iTunes track.
	 */
	YEAR("Year", "year");
	
	/**
	 * The display name.
	 */
	private final String name;
	
	/**
	 * The AppleScript key to retrieve the tag.
	 */
	private final String key;
	
	/**
	 * Creates a new ITunesTags object.
	 * 
	 * @param name
	 * 		The display name.
	 * @param key
	 * 		The AppleScript key to retrieve the tag.
	 */
	private ITunesTag(String name, String key) {
		this.name = name;
		this.key = key;
	}
	
	/**
	 * Gets the AppleScript key to retrieve the tag.
	 * 
	 * @return
	 * 		The AppleScript key to retrieve the tag.
	 */
	public String getKey() {
		return this.key;
	}
	
	/**
	 * Gets the display name.
	 * 
	 * @return
	 * 		The display name.
	 */
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}