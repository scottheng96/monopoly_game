package model.factory;

import controller.exceptions.InvalidDataFileValueException;
import model.location.*;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Represents a factory design to create new Locations, including subclasses of PropertySpaces
 *
 * Dependent on Player, Location and subclasses
 *
 */
public class LocationFactory {

    private static final String NAME_KEY = "NAME";
    private static final String LOCATION_KEY = "LOCATION_NUMBER";
    private static final String TYPE_KEY = "TYPE";
    private static final String PROPERTY_TYPE_KEY = "PROPERTY_TYPE";
    private static final String COST_KEY = "COST";
    private static final String MORTGAGE_KEY = "ISMORTGAGED";
    private static final String HOUSES_KEY = "HOUSES";
    private static final String GROUP_KEY = "GROUP";
    private static final String RENT_KEY = "RENT";
    private static final String BUILDING_COST_KEY = "BUILDING_COST";
    private static final String DESTINATION_KEY = "DESTINATION";
    private static final String DECK_TYPE_KEY = "DECK";
    private static final String AMOUNT_MONEY_GIVEN_KEY = "AMOUNT_MONEY_GIVEN";
    private static final String NUMBER_IN_GROUP_KEY = "NUMBER_PROPERTIES_IN_GROUP";
    private static final String TELEPORT_SPACE = "TeleportSpace";
    private static final String MONEY_SPACE = "MoneySpace";
    private static final String CARD_SPACE = "CardSpace";
    private static final String PROPERTY_SPACE = "propertySpace";
    private static final String JAIL_SPACE = "JailSpace";
    private static final String COMMON_SPACE = "CommonSpace";
    private static final String PROPERTY_SPACE_PATH = "model.location.propertySpace.";
    private static final String SEPARATOR = ",";

    /**
     * Does not have error checking, needs to be added
     * @param properties Properties object with information about the location
     * @param bank Player object that is serving as the bank
     * @return Location based on configurations
     */
    public Location create(Properties properties, Player bank) throws InvalidDataFileValueException {
        try {
            String name = properties.getProperty(NAME_KEY);
            int locationNumber = Integer.parseInt(properties.getProperty(LOCATION_KEY));
            String type = properties.getProperty(TYPE_KEY);
            if (type.equalsIgnoreCase(TELEPORT_SPACE)) {
                return new TeleportSpace(name, locationNumber, Integer.parseInt(properties.getProperty(DESTINATION_KEY)));
            }
            if (type.equalsIgnoreCase(CARD_SPACE)) {
                return new CardSpace(name, locationNumber, properties.getProperty(DECK_TYPE_KEY)); //TODO add decktype
            }
            if (type.equalsIgnoreCase(MONEY_SPACE)) {
                return new MoneySpace(name, locationNumber, Integer.parseInt(properties.getProperty(AMOUNT_MONEY_GIVEN_KEY)));
            }
            if (type.equalsIgnoreCase(JAIL_SPACE)) {
                return new JailSpace(name, locationNumber);
            }
            if (type.equalsIgnoreCase(COMMON_SPACE)){
                return new CommonSpace(name, locationNumber);
            }
            if (type.equalsIgnoreCase(PROPERTY_SPACE)) {
                return makeProperty(properties, name, locationNumber, bank);
            }
        } catch (Exception e) {
            throw new InvalidDataFileValueException("INVALID PROPERTY DATA FILE");
        }
        return null;
    }


    private Map<Integer,Integer> constructRentMap(String[] rents){
        Map<Integer,Integer> rentByBuilding = new HashMap<>();
        for (int numberBuildings = 0; numberBuildings < rents.length; numberBuildings++){
            rentByBuilding.put(numberBuildings, Integer.parseInt(rents[numberBuildings]));
        }
        return rentByBuilding;
    }

    private PropertySpace makeProperty(Properties properties, String name, int locationNumber, Player bank) throws InvalidDataFileValueException {
        boolean isMortgaged = Boolean.valueOf(properties.getProperty(MORTGAGE_KEY));
        int cost = Integer.parseInt(properties.getProperty(COST_KEY));
        String group = properties.getProperty(GROUP_KEY);
        int numberPropertiesInGroup = Integer.parseInt(properties.getProperty(NUMBER_IN_GROUP_KEY));
        Map<Integer, Integer> rentByBuilding = constructRentMap(properties.getProperty(RENT_KEY).split(SEPARATOR));
        int buildingCost = Integer.parseInt(properties.getProperty(BUILDING_COST_KEY));
        String propertyType = properties.getProperty(PROPERTY_TYPE_KEY);
        Class propertyClass;
        try {
            propertyClass = Class.forName(PROPERTY_SPACE_PATH + propertyType);
            Constructor constructor = propertyClass.getConstructor(String.class, int.class, String.class, int.class,
                    int.class, int.class, Map.class);
            PropertySpace property = (PropertySpace) constructor.newInstance(name, locationNumber, group, numberPropertiesInGroup, cost,
                    buildingCost, rentByBuilding);
            property.changeOwner(bank);
            if (isMortgaged) {
                property.mortgage();
            }
            for (int numberHouses = 0; numberHouses < Integer.parseInt(properties.getProperty(HOUSES_KEY)); numberHouses++){
                property.addHouse();
            }
            return property;
        } catch (Exception e) {
            throw new InvalidDataFileValueException("INVALID PROPERTY DATA FILE");
        }
    }
}
