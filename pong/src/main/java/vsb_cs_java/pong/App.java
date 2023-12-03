package vsb_cs_java.pong;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class App extends Application
{

	public static void main(String[] args) {
		launch(args);
	}
	
	private GameController controller;
	
	@Override
	public void start(Stage primaryStage) {
		try {
			//Construct a main window with a canvas.  			
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/pong/GameView.fxml"));
			BorderPane root = fxmlLoader.load();
			Scene scene = new Scene(root);
			controller = fxmlLoader.getController();
			
			primaryStage.setScene(scene);
			primaryStage.resizableProperty().set(false);
			primaryStage.setTitle("Java 1 - 5th laboratory");
			primaryStage.show();

			//Exit program when main window is closed
			primaryStage.setOnCloseRequest(this::exitProgram);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void exitProgram(WindowEvent evt) {
		System.exit(0);
	}
}
