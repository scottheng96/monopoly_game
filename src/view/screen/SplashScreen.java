package view.screen;

import controller.FrontEndDataReader;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.effect.Effect;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * This screen is the parent screen of all splash screens, including the screen used to choose the version of the game,
 * the one used to set up the rules, the one used to set up the players, and the one showing up when the game ends.
 */
public abstract class SplashScreen{

    private static final int TEXT_BOUNDARY_SIZE = 10;
    private static final Color TEXT_COLOR = Color.DARKBLUE;
    private static final String FONT = "Verdana";
    private static final Color RECTANGLE_COLOR = Color.LIGHTGRAY;
    private static final int SIZE = 40;

    private Group myRoot;
    private int mySceneWidth;
    private int mySceneHeight;
    private FrontEndDataReader myFrontEndDataReader;

    public SplashScreen(Group root, int width, int height, FrontEndDataReader frontEndDataReader) {
        myRoot = root;
        mySceneWidth = width;
        mySceneHeight = height;
        myFrontEndDataReader = frontEndDataReader;
        setUpBackgroundImage();
    }

    public Group getRoot() {
        return myRoot;
    }

    /** This method gets the width of the scene.
     * @return Integer
     */
    public int getSceneWidth() {
        return mySceneWidth;
    }

    /** This method gets the height of the scene.
     * @return Integer
     */
    public int getSceneHeight() {
        return mySceneHeight;
    }

    protected Text setUpText(String message, int textSize) {
        Text text = new Text();
        text.setText(message);
        text.setFont(Font.font(FONT, textSize));
        text.setFill(TEXT_COLOR);
        final Effect glow = new Glow(1.0);
        text.setEffect(glow);
        return text;
    }

    protected StackPane setUpStackPane(Text text, double x, double y) {
        final double v = text.getBoundsInLocal().getWidth() + TEXT_BOUNDARY_SIZE * 2;
        final double v1 = text.getBoundsInLocal().getHeight() + TEXT_BOUNDARY_SIZE * 2;
        final Rectangle rectangle = new Rectangle(v, v1);
        rectangle.setFill(RECTANGLE_COLOR);
        final StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(rectangle, text);
        stackPane.setLayoutX(x);
        stackPane.setLayoutY(y);
        return stackPane;
    }

    protected void setTranslateTransitionEffect(StackPane stackPane, double fromX, double toX, double fromY, double toY) {
        final int v = 3000;
        TranslateTransition translateTransition =
                new TranslateTransition(Duration.millis(v), stackPane);
        translateTransition.setFromX(fromX);
        translateTransition.setToX(toX);
        translateTransition.setFromY(fromY);
        translateTransition.setToY(toY);
        translateTransition.play();
        myRoot.getChildren().add(stackPane);
    }

    protected void setUpFadingEffect(StackPane stackPane) {
        final int v = 3000;
        FadeTransition ft = new FadeTransition(Duration.millis(v), stackPane);
        final double v1 = 1.0;
        ft.setFromValue(v1);
        final double v2 = 0.1;
        ft.setToValue(v2);
        ft.setCycleCount(Timeline.INDEFINITE);
        ft.setAutoReverse(true);
        ft.play();
        myRoot.getChildren().add(stackPane);
    }

    protected int getTextBoundarySize() {
        return TEXT_BOUNDARY_SIZE;
    }

    protected StackPane setUpWords(String message, int size, int xPosition, int yPosition) {
        final int i = 0;
        Text text = setUpText(message, size);
        StackPane tempStackPane = setUpStackPane(text, i, i);
        final double x = xPosition - tempStackPane.getBoundsInLocal().getWidth() / 2;
        final double y = yPosition - tempStackPane.getBoundsInLocal().getHeight() / 2;
        StackPane stackPane = setUpStackPane(text, x, y);
        setUpFadingEffect(stackPane);
        return stackPane;
    }

    protected abstract void setUpButtons();

    protected String getStyleString() {
        return String.format("-fx-font: %d %s;", SIZE, FONT);
    }

    protected FrontEndDataReader getFrontEndDataReader() {
        return myFrontEndDataReader;
    }

    protected void setUpBackgroundImage() {
        Image backgroundImage = new Image(this.getClass().getClassLoader().getResourceAsStream("wallpaper.jpg"));
        ImageView backgroundImageView = new ImageView(backgroundImage);
        backgroundImageView.setFitHeight(mySceneHeight);
        backgroundImageView.setFitWidth(mySceneWidth);
        backgroundImageView.toBack();
        myRoot.getChildren().add(backgroundImageView);
    }

    protected Button makeButton(Button myButton, double x, double y) {
        myButton.setStyle(getStyleString());
        myButton.setLayoutX(x);
        myButton.setLayoutY(y);
        myRoot.getChildren().add(myButton);
        return myButton;
    }
}