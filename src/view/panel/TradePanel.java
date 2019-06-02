package view.panel;

import javafx.scene.Group;
import javafx.scene.control.*;
import model.Trade;
import model.Transaction;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import java.util.*;
import static controller.Controller.PROPOSE_TRADE_BUTTON_ID;
import static model.player.PlayerState.TRADING;

/**
 * This class sets up a trade pane, including all the buttons on the pane, which enable players to trade properties
 * and money during their turns
 *
 * @author leahschwartz
 */
public class TradePanel extends Panel {
    private static final int TRADE_SCENE_WIDTH = 800;
    private static final int TRADE_SCENE_HEIGHT = 600;
    private static final String TITLE = "TRADE";
    private static final String CONFIRM = "CONFIRM";
    private static final String CONFIRM_TRADEE_ID = "confirmTradeeCommand";
    private static final int CHOICEBOX_WIDTH = 50;
    private static final int CHOICEBOX_HEIGHT = 20;
    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 20;
    private static final int PROPERTY_CHOICEBOX_WIDTH = 200;
    private static final int PROPERTY_CHOICEBOX_HEIGHT = 50;
    private static final int INITIAL_MONEY_VALUE = 0;
    private Player myPlayer;
    private Map<String, Player> myCandidatesMap;
    private Button myConfirmButton;
    private ChoiceBox<String> myWhoToTradeWith;
    private ListView<String> myOfferedProperties;
    private ListView<String> myRequestedProperties;
    private Spinner<Integer> myOfferedMoney;
    private Spinner<Integer> myRequestedMoney;
    private Map<String, PropertySpace> myPropertiesByName;
    private Player myRecipient;

    /**
     * This method sets up a trade pane.
     */
    public TradePanel() {
        super(new Group());
        setUpNewWindow(TRADE_SCENE_WIDTH, TRADE_SCENE_HEIGHT, TITLE);
        makeButtonsByID(new ArrayList<>(Arrays.asList(CONFIRM_TRADEE_ID, PROPOSE_TRADE_BUTTON_ID)));
        getButtonByID(CONFIRM_TRADEE_ID).setVisible(false);
    }

    private void setUpChoiceBox() {
        myWhoToTradeWith = new ChoiceBox<>();
        myWhoToTradeWith.setPrefSize(CHOICEBOX_WIDTH, CHOICEBOX_HEIGHT);
        myWhoToTradeWith.setLayoutX(TRADE_SCENE_WIDTH / 2.0 - myWhoToTradeWith.getPrefWidth() / 2);
        myWhoToTradeWith.setLayoutY(TRADE_SCENE_HEIGHT / 2.0);
        getRoot().getChildren().add(myWhoToTradeWith);
    }

    private void setPotentialTradingCandidates(Player current, Collection<Player> playerList) {
        myCandidatesMap = new HashMap<>();
        for (Player player: playerList) {
            if (!player.getUniqueID().equals(current.getUniqueID()) && !myCandidatesMap.containsKey(player.getName())) {
                myCandidatesMap.put(player.getName(), player);
            }
        }
        myWhoToTradeWith.getItems().clear();
        myWhoToTradeWith.getItems().addAll(myCandidatesMap.keySet());
    }

    private void setUpConfirmButton() {
        myConfirmButton = new Button(CONFIRM);
        myConfirmButton.setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        myConfirmButton.setLayoutX(TRADE_SCENE_WIDTH / 2.0 - myConfirmButton.getPrefWidth() / 2);
        myConfirmButton.setLayoutY(TRADE_SCENE_HEIGHT / 3.0 * 2);
        myConfirmButton.setDisable(true);
        getRoot().getChildren().add(myConfirmButton);
    }

    private void setConfirmButtonAction() {
        myWhoToTradeWith.getSelectionModel().selectedItemProperty().addListener(selection -> {
            myConfirmButton.setDisable(false);
        });

        myConfirmButton.setOnAction((click) -> {
            myRecipient = myCandidatesMap.get(myWhoToTradeWith.getValue());
            getRoot().getChildren().clear();
            setUpText(TRADE_SCENE_WIDTH / 2, TRADE_SCENE_HEIGHT, String.format("%s's Property", myPlayer.getName()));
            setUpText(TRADE_SCENE_WIDTH / 2, TRADE_SCENE_HEIGHT * 2, String.format("Extra money \n from %s", myPlayer.getName()));
            setUpOfferedProperties();
            setUpText(TRADE_SCENE_WIDTH / 2 * 3, TRADE_SCENE_HEIGHT, String.format("%s's Property", myWhoToTradeWith.getValue()));
            setUpRequestedProperties();
            setUpText(TRADE_SCENE_WIDTH / 2 * 3, TRADE_SCENE_HEIGHT * 2, String.format("Extra money \n from %s", myWhoToTradeWith.getValue()));
            setUpMoneySpinners();
            setUpDoneButton();
        });
    }

    private void setUpOfferedProperties() {
        myPropertiesByName = new HashMap<>();
        myOfferedProperties = makeNewPropertyChoiceBox(myPlayer.getProperties(),TRADE_SCENE_WIDTH / 4.0,
                TRADE_SCENE_HEIGHT / 3.0 );
    }

    private void setUpRequestedProperties() {
        myRequestedProperties = makeNewPropertyChoiceBox(myRecipient.getProperties(),TRADE_SCENE_WIDTH / 4.0 * 3,
                TRADE_SCENE_HEIGHT / 3.0 );
    }

    private ListView<String> makeNewPropertyChoiceBox(List<PropertySpace> properties, double xCoordinate, double yCoordinate){
        ListView<String> propertiesViewer = new ListView<>();
        propertiesViewer.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        propertiesViewer.setPrefSize(PROPERTY_CHOICEBOX_WIDTH, PROPERTY_CHOICEBOX_HEIGHT);
        for (PropertySpace propertySpace: properties) {
            myPropertiesByName.put(propertySpace.getName(), propertySpace);
            propertiesViewer.getItems().add(propertySpace.getName());
        }
        propertiesViewer.setLayoutX(xCoordinate - propertiesViewer.getPrefWidth() / 2);
        propertiesViewer.setLayoutY(yCoordinate);
        getRoot().getChildren().add(propertiesViewer);
        return propertiesViewer;
    }

    private void setUpMoneySpinners(){
        myOfferedMoney = setUpNewMoneySpinner(myPlayer.getMoneyBalance(),
                TRADE_SCENE_WIDTH / 4.0, TRADE_SCENE_HEIGHT / 7.0 * 5);
        myRequestedMoney = setUpNewMoneySpinner( myRecipient.getMoneyBalance(),
                TRADE_SCENE_WIDTH / 4.0 * 3, TRADE_SCENE_HEIGHT / 7.0 * 5);
    }


    private Spinner<Integer> setUpNewMoneySpinner(int playerMoney, double xCoordinate, double yCoordinate){
        Spinner<Integer> moneySpinner = new Spinner<>();
        moneySpinner.setPrefSize(PROPERTY_CHOICEBOX_WIDTH, PROPERTY_CHOICEBOX_HEIGHT);
        moneySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, playerMoney , INITIAL_MONEY_VALUE, 100));
        moneySpinner.relocate(xCoordinate - moneySpinner.getPrefWidth() / 2, yCoordinate);
        getRoot().getChildren().add(moneySpinner);
        return moneySpinner;
    }

    private void setUpDoneButton() {
        getButtonByID(PROPOSE_TRADE_BUTTON_ID).setVisible(true);
        getButtonByID(PROPOSE_TRADE_BUTTON_ID).setPrefSize(BUTTON_WIDTH, BUTTON_HEIGHT);
        getButtonByID(PROPOSE_TRADE_BUTTON_ID).setLayoutX(TRADE_SCENE_WIDTH / 2.0 - getButtonByID(PROPOSE_TRADE_BUTTON_ID).getPrefWidth() / 2);
        getButtonByID(PROPOSE_TRADE_BUTTON_ID).setLayoutY(TRADE_SCENE_HEIGHT / 7.0 * 6);
        getButtonByID(PROPOSE_TRADE_BUTTON_ID ).setDisable(false);
        getRoot().getChildren().add(getButtonByID(PROPOSE_TRADE_BUTTON_ID ));
    }

    public void update(Player current, Collection<Player> allPlayers){
        getRoot().getChildren().clear();
        myPlayer = current;
        setUpChoiceBox();
        setUpText(TRADE_SCENE_WIDTH, TRADE_SCENE_HEIGHT, "Select Another Player To Trade");
        setUpConfirmButton();
        setConfirmButtonAction();
        setPotentialTradingCandidates(current, allPlayers);
    }

    public void setUpTradeUsingChosenData(){
        int netMoney = myRequestedMoney.getValue() - myOfferedMoney.getValue();
        Transaction tradeToMake = new Trade(myPlayer, getPropertySpacesFromPickers(myOfferedProperties), myRecipient,
                getPropertySpacesFromPickers(myRequestedProperties), netMoney);
        myRecipient.addTransaction(tradeToMake);
        myRecipient.changeState(TRADING);
    }


    private List<PropertySpace> getPropertySpacesFromPickers(ListView<String> chooser){
        List<PropertySpace> playerProperties =  new ArrayList<>();
        for (String propertyName : chooser.getSelectionModel().getSelectedItems()){
            playerProperties.add(myPropertiesByName.get(propertyName));
        }
        return playerProperties;
    }

    /** This method returns the player that is going to get the property.
     * @return a player who is going to receive the property
     */
    public Player getRecipient(){
        return myRecipient;
    }
}