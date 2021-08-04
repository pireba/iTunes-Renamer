package com.github.pireba.itunesrenamer.fxml.window;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.github.pireba.itunesrenamer.fxml.action.Action;
import com.github.pireba.itunesrenamer.fxml.action.SearchAndReplace;
import com.github.pireba.itunesrenamer.fxml.control.ITunesTagComboBox;
import com.github.pireba.itunesrenamer.fxml.control.TracksTableView;
import com.github.pireba.itunesrenamer.fxml.pane.ActionsVBox;
import com.github.pireba.itunesrenamer.model.ITunes;
import com.github.pireba.itunesrenamer.model.ITunesTag;
import com.github.pireba.itunesrenamer.model.Track;
import com.github.pireba.utils.Utils;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.stage.Stage;

/**
 * The iTunes-Renamer main window.
 */
public class MainWindow extends Stage implements Initializable {
	
	/**
	 * A Logger object to log messages.
	 */
	private static final Logger logger = Logger.getLogger(MainWindow.class.getName());
	
	/**
	 * A ComboBox to select an {@linkplain ITunesTag}.
	 */
	@FXML private ITunesTagComboBox comboBoxITunesTag;
	
	/**
	 * A Button to import the tracks from iTunes.
	 */
	@FXML private Button buttonImport;
	
	/**
	 * A VBox to display and arrange the actions.
	 */
	@FXML private ActionsVBox vboxActions;
	
	/**
	 * A Button to add a new action.
	 */
	@FXML private Button buttonAddAction;
	
	/**
	 * A Button to remove all selected actions.
	 */
	@FXML private Button buttonRemoveAction;
	
	/**
	 * A ProgressIndicator to show the progress of a running Task.
	 */
	@FXML private ProgressIndicator progressIndicator;
	
	/**
	 * A TableView to display the imported tracks from iTunes.
	 */
	@FXML private TracksTableView tableTracks;
	
	/**
	 * A Label to show how many tracks will be renamed.
	 */
	@FXML private Label labelOverview;
	
	/**
	 * A Button to apply the new names to the tracks in iTunes.
	 */
	@FXML private Button buttonApply;
	
	/**
	 * Creates a new main window.
	 * 
	 * @throws IOException
	 * 		If an error occurs when loading the FXML file.
	 */
	public MainWindow() throws IOException {
		Scene scene = new Scene(Utils.loadFXML(this));
		this.setScene(scene);
		
		this.setTitle("iTunes-Renamer");
		this.setMinWidth(800.0);
		this.setMinHeight(600.0);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.addAction();
		this.addActionListChangeListener();
		this.addTableDisabledListener();		
	}
	
	/**
	 * Adds a ChangeListener that is notified when the list of actions is changed.
	 */
	private void addActionListChangeListener() {
		this.vboxActions.getChildren().addListener((ListChangeListener<? super Node>) (change) -> {
			this.rename();
		});
	}
	
	/**
	 * Adds a ChangeListener that will be notified when the table is disabled.
	 * Then the ProgressIndicator is moved to the front.
	 * Otherwise it will be moved to the back.
	 */
	private void addTableDisabledListener() {
		this.tableTracks.disableProperty().addListener((observable, oldValue, newValue) -> {
			if ( newValue ) {
				this.progressIndicator.toFront();
			} else {
				this.progressIndicator.toBack();
			}
		});
	}
	
	/**
	 * Creates a new action with all default listeners.
	 * 
	 * @param clazz
	 * 		The class of the action object to be created.
	 * @return
	 * 		The new action object.
	 */
	private Action createNewAction(Class<? extends Action> clazz) {
		try {
			Constructor<? extends Action> constructor = clazz.getConstructor();
			Action action = constructor.newInstance();
			this.addControlChangeListener(action);
			this.addActionReplaceListener(action);
			return action;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error creating new action.", e);
		}
		
		return null;
	}
	
	/**
	 * Adds a ChangeListener that is notified when an action control is changed.
	 * 
	 * @param action
	 * 		The action to which the listener should be added.
	 */
	private void addControlChangeListener(Action action) {
		action.addControlChangeListener((observable, oldValue, newValue) -> {
			this.rename();
		});
	}
	
	/**
	 * Adds a ChangeListener that will be notified when another action in the ComboBox is selected.
	 * 
	 * @param action
	 * 		The action to which the listener should be added.
	 */
	private void addActionReplaceListener(Action action) {
		action.addActionReplaceListener((observable, oldValue, newValue) -> {
			Action newAction = this.createNewAction(newValue);
			int index = this.vboxActions.getChildren().indexOf(action);
			this.vboxActions.getChildren().set(index, newAction);
		});
	}
		
	/**
	 * Add a new default action.
	 * The default action is {@linkplain SearchAndReplace}.
	 */
	@FXML
	private void addAction() {
		this.addAction(SearchAndReplace.class);
	}
	
	/**
	 * Add a new action.
	 * 
	 * @param clazz
	 * 		The class of the action to be added.
	 */
	private void addAction(Class<? extends Action> clazz) {
		Action action = this.createNewAction(clazz);
		this.vboxActions.getChildren().add(action);
	}
	
	/**
	 * Remove the selected actions.
	 */
	@FXML
	private void removeAction() {
		this.vboxActions.getChildren().removeIf((node) -> {
			Action action = (Action) node;
			return action.isSelected();
		});
	}
	
	/**
	 * Update the text of the overview label.
	 */
	private void updateOverview() {
		String format = "%d of %d tracks to rename";
		int count = ITunes.getRenameCount();
		int total = this.tableTracks.getItems().size();
		
		this.labelOverview.setText(String.format(format, count, total));
	}
	
	/**
	 * Import the tracks from iTunes.
	 */
	@FXML
	private void importFromITunes() {
		ITunesTag tag = this.comboBoxITunesTag.getValue();
		Task<ObservableList<Track>> task = ITunes.getLoadTask(tag);
		
		task.setOnFailed((event) -> {
			logger.log(Level.SEVERE, "Error importing tracks from iTunes.", task.getException());
		});
		
		task.setOnRunning((event) -> {
			this.tableTracks.disableProperty().bind(task.runningProperty());
		});
		
		task.setOnSucceeded((event) -> {
			ObservableList<Track> list = task.getValue();
			this.tableTracks.setItems(list);
			this.rename();
			this.updateOverview();
		});
		
		Thread thread = new Thread(task);
		thread.start();
	}
	
	/**
	 * Apply the new names to the tracks in iTunes.
	 */
	@FXML
	private void apply() {
		ITunesTag tag = this.comboBoxITunesTag.getValue();
		Task<Void> task = ITunes.getRenameTask(tag);
				
		task.setOnFailed((event) -> {
			logger.log(Level.SEVERE, "Error applying the new names to iTunes.", task.getException());
		});
		
		task.setOnRunning((event) -> {
			this.buttonApply.setText("Cancel");
			this.buttonApply.setOnAction((e) -> task.cancel(true));
			
			this.comboBoxITunesTag.disableProperty().bind(task.runningProperty());
			this.buttonImport.disableProperty().bind(task.runningProperty());
			this.vboxActions.disableProperty().bind(task.runningProperty());
			this.buttonAddAction.disableProperty().bind(task.runningProperty());
			this.buttonRemoveAction.disableProperty().bind(task.runningProperty());
			this.tableTracks.disableProperty().bind(task.runningProperty());
			this.progressIndicator.progressProperty().bind(task.progressProperty());
		});
		
		task.runningProperty().addListener((observable, oldValue, newValue) -> {
			if ( ! newValue ) {
				this.buttonApply.setText("Apply");
				this.buttonApply.setOnAction((e) -> this.apply());
				
				this.tableTracks.refresh();
				this.updateOverview();
				
				this.progressIndicator.progressProperty().unbind();
				this.progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
			}
		});
		
		Thread thread = new Thread(task);
		thread.start();
	}
	
	/**
	 * Rename the tracks.
	 * The result of each renaming action is applied to the tracks one by one.
	 */
	private void rename() {
		// Do nothing if no tracks are loaded.
		if ( this.tableTracks.getItems().isEmpty() ) {
			return;
		}
		
		Task<String[]> task = this.vboxActions.getRenameTask();
		
		task.setOnFailed((event) -> {
			logger.log(Level.SEVERE, "Error while renaming the tracks.", task.getException());
		});
		
		task.setOnRunning((event) -> {
			this.tableTracks.disableProperty().bind(task.runningProperty());
		});
		
		task.setOnSucceeded((event) -> {
			String[] result = task.getValue();
			ITunes.setNewNames(result);
			this.updateOverview();
		});
		
		Thread thread = new Thread(task);
		thread.start();
	}
}