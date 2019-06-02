package model.location.propertySpace;

import model.player.Player;
import java.util.Map;

/**
 * Represents Railroads on a Monopoly board. These PropertySpaces have rents updated based on how many other
 * Railroads owner has
 *
 * Dependent on abstract PropertySpace and Player classes
 *
 * @author leahschwartz, jordanshapiro
 */
public class RailRoad extends PropertySpace {

    private int numOwned;

    public RailRoad(String name, int locationNumber, String group, int propertiesInGroup, int cost, int buildingCost,
                    Map<Integer, Integer> rentByHouses){
        super(name, locationNumber, group, propertiesInGroup, cost, buildingCost, rentByHouses);
        numOwned = 1;
    }

    /**
     * updates the cost of rent based on how many RailRoads are owned by a single player
     */
    public void updateRent() {
        Player myOwner = getOwner();
        int num = myOwner.getMyPropertiesOfGroup(getGroup()).size();
        int diff = num - numOwned;
        numOwned = num;

        if (diff > 0) {
            System.out.println("railroad rent = " + (getRent() * (int) (Math.pow(2, diff))));
            setRent(getRent() * (int) (Math.pow(2, diff)));
        } else if (diff < 0) {
            float multiplier = (float) (1.0 / (Math.pow(2, diff * -1)));
            setRent(Math.round(getRent() * multiplier));
        }

        if (getNumberOfHouses() > 0) {
            setRent(getRentByHouses(getNumberOfHouses()));
        }
    }

}
