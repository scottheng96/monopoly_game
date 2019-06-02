package model;

import model.location.Location;
import java.util.*;

/**
 * This class is used for the data structure of the board's location
 */
public class Board {

    private List<Location> myLocations;
    private int size;
    private Location start;
    private Location end;

    private int myOfficialGo;
    private int myOfficialJail;
    private int myOfficialFreeParking;

    public Board(List<Location> locations) {
        myLocations = new ArrayList<>();
        setUp(locations);
    }

    /**
     * Sets up all locations based on starting configurations
     */
    public void setUp(List<Location> locations) {
        Collections.sort(locations);
        for (Location location: locations) {
            insertAtEnd(location);
        }
        resetLocationIndex();
        for (Location location: locations) {
           // System.out.println("I have this location in the board: " + location.getName() + " " + location.getLocationNumber());
        }
    }

    public void setOfficialGo(int officialGo) {
        myOfficialGo = officialGo;
    }

    public void setOfficialJail(int officialJail) {
        myOfficialJail = officialJail;
    }

    public void setOfficialFreeParking(int officialFreeParking) {
        myOfficialFreeParking = officialFreeParking;
    }

    public int getOfficialJailIndex() {
        return myOfficialJail;
    }

    public int getOfficialFreeParkingIndex() {
        return myOfficialFreeParking;
    }

    public int getOfficialGoIndex() {
        return myOfficialGo;
    }

    /* Function to get size of list */
    public int getSize() {
        return size;
    }

    private void helper(Location newLocation, boolean insertAtStart) {
        if (start == null) {
            newLocation.setNextLocation(newLocation);
            newLocation.setPreviousLocation(newLocation);
            start = newLocation;
            end = start;
        } else {
            newLocation.setPreviousLocation(end);
            end.setNextLocation(newLocation);
            start.setPreviousLocation(newLocation);
            newLocation.setNextLocation(start);
            if (insertAtStart) {
                start = newLocation;
            } else { //insertAtEnd
                end = newLocation;
            }
        }
        resetLocationIndex();
        size++;
    }

    /**
     * Inserts a new Location at the beginning of the board
     * @param newLocation
     */
    public void insertAtStart(Location newLocation) {
        helper(newLocation, true);
    }

    /**
     * Inserts a new Locaiton at the end of the board
     * @param newLocation
     */
    public void insertAtEnd(Location newLocation) {
        helper(newLocation, false);
    }

    public void insertAtPosition(Location newLocation, int index) {

        if (index == 0) {
            insertAtStart(newLocation);
            return;
        }
        Location pointer2 = start;
        Location pointer = newLocation;
        for (int i = 1; i < size; i++) {
            if (i == index) {
                Location temp = pointer2.getNextLocation();
                pointer2.setNextLocation(pointer);
                pointer.setPreviousLocation(pointer2);
                pointer.setNextLocation(temp);
                temp.setPreviousLocation(pointer);
            }
            pointer2 = pointer2.getNextLocation();
        }
        resetLocationIndex();
        size++;
    }

    public void deleteAtPosition(int index) {
        if (index == 0) {
            if (size == 1) {
                start = null;
                end = null;
                size = 0;
                return;
            }
            start = start.getNextLocation();
            start.setPreviousLocation(end);
            end.setNextLocation(start);
            size--;
            resetLocationIndex();
            return;
        }

        if (index == size - 1) {
            end = end.getPreviousLocation();
            end.setNextLocation(start);
            start.setPreviousLocation(end);
            size--;
        }

        Location pointer = start.getNextLocation();
        for (int i = 1; i < size; i++) {
            if (i == index) {
                Location p = pointer.getPreviousLocation();
                Location n = pointer.getNextLocation();
                p.setNextLocation(n);
                n.setPreviousLocation(p);
                resetLocationIndex();
                size--;
                return;
            }
            pointer = pointer.getNextLocation();
        }
        resetLocationIndex();
    }

    private void resetLocationIndex() {
        myLocations.clear();
        if (size == 0) {
            return;
        }
        else {
            myLocations.add(start);
            start.setLocationNumber(0);
            Location pointer = start.getNextLocation();
            int counter = 1;
            while (pointer != start) {
                pointer.setLocationNumber(counter);
                myLocations.add(pointer);
                pointer = pointer.getNextLocation();
                counter++;
            }
        }
    }

    /**
     * Gives a location based on the index in the board
     * @param index index of location in board
     * @return location at index
     */
    public Location getLocation(int index) {
        return myLocations.get(index);
    }

    public List<Location> getAllLocations() {
        return List.copyOf(myLocations);
    }
}