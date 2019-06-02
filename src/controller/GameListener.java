package controller;

import controller.exceptions.InvalidDataFileValueException;
import javafx.beans.property.Property;
import javafx.collections.ListChangeListener;
import model.Game;
import model.Transaction;
import view.BuildingManager;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import view.panel.Panel;
import view.popup.AbstractPopUp;
import view.popup.PopUpFactory;
import view.screen.GameSceneView;
import java.util.Collection;
import java.util.List;
import static model.player.PlayerState.DEBT;
import static model.player.PlayerState.IN_TRANSACTION;

/**
 * This class is used for game listeners
 */
public class GameListener {

    private Game myGame;
    private List<Panel> myAllPanels;
    private GameSceneView myGameScene;
    private PopUpFactory myPopUpFactory;
    private AbstractPopUp myPopup;
    private FrontEndDataReader myFrontEndDataReader;

    public GameListener(Game game, List<Panel> allPanels, GameSceneView gameScene, FrontEndDataReader frontEndDataReader){
        myGame = game;
        myAllPanels = allPanels;
        myGameScene = gameScene;
        myFrontEndDataReader = frontEndDataReader;
        myPopUpFactory = new PopUpFactory();
    }

    public void addAllListeners() {
        addCurrentPlayerListeners();
        addAllPlayerListeners();
        addAllPropertyListeners();
    }

    private void addCurrentPlayerListeners(){
        myGame.getMyCurrentPlayer().addListener((observable, oldPlayer, newPlayer) -> {
            updateAllPanels(myGame.getCurrentPlayer(), myGame.getMyPlayers());
            myGameScene.getPlayerTabs().updateCurrentTab(newPlayer);
            newPlayer.getPropertiesObservable().addListener((ListChangeListener<PropertySpace>) (c ->  {
                updateAllPanels(myGame.getCurrentPlayer(), myGame.getMyPlayers());
                myGameScene.getPlayerTabs().update(newPlayer);
            }));
            myGameScene.logMessage(String.format(myGameScene.getMessage("player"), newPlayer.getName()));

        });
    }

    private void addAllPlayerListeners(){
        for (Player newPlayer : myGame.getMyPlayers()) {
            newPlayer.getLocationProperty().addListener((locationObservable, oldLocation, newLocation) -> {
                myGameScene.updatePlayerTokenMove(newPlayer.getUniqueID(), newPlayer.getCurrentLocation().getLocationNumber());
                myGameScene.logMessage(String.format(myGameScene.getMessage("movement"), newPlayer.getName(), newLocation.getName()));
            });
            newPlayer.getStateProperty().addListener((stateObservable, oldState, newState) -> {
                System.out.println("STATE of " + newPlayer.getName() + " changed to " + newState.name());
                managePopups(newPlayer, myGame.getMyPlayers());
                updateAllPanels(myGame.getCurrentPlayer(), myGame.getMyPlayers());
            });
            newPlayer.getMoneyProperty().addListener((moneyProperty, oldMoney, newMoney) -> {
                myGameScene.logMessage(String.format(myGameScene.getMessage("playerMoney"), newPlayer.getName(), newMoney));
                myGameScene.getPlayerTabs().update(newPlayer);
                if (myGame.getCurrentPlayer().getCurrentState().equals(DEBT)) {
                    myGame.getCurrentPlayer().changeState(IN_TRANSACTION);
                    myGame.getCurrentPlayer().executeStateChange(myGame.getMyBank(), myGame.getMyRoll());
                }
            });
            newPlayer.getTransactionsObservable().addListener((ListChangeListener<Transaction>) (c -> {
                myGameScene.getPlayerTabs().update(newPlayer);
                while (c.next()) {
                    for (Transaction transaction : c.getAddedSubList()) {
                        attachListenersToTransactions(transaction);
                    }
                }
            }));
        }
    }

    private void managePopups(Player player, Collection<Player> allPlayers){
        if (myPopup != null){
            myPopup.close();
        }
        try {
            myPopup = myPopUpFactory.create(player, allPlayers, myGame.getMyBank(), myGame.getMyRoll(), myFrontEndDataReader);
            myPopup.show();
        } catch (InvalidDataFileValueException e) {
            System.out.println("POPUP ERROR");
        }
    }

    private void attachListenersToTransactions(Transaction transaction){
        transaction.getIsCompleteProperty().addListener((completionProperty, oldCompletion, newCompletion) -> {
            if (transaction.isComplete()){
                myGameScene.logMessage(String.format(myGameScene.getMessage("transaction"), transaction.getPayer().getName(),
                        transaction.getPayee().getName(), transaction.getAmount()));
            }
        });
    }

    private void addAllPropertyListeners() {
        BuildingManager myBuildingManager = new BuildingManager();
        for (PropertySpace property : myGame.getAllProperties()) {
            for (Property propertyObject : property.getAllObservableProperties()) {
                propertyObject.addListener((simpleProperty, oldValue, newValue) -> {
                    updateGameOnPropertyChange(property);
                });
            }
            String name = property.getName();
            property.getOwnerPropertyObject().addListener((ownerProperty, oldOwner, newOwner) -> {
                myGameScene.logMessage(String.format(myGameScene.getMessage("owner"), newOwner.getName(), name));
            });
            property.getNumberOfHousesPropertyObject().addListener((houseProperty, oldHouses, newHouses) -> {
                myGameScene.logMessage(String.format(myGameScene.getMessage("houses"), name, newHouses));
                myBuildingManager.setBuilding(myGameScene, myGameScene.getRoot(), newHouses.intValue(),
                       property.getTopNumberOfHouses(), property.getLocationNumber());
            });
            property.getMortgagedPropertyObject().addListener((mortgageProperty, oldStatus, newStatus) -> {
                myGameScene.logMessage(String.format(myGameScene.getMessage("mortgage"), name, newStatus));
            });
            property.getRentPropertyObject().addListener((rentProperty, oldRent, newRent) ->{
                myGameScene.logMessage(String.format(myGameScene.getMessage("rent"), property.getName(), oldRent, newRent));
            });
            }
        }

       private void updateGameOnPropertyChange(PropertySpace property){
        myGameScene.getToolTip(property).updateInformation(property.getName(), property.getCost(), property.getOwner().getName(),
                property.getBuildingCost(), property.isMortgaged(), property.getNumberOfHouses(), property.getRent());
        myGame.checkRules();
        updateAllPanels(myGame.getCurrentPlayer(), myGame.getMyPlayers());
       }

    private void updateAllPanels(Player current, Collection<Player> allPlayers){
        for (Panel panel : myAllPanels){
            panel.update(current, allPlayers);
        }
    }
}

