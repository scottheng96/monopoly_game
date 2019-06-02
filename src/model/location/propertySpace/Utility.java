package model.location.propertySpace;

import model.player.Player;
import java.util.Map;

/**
 * Represents Utilities on a Monopoly board. These PropertySpaces have rents updated based on how many other
 * number of utilities owned by owner and on the last roll of the player
 *
 * Dependent on abstract PropertySpace and Player classes
 *
 * @author leahschwartz, jordanshapiro
 */
public class Utility extends PropertySpace {

    private int myMostRecentRoll;

    public Utility(String name, int locationNumber, String group, int propertiesInGroup, int cost, int buildingCost,
                   Map<Integer, Integer> rentByHouses){
        super(name, locationNumber, group, propertiesInGroup, cost, buildingCost, rentByHouses);
        myMostRecentRoll = 1;
    }

    /**
     * Saves player's most recent roll upon landing in order to use it as a rent multiplier
     * @param player player landing on space
     * @param bank
     * @param lastRoll
     */
    @Override
    public void activateSpaceAction(Player player, Player bank, int lastRoll) {
        myMostRecentRoll = lastRoll;
        super.activateSpaceAction(player, bank, lastRoll);
    }


    /**
     * Multiplies rent value with the player's last roll
     * @return
     */
    @Override
    public int getRent() {
        return super.getRent() * myMostRecentRoll;
    }

    public void updateRent() {
       Player owner = getOwner();
       int newRent = rentFunction(owner.getMyPropertiesOfGroup(getGroup()).size());
       setRent(newRent);
       if (getNumberOfHouses() > 0) {
           setRent(getRentByHouses(getNumberOfHouses()));
       }
    }

    private int rentFunction(int n) {
        return 7 + ((n - 1) * 3);
    }

}
