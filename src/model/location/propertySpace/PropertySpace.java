package model.location.propertySpace;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Transaction;
import model.location.Location;
import model.player.Player;
import model.player.PlayerState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents PropertySpaces on a Monopoly board and all their functionality. These PropertySpaces include
 * RealEstate, RailRoads, and Utilities. PropertySpaces can be owned, traded, mortgaged, and improved depending
 * on initial configurations.
 *
 * Dependent on Abstract Location and Player classes
 *
 * @author leahschwartz, jordanshapiro
 */
public abstract class PropertySpace extends Location {

    private String myGroup;
    private int myCost;
    private int myBuildingCost;
    private Map<Integer, Integer> myRentByHouses;
    private boolean myCanHousesBeAdded;
    private int myNumberPropertiesInGroup;

    private SimpleObjectProperty<Player> myOwner;
    private SimpleIntegerProperty myNumberOfHouses;
    private SimpleBooleanProperty isMortgaged;
    private SimpleIntegerProperty myRent;

    public PropertySpace(String name, int locationNumber, String group, int propertiesInGroup, int cost,
                         int buildingCost, Map<Integer, Integer> rentByHouses){
        super(name, locationNumber);
        myGroup = group;
        myCost = cost;
        myNumberPropertiesInGroup = propertiesInGroup;
        myBuildingCost = buildingCost;
        myRentByHouses = rentByHouses;
        setIsResolved(false);
        myCanHousesBeAdded = isBuildableType();
        myRent = new SimpleIntegerProperty(myRentByHouses.get(0));
        myNumberOfHouses = new SimpleIntegerProperty(0);
        myOwner = new SimpleObjectProperty<>();
        isMortgaged = new SimpleBooleanProperty(false);
    }

    /**
     * Resolves the game actions that occur when a player lands on the space
     * @param player player landing on space
     */
    public void activateSpaceAction(Player player, Player bank, int lastRoll) {
      //  System.out.println("activating space + " + getName());
        if (!myOwner.getValue().equals(player)){
            if (requiresRentPayment(bank)) {
           //     System.out.println("Player must pay rent!");
                player.addTransaction(new Transaction(player, myOwner.getValue(), -1 * getRent()));
            } else if (player.hasEnoughMoney(myCost)) {
                if (player.getCurrentState().equals(PlayerState.BUYING)) {
               //     System.out.println("Buying Property: " + getName());
                    changeOwner(player);
                    player.withdrawMoney(getCost());
                   // System.out.println("Owner of " + getName() + " is " + getOwner().getName());
                    setIsResolved(true);
                } else {
                    player.changeState(PlayerState.BUYING);
                }
            }
        }
        finishLanding(player);
    }

    /**
     * Updates the cost of rent for the PropertySpace. This is a function of buildings, and how many group members a single player owns
     */
    public abstract void updateRent();


    public List<Property> getAllObservableProperties(){
        return new ArrayList<>(Arrays.asList(isMortgaged, myNumberOfHouses, myOwner, myRent));
    }

    public SimpleIntegerProperty getNumberOfHousesPropertyObject() {
        return myNumberOfHouses;
    }

    public SimpleBooleanProperty getMortgagedPropertyObject() {
        return isMortgaged;
    }

    public SimpleObjectProperty<Player> getOwnerPropertyObject() {
        return myOwner;
    }

    public SimpleIntegerProperty getRentPropertyObject() {
        return myRent;
    }

    public String getGroup(){
        return myGroup;
    }

    private boolean requiresRentPayment(Player bank){
        return !isOwned(bank) && !isMortgaged.get();
    }

    protected void setRent(int rent) {
        myRent.setValue(rent);
    }

    /**
     * @return an int that represents the money value of rent for landing on this property once it is owned
     */
    public int getRent() {
        updateRent();
        return myRent.getValue();
    }

    public int getNumberPropertiesInGroup(){
        return myNumberPropertiesInGroup;
    }

    public boolean ownerIsEligibleToBuildMore(){
        return myOwner.getValue().hasEnoughMoney(getBuildingCost())
                && myNumberOfHouses.getValue() < getTopNumberOfHouses() && isPartOfMonopoly();
    }


    public void setHouseCanBeAdded(boolean houseCanBeAdded) {
        myCanHousesBeAdded = houseCanBeAdded;
    }

    public boolean canHousesBeAdded() {
        return myCanHousesBeAdded && ownerIsEligibleToBuildMore() && !isMortgaged();
    }

    /**
     * Changes the mortgaged status of the PropertySpace object to mortgaged
     */
    public void mortgage() {
        isMortgaged.setValue(true);
        myOwner.getValue().increaseMoney(myCost / 2);
    }

    public int getUnmortgageCost(){
        return (int) ((myCost / 2) * 1.1);
    }

    /**
     * Changes the mortgaged status of the PropertySpace object to unmortgaged
     */
    public void unmortgage() {
        isMortgaged.setValue(false);
        myOwner.getValue().withdrawMoney(getUnmortgageCost());
    }

        /**
         * Used for determining if this property can ever be built on
         * @return a boolean answering the question, "can this PropertySpace ever be built on"
         */
    public boolean isBuildableType() {
        return myRentByHouses.size() >= 1;
    }

    /**
     * Adds a house to the PropertySpace
     */
    public void addHouse() {
            myNumberOfHouses.setValue(myNumberOfHouses.get() + 1);
            myOwner.getValue().withdrawMoney(myBuildingCost);
    }

    /**
     * Removes a house from the property
     */
    public void sellHouse() {
        myNumberOfHouses.setValue(myNumberOfHouses.get() - 1);
        myOwner.getValue().increaseMoney(myBuildingCost / 2);
    }

    /**
     * Gets how much a property costs
     * @return int cost of property
     */
    public int getCost() {
        return myCost;
    }

    /**
     * Changes owner of PropertySpace
     * NOTE: this method should be used whenever a player is gaining or losing a property, it is for external API use
     * @param newOwner new owner
     */
    public void changeOwner(Player newOwner) {
        if (myOwner.getValue() != null) {
            myOwner.getValue().removeProperty(this);
        }
        newOwner.addProperty(this);
        myOwner.setValue(newOwner);
    }

    public Player getOwner() {
        return myOwner.get();
    }

    private boolean isOwned(Player bank){
        return myOwner.getValue().equals(bank);
    }

    public boolean isMortgaged() {
        return isMortgaged.getValue();
    }

    public int getNumberOfHouses() {
        return myNumberOfHouses.get();
    }

    public int getRentByHouses(int numberOfHouses) {
        return myRentByHouses.get(numberOfHouses);
    }

    /**
     * Gets the net worth of the PropertySpace, based on mortgage value as well as value of buildings on it
     * @return net worth of PropertySpace
     */
    public int getNetWorth(){
        int totalWorth = myNumberOfHouses.get() * (myBuildingCost / 2);
        if (!isMortgaged()){
            totalWorth += (myCost / 2);
        }
        return totalWorth;
    }

    /**
     * @return an int representing the cost of building a new house/hotel
     */
    public int getBuildingCost() {
        return myBuildingCost;
    }

    /**
     * Determines how many houses are allowed to exist on Property at maximum
     * @return number of houses allowed
     */
    public int getTopNumberOfHouses() {
        return myRentByHouses.size() - 1;
    }

    /**
     * Checks if property is part of a Monopoly based on owner holdings
     * @return boolean
     */
    public boolean isPartOfMonopoly() {
        return myOwner.getValue().getMyPropertiesOfGroup(myGroup).size() == myNumberPropertiesInGroup;
    }
}
