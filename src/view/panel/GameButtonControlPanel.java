package view.panel;

import javafx.scene.Group;
import javafx.scene.Scene;
import model.player.Player;
import model.player.PlayerState;
import java.util.*;

import static controller.Controller.*;
import static model.player.PlayerState.*;

/**
 * This class is responsible for setting up a control panel for the game. This panel will contain buttons like roll, end
 * a turn, management, trade and auction buttons.
 *
 * @author leahschwartz
 */
public class GameButtonControlPanel extends Panel {

    private Scene myScene;

    private static final String paneID = "menuPane";
    private static final List<String> BUTTON_IDS = new ArrayList<>(Arrays.asList(ROLL_BUTTON_ID, END_TURN_BUTTON_ID,
            MANAGEMENT_BUTTON_ID, TRADE_BUTTON_ID, AUCTION_BUTTON_ID));

    /** This method sets up the control panel for the game
     * @param root root of the scene
     * @param scene scene of the monopoly
     */
    public GameButtonControlPanel(Group root, Scene scene){
        super(root);
        myScene = scene;
        makeButtonsByID(BUTTON_IDS);
        makeButtonBox();
        makePane(paneID);
    }

    /** This method determines which buttons are available based on the current player's state.
     * @param state the current state of the player
     */
    public void enableCorrectButtons(PlayerState state){
        if (state.equals(BANKRUPT)){
            disableAllButtons();
            myScene.lookup(SCENE_LOOKUP_TAG + END_TURN_BUTTON_ID).setDisable(false);
        }
        else {
            myScene.lookup(SCENE_LOOKUP_TAG + TRADE_BUTTON_ID).setDisable(state.equals(PRE_ROLL));
            myScene.lookup(SCENE_LOOKUP_TAG + ROLL_BUTTON_ID).setDisable(!state.equals(PlayerState.PRE_ROLL));
            myScene.lookup(SCENE_LOOKUP_TAG + MANAGEMENT_BUTTON_ID).setDisable(!state.equals(PlayerState.PRE_ROLL) &&
                    !state.equals(PlayerState.POST_ROLL) && !state.equals(DEBT));
            myScene.lookup(SCENE_LOOKUP_TAG + END_TURN_BUTTON_ID).setDisable(!state.equals(POST_ROLL));
            myScene.lookup(SCENE_LOOKUP_TAG + AUCTION_BUTTON_ID).setDisable(state.equals(PRE_ROLL));
        }
    }

    /** This method calls enableCorrectButtons to update the status of buttons
     * @param current current players
     * @param allPlayers all the players
     */
    public void update(Player current, Collection<Player> allPlayers){
        enableCorrectButtons(current.getCurrentState());
    }
}