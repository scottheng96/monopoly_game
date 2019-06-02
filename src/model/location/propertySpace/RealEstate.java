package model.location.propertySpace;

import java.util.Map;

/**
 * Represents RealEstate spaces on a Monopoly board. These PropertySpaces have rents updated based solely
 * on number of houses
 *
 * Dependent on abstract PropertySpace and Player classes
 *
 * @author leahschwartz, jordanshapiro
 */
public class RealEstate extends PropertySpace {

    public RealEstate(String name, int locationNumber, String group, int propertiesInGroup, int cost, int buildingCost,
                      Map<Integer, Integer> rentByHouses){
        super(name, locationNumber, group, propertiesInGroup, cost, buildingCost, rentByHouses);
    }

    /**
     * Updates the cost of rent for a RealEstate based on number of houses built on the property
     */
    public void updateRent() {
        int houses = getNumberOfHouses();
        if (houses <= getTopNumberOfHouses()) {
            setRent(getRentByHouses(houses));
        }
    }

}
