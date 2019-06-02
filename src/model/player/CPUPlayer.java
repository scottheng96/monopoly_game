package model.player;

import model.Transaction;
import model.location.propertySpace.PropertySpace;

/**
 * Represents an automated CPU type player to play the game
 *
 * @author leahschwartz
 */
public class CPUPlayer extends Player {

    public CPUPlayer(String name, String uniqueID) {
        super(name, uniqueID);
    }

    public CPUPlayer(String name) {
        super(name);
    }

    /**
     * Allows CPU to make money by selling houses and mortgaging properties upon going into debt
     * @param newPlayerState enum representing player's state
     */
    @Override
    public void changeState(PlayerState newPlayerState) {
        setCurrentState(newPlayerState);
        while (getCurrentState().equals(PlayerState.DEBT)) {
            for (Transaction t : getTransactions()){
            }
            makeMoneyToGetOutOfDebt();
            changeState(PlayerState.IN_TRANSACTION);
        }
    }

    private void makeMoneyToGetOutOfDebt(){
        for (PropertySpace property : getProperties()) {
            if (property.getNumberOfHouses() > 0) {
                property.sellHouse();
                break;
            }
            else if (!property.isMortgaged()) {
                property.mortgage();
                break;
            }
        }
    }
}
