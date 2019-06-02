package model.player;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Transaction;
import model.roll.Roll;
import model.card.Card;
import model.location.Location;
import model.location.propertySpace.PropertySpace;
import java.util.*;

/**
 * This abstract class is used for all game players to encapsulate information about their state, id, location, and
 * properties/cards they have
 *
 * @author leahschwartz
 */
public abstract class Player {

    private String myName;
    private String myUniqueID;
    private Location myPreviousLocation;
    private List<PropertySpace> myProperties;
    private ObservableList<PropertySpace> myObservableProperties;
    private List<Card> myCards;
    private ObservableList<Card> myObservableCards;
    private int myStartingLocationNumber;
    private boolean isWinner;
    private SimpleObjectProperty<PlayerState> myStateProperty = new SimpleObjectProperty<>();
    private SimpleObjectProperty<Location> myLocationProperty = new SimpleObjectProperty<>();
    private SimpleIntegerProperty myMoneyProperty;
    private List<Transaction> myTransactions;
    private ObservableList<Transaction> myObservableTransactions;
    private Map<String, List<Card>> myCardTypeToCards = new HashMap<>();
    private int myImprisonedCountdown = 0;
    private int myBid;

    public Player(String name, String uniqueID) {
        this(name);
        myUniqueID = uniqueID;
    }

    public Player(String name){
        myName = name;
        myUniqueID = UUID.randomUUID().toString();
        myMoneyProperty = new SimpleIntegerProperty(0);
        myProperties = new ArrayList<>();
        myObservableProperties = FXCollections.observableList(myProperties);
        myCards = new ArrayList<>();
        myObservableCards = FXCollections.observableList(myCards);
        myTransactions = new ArrayList<>();
        myObservableTransactions = FXCollections.observableList(myTransactions);
        myStartingLocationNumber = 0;
        isWinner = false;
        changeState(PlayerState.NOT_YOUR_TURN);
    }

    public SimpleObjectProperty<PlayerState> getStateProperty(){
        return myStateProperty;
    }

    public SimpleObjectProperty<Location> getLocationProperty(){
        return myLocationProperty;
    }

    public ObservableList<PropertySpace> getPropertiesObservable(){
        return myObservableProperties;
    }

    public SimpleIntegerProperty getMoneyProperty(){
        return myMoneyProperty;
    }

    public ObservableList<Transaction> getTransactionsObservable(){
        return myObservableTransactions;
    }

    /**
     * Gets all the cards that the player is holding that need activating
     * @return a List of cards that need activating
     */
    public List<Card> getCardsNeedingActivation(){
        List<Card> needsActivation = new ArrayList<>();
        for (Card card : myCards){
            if (!card.isHoldable() && !card.isResolved()){
                needsActivation.add(card);
            }
        }
        return needsActivation;
    }

    public String getName(){
        return myName;
    }

    public String getUniqueID(){
        return myUniqueID;
    }

    public int getStartingLocationNumber(){
        return myStartingLocationNumber;
    }

    public void setStartingLocationNumber(int locationNumber){
        myStartingLocationNumber = locationNumber;
    }

    public boolean isWinner(){
        return isWinner;
    }

    public void setIsWinner(boolean wins){
        isWinner = wins;
    }

    public boolean isImprisoned(){
        return myImprisonedCountdown > 0;
    }

    public void decreaseImprisonedCountdown(){
        myImprisonedCountdown--;
    }

    public void setImprisonedCountdown(int timeToServe){
        myImprisonedCountdown = timeToServe;
    }

    protected void setCurrentState(PlayerState newState){
        myStateProperty.set(newState);
        if (newState.equals(PlayerState.ROLLING)){
            myPreviousLocation = myLocationProperty.getValue();
        }
    }

    private void setCurrentLocation(Location newLocation){
        if (myPreviousLocation == null){
            myPreviousLocation = newLocation;
        }
        else {
            myPreviousLocation = myLocationProperty.getValue();
        }
        myLocationProperty.setValue(newLocation);
    }

    /**
     * Sets player's location ahead a certain number of spaces, should only be used if player has already been
     * given a location on the board
     * @param spacesToMove number of spaces to move ahead
     */
    public void move(int spacesToMove){
        for (int space = 0; space < spacesToMove; space++){
            setCurrentLocation(myLocationProperty.getValue().getNextLocation());
        }
    }

    /**
     * Sets player's location to be a specific location
     * @param spaceToMoveTo number of spaces to move ahead
     */
    public void moveTo(Location spaceToMoveTo){
        setCurrentLocation(spaceToMoveTo);
    }


    public PlayerState getCurrentState(){
        return myStateProperty.getValue();
    }

    /**
     * Gets player's current location
     * @return player's location
     */
    public Location getCurrentLocation(){
        return myLocationProperty.getValue();
    }

    public Location getPreviousLocation(){
        return myPreviousLocation;
    }

    /**
     * Gives money to player
     * @param money amount of money to add
     */
    public void increaseMoney(int money){
        myMoneyProperty.setValue(myMoneyProperty.get() + money);
    }

    /**
     * Takes money from player
     * @param money amount of money to withdraw
     */
    public int withdrawMoney(int money){
        myMoneyProperty.setValue(myMoneyProperty.get() - money);
        return money;
    }

    public int withdrawAllMoney(){
        return withdrawMoney(myMoneyProperty.get());
    }

    /**
     * Checks if this Player has enough money to pay some fee
     * @param moneyNeeded an int representing the amount of money this Player needs
     * @return a boolean representing if this player can pay the fee
     */
    public boolean hasEnoughMoney(int moneyNeeded){
        return myMoneyProperty.get() >= moneyNeeded;
    }

    public int getMoneyBalance(){
        return myMoneyProperty.get();
    }

    /**
     * Gives player a new PropertySpace
     * NOTE: This method should only be called as internal API by PropertySpace - otherwise use PropertySpace.changeOwner()
     * @param propertySpace PropertySpace to give to player
     */
    public void addProperty(PropertySpace propertySpace){
        myObservableProperties.add(propertySpace);
    }

    /**
     * Takes a PropertySpace away from a player
     * NOTE: This method should only be called as internal API by PropertySpace - otherwise use PropertySpace.changeOwner()
     * @param propertySpace PropertySpace to take from player
     */
    public void removeProperty(PropertySpace propertySpace){
        myObservableProperties.removeAll(propertySpace);
    }

    public List<PropertySpace> getProperties() {
        return List.copyOf(myProperties);
    }

    public boolean hasProperty(PropertySpace propertySpace){
        return getProperties().contains(propertySpace);
    }

    public List<Card> getCards() {
        return myCards;
    }

    public boolean hasCardsOfType(String cardType){
      //  System.out.println("player wants: " + cardType.toUpperCase());
        for (String key : myCardTypeToCards.keySet()){
        //    System.out.println("i do have: " + key);
        }
        return (myCardTypeToCards.get(cardType.toUpperCase()) != null && !myCardTypeToCards.get(cardType.toUpperCase()).isEmpty());
    }
    public Card getLatestCard() { return myCards.get(myCards.size()-1);}

    public Card removeCardByType(String cardType){
        Card cardToRemove = myCardTypeToCards.get(cardType.toUpperCase()).get(0);
        return removeCard(cardToRemove);
    }

    /**
     * Gives player a new Card
     * @param card Card to give to player
     */
    public void addCard(Card card){
        String deckType = card.getDeckType().toUpperCase();
        if (!myCardTypeToCards.containsKey(deckType)){
            myCardTypeToCards.put(deckType, new LinkedList<>());
        }
        myCardTypeToCards.get(deckType).add(card);
      //  System.out.println("added card of type" + deckType);
        myCards.add(card);
        changeState(PlayerState.DRAWING_CARD);
    }

    /**
     * Takes a Card away from a player
     * @param card Card to take from player
     */
    public Card removeCard(Card card){
        if (myCardTypeToCards.containsKey(card.getDeckType())){
            myCardTypeToCards.get(card.getDeckType()).remove(card);
        }
        myCards.remove(card);
        return card;
    }

    public void addTransaction(Transaction transaction){
        myObservableTransactions.add(transaction);
        changeState(PlayerState.IN_TRANSACTION);
    }

    public List<Transaction> getTransactions(){
        return List.copyOf(myTransactions);
    }

    public void removeTransaction(Transaction transaction){
        myObservableTransactions.remove(transaction);
    }

    /**
     * Sets the current state of a player
     * @param newPlayerState enum representing player's state
     */
    public abstract void changeState(PlayerState newPlayerState);

    public void executeStateChange(Player bank, Roll roll){
       changeState(myStateProperty.getValue().execute(this, bank, roll));
    }

    public int getNetWorth(){
        int netWorth = myMoneyProperty.get();
        for (PropertySpace propertySpace : myObservableProperties){
            netWorth += propertySpace.getNetWorth();
        }
        return netWorth;
    }

    @Override
    public boolean equals(Object other){
        if (!this.getClass().equals(other.getClass())){
            return false;
        }
        Player otherPlayer = (Player) other;
        return myUniqueID.equals(otherPlayer.getUniqueID());
    }

    /**
     * Gets the properties that this player owns from a specific group
     * @param group a String representing a property group
     * @return a Set of PropertySpace objects all of which are owned by this player and are of the specified group
     */
    public Set<PropertySpace> getMyPropertiesOfGroup(String group){
        Set<PropertySpace> myPropertiesOfGroup = new HashSet<>();
        for (PropertySpace property : myObservableProperties){
            if (property.getGroup().equalsIgnoreCase(group)){
                myPropertiesOfGroup.add(property);
            }
        }
        return myPropertiesOfGroup;
    }

    public void setBid(int bid){
        myBid = bid;
    }

    public int getBid(){
        return myBid;
    }

    public Transaction getLatestTransaction(){
        return myTransactions.get(myTransactions.size() - 1);
    }
}