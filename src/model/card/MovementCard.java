package model.card;

import model.location.Location;
import model.player.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a Card object which can be given to a player and upon activation, moves a player somewhere on the board
 *
 * Dependent on abstract Card, Player classes
 *
 * @author leahschwartz
 */
public class MovementCard extends Card{

    private Map<Integer,Integer> myDistances;

    public MovementCard(int cardNumber, String deckType, int[] locationConfigurations){
        super(cardNumber, deckType, locationConfigurations);
        myDistances = new HashMap<>();
    }

    @Override
    public void doAction(Player drawer, Player bank, int lastRoll) {
        calculateDistances(drawer);
        movePlayer(drawer, getNearest());
        drawer.getCurrentLocation().activateSpaceAction(drawer, bank, lastRoll);
    }

    private void movePlayer(Player drawer, int locationToMoveTo){
        Location location =  drawer.getCurrentLocation();
        while (location.getLocationNumber() != locationToMoveTo){
            location = location.getNextLocation();
        }
        drawer.moveTo(location);
    }


    private void calculateDistances(Player drawer){
        myDistances.clear();
        for (Integer option : getConfigurations()){
            int distance = 0;
            Location location = drawer.getCurrentLocation().getNextLocation();
            while (location.getLocationNumber() != option){
                distance++;
                location = location.getNextLocation();
            }
            myDistances.put(option, distance);
        }
    }

    private int getNearest(){
        return Collections.min(myDistances.entrySet(), Comparator.comparingInt(Map.Entry::getValue)).getKey(); // key with min value
    }

}
