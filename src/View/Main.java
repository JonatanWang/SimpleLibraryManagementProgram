package View;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * The entrance of the program
 * @author 
 */
public class Main extends Application {

    @Override   // Overide the start method of Application
    public void start(Stage primaryStage) {
        
        LibraryView view = new LibraryView(primaryStage);
        view.setStyle("-fx-background-color: gold");
        
        Scene scene = new Scene(view, 1200, 800);
        
        
        primaryStage.setTitle("Library");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
