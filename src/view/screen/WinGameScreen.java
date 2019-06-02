package view.screen;

import controller.FrontEndDataReader;
import controller.exceptions.MissingWinnerException;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import model.player.Player;
import java.util.Collection;

public class WinGameScreen extends SplashScreen {
    private static final String NEW_GAME = "NEW GAME";
    private static String winningPlayerName;
    private Group myRoot;
    private Button newGameButton;

    /** The constructor that set up a WinGameScreen, appearing after the game is ended
     * @param root Root of the scene
     * @param sceneWidth Width of the scene
     * @param sceneHeight Height of the scene
     * @param frontEndDataReader A reader that can be used to read the front end data
     */
    public WinGameScreen(Group root, int sceneWidth, int sceneHeight, Collection<Player> players, FrontEndDataReader frontEndDataReader) {
        super(root, sceneWidth, sceneHeight, frontEndDataReader);
        myRoot = super.getRoot();
        winningPlayerName = findWinner(players).getName();
        setUpButtons();
        setUpWinningText(sceneWidth, sceneHeight);
    }

    private Player findWinner(Collection<Player> players){
        for (Player player : players){
            if (player.isWinner()){
                return player;
            }
        }
        throw new MissingWinnerException("NO WINNER FOUND!");
    }

    protected void setUpButtons() {
        newGameButton = makeNewGameButton();
    }

    protected void setUpWinningText(int sceneLength, int sceneHeight) {
        Text winningText = new Text("Congratulations " + winningPlayerName + "!\n Time to start a New Game!");
        winningText.setId("winningText");
        winningText.setX(sceneLength/3);
        winningText.setY(sceneHeight/3);
        myRoot.getChildren().add(winningText);
    }

    /** This method returns a button that allows the user to start a new game
     * @return Button
     */
    public Button getNewGameButton() { return newGameButton; }

    private Button makeNewGameButton() {
        final int x = 500;
        final int y = 500;
        Button myNewGameButton = new Button(NEW_GAME);
        return makeButton(myNewGameButton, x, y);
    }
}
