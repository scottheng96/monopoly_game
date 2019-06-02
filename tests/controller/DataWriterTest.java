package controller;

import controller.exceptions.InvalidDataFileValueException;
import model.location.Location;
import model.location.TeleportSpace;
import model.player.HumanPlayer;
import model.player.Player;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DataWriterTest {

    private static Player player1;
    private static Player player2;
    private static Location location1;
    private static Location location2;
    private static BackEndDataReader myReader;
    private static DataWriter myWriter;

    @BeforeAll
    public static void setUp() {
        player1 = new HumanPlayer("TEST1");
        player1.setStartingLocationNumber(5);
        player1.increaseMoney(555);
        player1.setStartingLocationNumber(0);

        location1 = new TeleportSpace("test1", 5, 6);

        myWriter = new DataWriter("./resources/testing"); //TODO: use .properties files instead of hard coding
        myWriter.makePlayerFile(player1);
        myWriter.makeLocationFile(location1);
        //myWriter.makeRules();

        myReader = new BackEndDataReader();
        try {
            myReader.readAllGameFiles("./resources/testing/" + myWriter.getTimestamp() + "/");
        } catch (InvalidDataFileValueException e) {
            e.printStackTrace();
        }
        for (Player i : myReader.getPlayers()) {
            System.out.println(i.getUniqueID());
            System.out.println(i.getName());
        }
        player2 = myReader.getPlayers().get(0);
        location2 = myReader.getLocations().get(0);

        //System.out.println(player2.getName());
    }

    @Test
    public void testPlayer() {
        assertTrue(player1.equals(player2));
    }

    @Test
    public void testLocation() {
        location1.equals(location2);
    }
}
