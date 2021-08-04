package com.github.pireba.itunesrenamer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.pireba.itunesrenamer.fxml.window.MainWindow;
import com.github.pireba.utils.Utils;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Class to start the application.
 */
public class App extends Application {
	
	/**
	 * A Logger object to log messages.
	 */
	private static final Logger logger = Logger.getLogger(App.class.getName());
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage = new MainWindow();
			primaryStage.show();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error loading the FXML file of the main window", e);
		}
	}
	
	/**
	 * Start the JavaFX application.
	 * 
	 * @param args
	 * 		The arguments.
	 */
	public static void main(String[] args) {
		try {
			Utils.loadLoggingProperties();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error loading the logging properties file.", e);
		}
		
		launch(args);
	}
}