package view.panel;

import controller.exceptions.PropertyManagementException;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.paint.Color;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import model.player.Turn;
import java.lang.reflect.Method;
import java.util.*;

import static controller.Controller.SCENE_LOOKUP_TAG;

/**
 * This class sets up a management panel which have functionality such as buying houses, selling houses or mortgaging
 *
 * @author leahschwartz
 */
public class ManagementPanel extends Panel {

    private static final int MANAGEMENT_SCENE_WIDTH = 800;
    private static final int MANAGEMENT_SCENE_HEIGHT = 600;
    private static final Color BACKGROUND = Color.AZURE;
    private static final String TITLE = "MANAGEMENT";
    private static final String PANE_ID = "managementPane";
    private static final String ADD_HOUSE_ID = "addHouseCommand";
    private static final String SELL_HOUSE_ID = "sellHouseCommand";
    private static final String MORTGAGE_ID = "mortgageCommand";
    private static final String UNMORTGAGE_ID = "unmortgageCommand";
    private static final int CHOICEBOX_WIDTH = 400;
    private static final int CHOICEBOX_HEIGHT = 50;
    private ChoiceBox<String> myChoiceBox;
    private Map<String, PropertySpace> myCurrentProperties;
    private PropertySpace selected;
    private Turn myTurn;


    private ResourceBundle myReflectionResources = ResourceBundle.getBundle("management_button_actions");

    /** This methods sets up a management panel
     * @throws PropertyManagementException the property configuration is improper
     */
    public ManagementPanel() throws PropertyManagementException{
        super(new Group());
        setUpNewWindow(MANAGEMENT_SCENE_WIDTH, MANAGEMENT_SCENE_HEIGHT, TITLE);
        myChoiceBox = new ChoiceBox<>();
        setUpText(MANAGEMENT_SCENE_WIDTH, MANAGEMENT_SCENE_HEIGHT, String.format("Manage My Properties"));
        makeButtonsByID(new ArrayList<>(Arrays.asList(ADD_HOUSE_ID, SELL_HOUSE_ID, MORTGAGE_ID, UNMORTGAGE_ID)));
        makeButtonBox();
        makePane(PANE_ID);
        setUpChoiceBox();
        disableAllButtons();
        try{
        setManagementButtonActions();
        } catch (PropertyManagementException e){
            throw new PropertyManagementException("IMPROPER PROPERTY CONFIGURATION FOR MANAGEMENT");
        }
    }


    private void setUpChoiceBox() {
        myChoiceBox.setPrefSize(CHOICEBOX_WIDTH, CHOICEBOX_HEIGHT);
        myChoiceBox.setLayoutX(MANAGEMENT_SCENE_WIDTH / 2.0 - myChoiceBox.getPrefWidth() / 2);
        myChoiceBox.setLayoutY(MANAGEMENT_SCENE_HEIGHT / 2.0);
        myChoiceBox.getSelectionModel().selectedItemProperty().addListener((property, oldSelection, newSelection) -> {
            enableManagementButtons();
        });
        getRoot().getChildren().add(myChoiceBox);
    }

    private void setPropertySelection(List<PropertySpace> properties) {
        Map<String, PropertySpace> newProperties = getNewPropertiesMap(properties);
        if (!newProperties.keySet().equals(new HashSet<>(myChoiceBox.getItems()))) {
            myCurrentProperties = newProperties;
            myChoiceBox.getItems().clear();
            for (String propertyName : (newProperties.keySet())) {
                myChoiceBox.getItems().add(propertyName);
            }
        }
    }

    private void enableManagementButtons(){
        disableAllButtons();
        if (myChoiceBox.getValue() != null) {
            selected = myCurrentProperties.get(myChoiceBox.getValue());
            getScene().lookup(SCENE_LOOKUP_TAG + ADD_HOUSE_ID).setDisable(!selected.canHousesBeAdded());
            getScene().lookup(SCENE_LOOKUP_TAG + SELL_HOUSE_ID).setDisable(selected.getNumberOfHouses() < 1);
            getScene().lookup(SCENE_LOOKUP_TAG + MORTGAGE_ID).setDisable(selected.isMortgaged() || selected.getNumberOfHouses() > 0);
            getScene().lookup(SCENE_LOOKUP_TAG + UNMORTGAGE_ID).setDisable(!selected.isMortgaged() ||
                    !selected.getOwner().hasEnoughMoney(selected.getUnmortgageCost()));
        }
    }

    private void setManagementButtonActions() throws PropertyManagementException{
            for (Button button : getAllButtons()) {
                button.setOnAction((click) -> {
                    callManagementMethod(myReflectionResources.getString(button.getId()), selected);
                    enableManagementButtons();
            });
        }
    }

    private void callManagementMethod (String methodName, PropertySpace selected)  {
        try {
            Method m = selected.getClass().getMethod(methodName);
            m.invoke(selected);
        } catch (Exception e) {
            throw new PropertyManagementException("IMPROPER PROPERTY CONFIGURATION FOR MANAGEMENT");
        }
    }

    /** This method enables the management buttons on the management panel
     * @param current current player
     * @param allPlayers all the players
     */
    public void update(Player current, Collection<Player> allPlayers){
        enableManagementButtons();
        setPropertySelection(current.getProperties());
    }
}
