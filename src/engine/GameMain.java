package engine;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class GameMain extends Application {

    private Scene myScene;
    private Stage myStage;
    private static final String TITLE = "MONOPOLY";

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) {
        myStage = stage;
        myStage.setResizable(false);

        Controller c = new Controller();
        myScene = c.getScene();
        myScene.setOnKeyPressed(key -> c.handleKeyInput(key.getCode()));
        setStyleSheet();

        myStage.setScene(myScene);
        myStage.setTitle(TITLE);
        myStage.show();
    }

    private void setStyleSheet() {
        URL url = this.getClass().getResource("main.css");
        if (url == null) {
            System.out.println("Resource not found. Aborting.");
            System.exit(-1);
        }
        String css = url.toExternalForm();
        myScene.getStylesheets().add(css);
    }


    /**
     * Start the program.
     */
    public static void main (String[] args){
        launch(args);
    }
}
