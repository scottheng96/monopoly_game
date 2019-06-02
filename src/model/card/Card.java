package model.card;

import model.player.Player;

/**
 * Abstract class for a Card object which can be given to a player and activated to perform some type of game
 * changing action such as payment or movement
 *
 * Dependent on Player class
 *
 * @author leahschwartz
 */
public abstract class Card {

    private int myCardNumber;
    private String myDeckType;
    private boolean isHoldable;
    private boolean myIsResolved;
    private int[] myConfigurations;


    public Card(int cardNumber, String deckType, int[] configurations){
        myCardNumber = cardNumber;
        myDeckType = deckType;
        myConfigurations = configurations;
        isHoldable = false;
    }

    public boolean isResolved(){
        return myIsResolved;
    }

    public void setIsResolved(boolean resolvedStatus){
        myIsResolved = resolvedStatus;
    }

    public int[] getConfigurations(){
        return myConfigurations;
    }

    public int getNumber(){
        return myCardNumber;
    }


    /**
     * Does the action associated with the card
     */
    public abstract void doAction(Player drawer, Player bank, int lastRoll);

    /**
     * @return a boolean that answers the question, "is this Card holdable by the player?"
     */
    public boolean isHoldable(){
        return isHoldable;
    }

    public String getDeckType(){
        return myDeckType;
    }

}


