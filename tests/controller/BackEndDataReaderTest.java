
package controller;

import controller.exceptions.InvalidDataFileValueException;
import model.location.*;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class BackEndDataReaderTest {

    private BackEndDataReader myReader;
    private List<Location> myLocations;
    private List<Player> myPlayers;

    @BeforeEach
    public void setUp() {
        myReader = new BackEndDataReader();
        try {
            myReader.readAllGameFiles("./resources/testing/data_reader/");
        } catch (InvalidDataFileValueException e) {
            e.printStackTrace();
        }
        myLocations = myReader.getLocations();
        myPlayers = myReader.getPlayers();
    }

    @Test
    public void testLocations() {
        Set<String> myNames = new HashSet<>((Arrays.asList("ATLANTIC AVENUE", "GO", "ST. JAMES PLACE")));
        for (Location l : myLocations) {
            assertTrue(myNames.contains(l.getName()));
        }
        assertEquals(myNames.size(), myLocations.size());
    }

    @Test
    public void testPlayers() {
        Set<String> myNames = new HashSet<>();
        myNames.addAll(Arrays.asList(new String[]{"LEAH"}));
        for (Player player : myPlayers) {
         //   System.out.println(player.getName());
            assertTrue(myNames.contains(player.getName()));
        }
        assertEquals(myNames.size(), myPlayers.size());
    }

    @Test
    public void testPropertiesAreAssignedToBank(){
        assertEquals(2, myReader.getBank().getProperties().size());
    }

}

