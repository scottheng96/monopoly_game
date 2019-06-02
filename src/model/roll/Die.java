package model.roll;

import model.location.Location;

/**
 * Represents the dice in a board game which can be rolled and tell specific information, such as if doubles were rolled
 */
public abstract class Die {

    private int myLastRoll;

    /**
     * Rolls all dice to determine overall amount player should move
     * @param currentLocation location of player since amount they need to move may depend on where they are
     * @return amount of roll
     */
    public abstract int roll(Location currentLocation);

    public abstract int roll();

    public abstract boolean isConsideredForDoubles();

    public int getLastRoll(){
        return myLastRoll;
    }

    protected void setMyLastRoll(int roll){
        myLastRoll = roll;
    }

}
