package view.screen;

import controller.FrontEndDataReader;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * To make the screen that allows players to choose what version of game they want to play with.
 */
public class GameVersionChosenScreen extends SplashScreen {

    static private final String GAME_VERSION_CHOSEN_MESSAGE = "CHOOSE YOUR VERSION";
    static private final int GAME_VERSION_CHOSEN_MESSAGE_SIZE = 40;
    static private final int GAME_VERSION_CHOSEN_TEXT_SIZE = 30;
    static private final int SCREEN_BOTTOM_BUFFER_HEIGHT = 100;
    static private final int SCREEN_BOTTOM_BUFFER_WIDTH = 50;
    private final String GENERAL_PATH = "./resources/";
    private final String SLASH = "/";
    private static final String READY = "READY";
    private static final String PREV = "PREV";
    private static final String NEXT = "NEXT";
    private static final int myCorrection = 10;

    private Button myReadyButton;
    private Button myNextButton;
    private Button myPrevButton;
    private int myGameVersionInteger;
    private Map<String, Image> myGameVersions;
    private ImageView myImage;

    /**
     * To extend the abstract SplashScreen class.
     * @param root The root that contains all the elements that are shown on the screen
     * @param width The width of the game scene screen
     * @param height The height of the game scene screen
     * @param frontEndDataReader The frontEndDataReader that reads data from the frontend.
     */
    public GameVersionChosenScreen(Group root, int width, int height, FrontEndDataReader frontEndDataReader) {
        super(root, width, height, frontEndDataReader);
        myGameVersionInteger = 0;
        setScreen(getFrontEndDataReader());
    }

    /**
     * To set the pictures, features, and buttons for the gameVersionChosenScreen.
     */
    private void setScreen(FrontEndDataReader myFrontEndDataReader) {
        final int xPosition = getSceneWidth() / 2 - SCREEN_BOTTOM_BUFFER_WIDTH;
        final int yPosition = getSceneHeight() / 6 - SCREEN_BOTTOM_BUFFER_HEIGHT;
        setUpWords(GAME_VERSION_CHOSEN_MESSAGE,
                GAME_VERSION_CHOSEN_MESSAGE_SIZE,
                xPosition,
                yPosition);
        setUpGameVersionPictures();
        setUpButtons();
    }

    /**
     * To add necessary pictures that represent different game versions to the screen.
     */

    public void setUpGameVersionPictures() {
        myGameVersions = getFrontEndDataReader().getGameVersions();
        List<String> gameVersionNames = new ArrayList<>(myGameVersions.keySet());
        myImage = new ImageView(myGameVersions
                .get(gameVersionNames.get(myGameVersionInteger)));
        final double v = getSceneWidth() / 2.0;
        myImage.setFitWidth(v);
        myImage.setPreserveRatio(true);
        final double v1 = myImage.getBoundsInLocal().getWidth() / 2;
        myImage.setLayoutX(v - v1 - SCREEN_BOTTOM_BUFFER_WIDTH);
        final double v2 = getSceneHeight() / 2.0;
        final double v3 = myImage.getBoundsInLocal().getHeight() / 2;
        final int i = getTextBoundarySize() * 2;
        myImage.setLayoutY(v2 - v3 - i - SCREEN_BOTTOM_BUFFER_HEIGHT);

        getRoot().getChildren().add(myImage);
        final int xPosition = getSceneWidth() / 2 - SCREEN_BOTTOM_BUFFER_WIDTH;
        final int yPosition = getSceneHeight() / 2 - SCREEN_BOTTOM_BUFFER_HEIGHT;
        setUpWords(gameVersionNames.get(myGameVersionInteger), GAME_VERSION_CHOSEN_TEXT_SIZE,
                xPosition, yPosition);
    }

    /**
     * To set up buttons for choosing previous version, choosing next version, and confirming the chosen version.
     */
    @Override
    protected void setUpButtons() {
        myPrevButton = makeVersionChosenScreenPrevButton();
        myNextButton = makeVersionChosenScreenNextButton();
        myReadyButton = makeVersionChosenScreenReadyButton();
        setDirectionButtonActions();
    }

    private Button makeVersionChosenScreenPrevButton() {
        final int xPosition = getSceneWidth() / 4 + myCorrection;
        final int yPosition = getSceneHeight() / 5 * 4 - SCREEN_BOTTOM_BUFFER_HEIGHT;
        Button myButton = new Button(PREV);
        return makeButton(myButton, xPosition, yPosition);
    }

    private Button makeVersionChosenScreenNextButton() {
        final int xPosition = getSceneWidth() / 5 * 3 - myCorrection * 6;
        final int yPosition = getSceneHeight() / 5 * 4 - SCREEN_BOTTOM_BUFFER_HEIGHT;
        Button myButton = new Button(NEXT);
        return makeButton(myButton, xPosition, yPosition);
    }

    private Button makeVersionChosenScreenReadyButton() {
        final int xPosition = getSceneWidth() / 2 - myCorrection * 15;
        final int yPosition = getSceneHeight() / 5 * 4 - SCREEN_BOTTOM_BUFFER_HEIGHT;
        Button myButton = new Button(READY);
        return makeButton(myButton, xPosition, yPosition);
    }


    private void setDirectionButtonActions(){
       getNextButton().setOnAction((ActionEvent nextVersionEvent) -> {
            setToNextGameVersion(getFrontEndDataReader());
        });

        getPrevButton().setOnAction((ActionEvent prevVersionEvent) -> {
            setToPreviousGameVersion(getFrontEndDataReader());
        });
    }

    public int getVersionNumber() {
        return myGameVersionInteger;
    }

    public void setGameVersionInteger(int integer) {
        getRoot().getChildren().remove(myImage);
        myGameVersionInteger = integer;
    }

    public Map<String, Image> getGameVersions() {
        return myGameVersions;
    }

    public Button getReadyButton(){
        return myReadyButton;
    }

    public Button getNextButton() {
        return myNextButton;
    }

    public Button getPrevButton() {
        return myPrevButton;
    }

    public String getGameVersion(){
        System.out.println(String.format("%s%s%s", GENERAL_PATH, new ArrayList<>(getGameVersions().keySet()).
                get(getVersionNumber()).toLowerCase(), SLASH));
        return String.format("%s%s%s", GENERAL_PATH, new ArrayList<>(getGameVersions().keySet()).
                get(getVersionNumber()).toLowerCase(), SLASH);
    }


    /**
     * To get to the next version of the game
     */
    public void setToNextGameVersion(FrontEndDataReader frontendReader){
        setGameVersionInteger((myGameVersionInteger + 1) % getGameVersions().size());
        setUpGameVersionPictures();
    }

    /**
     * To get to the previous version of the game
     */
    public void setToPreviousGameVersion(FrontEndDataReader frontendReader){
        setGameVersionInteger(myGameVersionInteger == 0 ? myGameVersionInteger + getGameVersions().size() - 1 : myGameVersionInteger - 1);
        setUpGameVersionPictures();
    }
}