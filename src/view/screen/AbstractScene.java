package view.screen;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * It is an abstract scene class that allows all other kinds of screens to extend on.
 */
public class AbstractScene {

    private Scene myScene;
    private Group myRoot;

    /**
     * To set the background image and size of the game scene
     * @param scene The scene of the game
     * @param root The root that contains all the elements that are shown on the screen
     * @param sceneLength The length of the game scene
     * @param sceneHeight The height of the game scene
     */
    public AbstractScene(Scene scene, Group root, double sceneLength, double sceneHeight) {
        myRoot = root;
        myScene = scene;
        Image backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("gamebackground.jpg"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(sceneHeight);
        backgroundImageView.setFitWidth(sceneLength);
        backgroundImageView.toBack();
        myRoot.getChildren().add(backgroundImageView);
    }

    public Scene getScene() {return myScene; }

    public Group getRoot() {return myRoot;}

}
