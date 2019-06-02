package view.screen;

import controller.FrontEndDataReader;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

/**
 * This class set up the welcome screen that welcomes the user to the game. On this screen, the user will be able to start
 * a new game and load an old game.
 */
public class WelcomeScreen extends SplashScreen {
    private static final String WELCOME = "WELCOME TO MONOPOLY";
    private static final String COPYRIGHT = "TEAM07 ENTERTAINMENT";
    private static final int WELCOME_TEXT_SIZE = 70;
    private static final int COPYRIGHT_TEXT_SIZE = 40;
    private static final String START = "NEW GAME";
    private static final String LOAD = "LOAD GAME";
    private static final String NEW_GAME = "NEW GAME";
    private static final int myCorrection = 10;
    private Button myStartButton;
    private Button myLoadButton;

    /** The constructor that set up a WelcomeScreen
     * @param root Root of the scene
     * @param sceneWidth Width of the scene
     * @param sceneHeight Height of the scene
     * @param frontEndDataReader A reader that can be used to read the front end data
     */
    public WelcomeScreen(Group root, int sceneWidth, int sceneHeight, FrontEndDataReader frontEndDataReader) {
        super(root, sceneWidth, sceneHeight, frontEndDataReader);
        setUpWelcomeScene();
    }

    private void setUpWelcomeScene() {
        setUpWelcomeText();
        setUpCopyrightText();
        setUpButtons();
    }

    private void setUpWelcomeText() {
        Text welcomeText = setUpText(WELCOME, WELCOME_TEXT_SIZE);
        welcomeText.setId(String.format("fancytext"));
        final int v = 0;
        StackPane stackPaneTemp = setUpStackPane(welcomeText, v, v);
        final double x = getSceneWidth() / 2 - stackPaneTemp.getBoundsInLocal().getWidth() / 2;
        final double y = getSceneHeight() / 2 - stackPaneTemp.getBoundsInLocal().getHeight() - getTextBoundarySize() * 12;
        StackPane stackPaneWelcome = setUpStackPane(welcomeText, x, y);
        setUpFadingEffect(stackPaneWelcome);
    }

    private void setUpCopyrightText() {
        Text copyrightText = setUpText(COPYRIGHT, COPYRIGHT_TEXT_SIZE);
        final int v = 0;
        StackPane stackPaneCopyright = setUpStackPane(copyrightText, v, v);
        final double x = getSceneWidth() - stackPaneCopyright.getBoundsInLocal().getWidth() - getTextBoundarySize() * 15;
        final int y = getSceneHeight() / 7 * 3 - getTextBoundarySize() * 4;
        setTranslateTransitionEffect(stackPaneCopyright, v, x, y, y);
    }

    @Override
    protected void setUpButtons() {
        myStartButton = makeWelcomeStartButton();
        myLoadButton = makeLoadButton();
    }

    /** This method returns a button that allow the user to start a new game.
     * @return Button
     */
    public Button getWelcomeReadyButton(){
        return myStartButton;
    }

    /** This method returns a button that allow the user to load previous games.
     * @return Button
     */
    public Button getWelcomeLoadButton(){
        return myLoadButton;
    }

    private Button makeWelcomeStartButton() {
        Button myButton = new Button(START);
        final int myXPosition = getSceneWidth() / 3 * 2 - myCorrection * 6;
        final int myYPosition = getSceneHeight() / 3 * 2 - myCorrection * 10;
        return makeButton(myButton, myXPosition, myYPosition);
    }

    private Button makeLoadButton(){
        Button myButton = new Button(LOAD);
        final int x = getSceneWidth() / 3 * 2 - myCorrection * 6;
        final int y = getSceneHeight() / 3 * 2;
        return makeButton(myButton, x, y);
    }
}