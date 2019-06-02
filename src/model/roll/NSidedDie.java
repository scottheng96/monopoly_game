package model.roll;

import model.location.Location;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This abstract class is used for all N-Sided die
 */
public class NSidedDie extends Die {

    private int numberOfSides;
    private static final boolean DOUBLES = true;

    public NSidedDie(int sides) {
        numberOfSides = sides;
    }

    /**
     * Rolls all n-sided dice to determine overall roll
     * @param currentLocation location of player since amount they need to move may depend on where they are
     * @return amount of roll
     */
    public int roll(Location currentLocation){

      return ThreadLocalRandom.current().nextInt(1, numberOfSides + 1);
    }



    public int roll() {
       int roll = ThreadLocalRandom.current().nextInt(1, numberOfSides + 1);
        setMyLastRoll(roll);
        return roll;
    }

    /**
     * Tells the caller if this die is considered for doubles
     * @return a boolean telling the caller if this die should be considered for doubles
     */
    public boolean isConsideredForDoubles() {
        return DOUBLES;
    }

}
