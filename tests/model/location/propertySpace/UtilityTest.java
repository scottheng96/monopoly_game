package model.location.propertySpace;

import jdk.jshell.execution.Util;
import model.Board;
import model.location.Location;
import model.location.propertySpace.Utility;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilityTest {

    private Player player;
    private Utility u1;
    private Utility u2;
    private Utility u3;
    private Board myBoard;
    private int roll;

    private static final String GROUP = "Utility";

    @BeforeEach
    public void setUp() {
        player = new HumanPlayer("Test", "Test");
        u1 = new Utility("TestUtility0", 0, GROUP, 3, 0, 0,
                Map.ofEntries(
                        Map.entry(0,0)
                ));
        u2 = new Utility("TestUtility1", 1, GROUP, 3, 0, 0,
                Map.ofEntries(
                        Map.entry(0,0)
                ));
        u3 = new Utility("TestUtility2", 2, GROUP, 3, 0, 0,
                Map.ofEntries(
                        Map.entry(0,0),
                        Map.entry(1, 4)
                ));
        List<Location> list = new ArrayList<>(List.of(u1, u2, u3));
        myBoard = new Board(list);
        u1.changeOwner(player);
    }

    @Test
    public void getRent_rentUpdatesWithNewRollNumber(){
        u1.activateSpaceAction(player, new Bank(""), 7);
        int rent = u1.getRent();
        assertEquals(49, rent);
    }

    @Test
    public void getRent_testOneProperty() {
        int rent = u1.getRent();
        assertEquals(7, rent);
    }

    @Test
    public void getRent_TwoProperties() {
        u2.changeOwner(player);
        int rent1 = u1.getRent();
        int rent2 = u2.getRent();

        assertEquals(10, rent1);
        assertEquals(rent1, rent2);
    }

    @Test
    public void getRent_ThreeProperties() {
        u2.changeOwner(player);
        u3.changeOwner(player);
        int rent1 = u1.getRent();
        int rent2 = u2.getRent();
        int rent3 = u3.getRent();

        assertEquals(13, rent1);
        assertEquals(rent1, rent2);
        assertEquals(rent1, rent3);
    }

    @Test
    public void addHouse_rentIncreases(){
        Player p2 = new HumanPlayer("", "");
        u3.changeOwner(p2);
        u3.addHouse();
        assertEquals(4, u3.getRent());
    }


}
