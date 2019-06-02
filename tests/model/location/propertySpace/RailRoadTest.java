package model.location.propertySpace;

import model.Board;
import model.location.Location;
import model.location.propertySpace.RailRoad;
import model.player.HumanPlayer;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.image.RasterOp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RailRoadTest {

    private Player p;
    private RailRoad r1;
    private RailRoad r2;
    private RailRoad r3;
    private Board myBoard;
    private int roll;

    private static final String GROUP = "RailRoad";

    @BeforeEach
    public void setUp() {
        p = new HumanPlayer("Test", "Test");
        r1 = new RailRoad("TestRail0", 0, GROUP, 3, 0, 0,
                Map.ofEntries(
                        Map.entry(0,25)
                ));
        r2 = new RailRoad("TestRail1", 1, GROUP, 3, 0, 0,
                Map.ofEntries(
                        Map.entry(0,25)
                ));
        r3 = new RailRoad("TestRail2", 2, GROUP, 3, 0, 0,
                Map.ofEntries(
                        Map.entry(0,25),
                        Map.entry(1, 400)
                ));
        List<Location> list = new ArrayList<>(List.of(r1, r2, r3));
        myBoard = new Board(list);
        roll = 10;

        r1 = (RailRoad) myBoard.getLocation(0);
        r2 = (RailRoad) myBoard.getLocation(1);
        r3 = (RailRoad) myBoard.getLocation(2);
        r1.changeOwner(p);
        r1.getForwardLocation(roll);
        r2.getForwardLocation(roll);
        r3.getForwardLocation(roll);
    }

    @Test
    public void testOneProperty() {
        int rent = r1.getRent();
        assertEquals(25, rent);
    }

    @Test
    public void testTwoProperties() {
        r2.changeOwner(p);
        int rent1 = r1.getRent();
        int rent2 = r2.getRent();

        assertEquals(50, rent1);
        assertEquals(rent1, rent2);
    }

    @Test
    public void testThreeProperties() {
        r2.changeOwner(p);
        r3.changeOwner(p);
        int rent1 = r1.getRent();
        int rent2 = r2.getRent();
        int rent3 = r3.getRent();

        assertEquals(100, rent1);
        assertEquals(rent1, rent2);
        assertEquals(rent1, rent3);
    }

    @Test
    public void testUpdateDownward() {
        r2.changeOwner(p);
        r3.changeOwner(p);
        int rent1 = r1.getRent();

        assertEquals(100, rent1);

        Player p2 = new HumanPlayer("", "");
        r2.changeOwner(p2);
        r3.changeOwner(p2);
        rent1 = r1.getRent();

        assertEquals(25, rent1);
    }

    @Test
    public void addHouse_rentIncreases(){
        Player p2 = new HumanPlayer("", "");
        r3.changeOwner(p2);
        r3.addHouse();
        assertEquals(400, r3.getRent());
    }

}
