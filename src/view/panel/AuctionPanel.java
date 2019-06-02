package view.panel;

import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import java.util.*;

import static controller.Controller.BEGIN_AUCTION_BUTTON_ID;

/**
 * Represents the panel that allow players to auction off properties to one another during the game
 *
 * @author leahschwartz
 */
public class AuctionPanel extends Panel {

    private static final int AUCTION_SCENE_WIDTH = 800;
    private static final int AUCTION_SCENE_HEIGHT = 600;
    private static final String TITLE = "AUCTION";
    private static final String AUCTION_PANE_ID = "auctionPane";
    private static final String AUCTION_CHOICE_BOX_ID = "auctionChoiceBox";
    private ChoiceBox<String> myPropertyChoices;
    private Map<String, PropertySpace> myPropertyMap;

    /**
     * To extend the abstract Panel class and set basic elements such as texts and buttons.
     */
    public AuctionPanel () {
        super(new Group());
        setUpNewWindow(AUCTION_SCENE_WIDTH, AUCTION_SCENE_HEIGHT, TITLE);
        setUpText(AUCTION_SCENE_WIDTH / 2, AUCTION_SCENE_HEIGHT, "AUCTION!");
        makeButtonsByID(new ArrayList<>(Collections.singletonList(BEGIN_AUCTION_BUTTON_ID)));
        makeButtonBox();
        makePane(AUCTION_PANE_ID);
        setUpPanelElement();
    }

    /**
     * To set up elements of Auction Panel
     */
    private void setUpPanelElement(){
        myPropertyChoices = new ChoiceBox<>();
        myPropertyChoices.setId(AUCTION_CHOICE_BOX_ID);
        myPropertyChoices.relocate(AUCTION_SCENE_WIDTH / 2.0, AUCTION_SCENE_HEIGHT / 2.0);
        getButtonBox().getChildren().add(myPropertyChoices);
        myPropertyChoices.getSelectionModel().selectedItemProperty().addListener(selection -> {
            getButtonByID(BEGIN_AUCTION_BUTTON_ID).setDisable(false);
        });
    }

    /**
     * Updates the auction panel as game proceeds to ensure correct properties are shown
     * @param current The current player
     * @param allPlayers All the players in the game
     */
    @Override
    public void update(Player current, Collection<Player> allPlayers) {
        myPropertyChoices.getItems().clear();
        myPropertyMap = getNewPropertiesMap(current.getProperties());
        myPropertyChoices.getItems().addAll(myPropertyMap.keySet());
    }

    /** Returns the property that was to be auctioned.
     * @return PropertySpace
     */
    public PropertySpace getAuctionProperty(){
        return myPropertyMap.get(myPropertyChoices.getValue());
    }

    @Override
    public void show() {
        getButtonByID(BEGIN_AUCTION_BUTTON_ID).setDisable(true);
        super.show();
    }
}
