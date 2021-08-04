package com.github.pireba.itunesrenamer;

/**
 * Helper class to start the application.<br>
 * If the main method is in a class that extends {@linkplain javafx.application.Application Application}, 
 * the javafx.graphics module must be present as a named module.
 * If this module is not present, the launch is aborted.
 * To avoid this, you must provide a new main class that is not extended by {@linkplain javafx.application.Application Application}.
 * 
 * @see <a href="https://stackoverflow.com/questions/52653836/maven-shade-javafx-runtime-components-are-missing">Maven Shade JavaFX runtime components are missing</a>
 */
public class AppLauncher {
	
	/**
	 * Start the main method of the {@linkplain App} class.
	 * 
	 * @param args
	 * 		The arguments.
	 */
	public static void main(String[] args) {
		App.main(args);
	}
}