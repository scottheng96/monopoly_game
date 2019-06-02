package model.location;

import model.player.Player;
import model.player.PlayerState;

/**
 * Abstract class for representing a location on a Monopoly board. Locations know their neighbors and are
 * comparable by order.
 *
 * Dependent on abstract Location, Player classes
 *
 * @author leahschwartz, jordanshapiro
 */
public abstract class Location implements Comparable{

    private boolean myIsResolved;
    private int myLocationNumber;
    private Location myNext;
    private Location myPrevious;
    private String myName;

    public Location(String name, int locationNumber){
        myName = name.toUpperCase();
        myLocationNumber = locationNumber;
    }

    /**
     * Does the action that a space demands
     * @param player a Player object that landed on the space
     * @param bank
     * @param lastRoll
     */
    public abstract void activateSpaceAction(Player player, Player bank, int lastRoll);

    protected void finishLanding(Player player){
        if (!player.getCurrentState().equals(PlayerState.BUYING) && !player.getCurrentState().equals(PlayerState.DEBT)){
            setIsResolved(true);
        }
    }

    /**
     * Gets the location ahead of the current location
     * @return next location
     */
    public Location getNextLocation() {
        return myNext;
    }

    /**
     * Gets a location that is a set number of spaces in the forward direction
     * @param numberToGoForward
     * @return a Location object that is a specific number of steps from this Location
     */
    public Location getForwardLocation(int numberToGoForward){
        Location next = this;
        for (int number = 0; number < numberToGoForward; number++){
           next = next.getNextLocation();
        }
        return next;
    }

    /**
     * Gets the location behind the current location
     * @return previous location
     */
    public Location getPreviousLocation() {
        return myPrevious;
    }

    public String getName() {
        return myName;
    }

    public void setNextLocation(Location myNext) {
        this.myNext = myNext;
    }

    public void setPreviousLocation(Location myPrevious) {
        this.myPrevious = myPrevious;
    }

    public void setLocationNumber(int myLocationNumber) {
        this.myLocationNumber = myLocationNumber;
    }

    public int getLocationNumber() {return myLocationNumber;}

    public void setName(String name) {
        myName = name;
    }

    public void resetForNextTurn(){
        setIsResolved(false);
    }

    public boolean isResolved(){
        return myIsResolved;
    }

    public void setIsResolved(boolean resolvedStatus){
        myIsResolved = resolvedStatus;
    }

    /**
     * Checks if two Locations are the same
     * @return boolean
     */
    @Override
    public boolean equals(Object other) {
        if (other instanceof Location) {
            Location otherLocation = ((Location) other);
                return (myLocationNumber == otherLocation.getLocationNumber()
                        && myName.equals(otherLocation.getName()));
        }
        return false;
    }

    @Override
    public int compareTo(Object o) {
        Location other = (Location) o;
        return getLocationNumber() - other.getLocationNumber();
    }

    public boolean wasLandedOn(Player player, int totalRoll){
        Location previous = player.getPreviousLocation();
        return  (previous.getForwardLocation(totalRoll).equals(this));
    }
}
