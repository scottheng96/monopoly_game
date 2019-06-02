package model;

import model.Board;
import model.location.Location;
import model.location.MoneySpace;
import model.location.TeleportSpace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    private Board board;
    private static final String GO = "GO";
    private static final String TAX = "TAX";
    private static final String PLACE = "PLACE";
    private static final String gameVersion = "REGULAR_MONOPOLY";
    private Location myGo;
    private Location myTax;
    private Location myPlace;
    private Location flag;
    private ArrayList<Location> locations;

    @BeforeEach
    public void setUp() {
        locations = new ArrayList<>();
        myGo = new MoneySpace(GO, 0, 300);
        myTax = new MoneySpace(TAX,1, -300);
        myPlace = new TeleportSpace(PLACE,2, 5);
        flag = new MoneySpace(TAX,1, -300);
        locations.addAll(Arrays.asList(myGo, myTax, myPlace));
        board = new Board(new ArrayList<>());
    }

    @Test
    public void setUp_BoardGetsSetWithDifferentSpaces(){
        List<String> locationNames = new ArrayList<>(Arrays.asList("BALTIC AVENUE", "FREE PARKING", "MEDITERRANEAN AVENUE"));
        List<Location> locations = new ArrayList<>();
        for (int i = 0; i < locationNames.size(); i++){
            locations.add(new MoneySpace(GO, 0, 300));
        }
        board = new Board(locations);
        for (int i = 0; i < locationNames.size(); i++) {
            assertNotEquals(locationNames.get(i), board.getLocation(i).getName());
        }
    }

    @Test
    public void getSizeTest() {
        assertEquals(0, board.getSize());
        board.insertAtStart(myGo);
        assertEquals(1, board.getSize());
        board.deleteAtPosition(0);
        assertEquals(0, board.getSize());
    }

    @Test
    public void insertAtStartTest() {
        board.insertAtStart(myGo);
        assertEquals(1, board.getSize());
        assertEquals(myGo, myGo.getNextLocation());
        assertEquals(myGo, myGo.getPreviousLocation());
        board.insertAtStart(myTax);
        assertEquals(2, board.getSize());
        assertEquals(myGo, myTax.getNextLocation());
        assertEquals(myTax, myGo.getPreviousLocation());
        assertEquals(myTax, myGo.getNextLocation());
        assertEquals(myGo, myTax.getPreviousLocation());
        assertEquals(1, myGo.getLocationNumber());
        assertEquals(0, myTax.getLocationNumber());
    }

    @Test
    public void insertAtEndTest() {
        board.insertAtEnd(myGo);
        assertEquals(1, board.getSize());
        assertEquals(myGo, myGo.getNextLocation());
        assertEquals(myGo, myGo.getPreviousLocation());
        board.insertAtEnd(myTax);
        assertEquals(2, board.getSize());
        assertEquals(myTax, myGo.getNextLocation());
        assertEquals(myGo, myTax.getPreviousLocation());
        assertEquals(myGo, myTax.getNextLocation());
        assertEquals(myTax, myGo.getNextLocation());
        assertEquals(0, myGo.getLocationNumber());
        assertEquals(1, myTax.getLocationNumber());
    }

    @Test
    public void insertAtPositionTest() {
        for (int i = 0; i < 5; i++) {
            Location commonGo = new MoneySpace(GO,0, 200);
            board.insertAtStart(commonGo);
        }
        board.insertAtPosition(myGo, 3);
        assertEquals(6, board.getSize());
        assertEquals(2, myGo.getPreviousLocation().getLocationNumber());
        assertEquals(3, myGo.getLocationNumber());
        assertEquals(4, myGo.getNextLocation().getLocationNumber());
        assertEquals(5, myGo.getNextLocation().getNextLocation().getLocationNumber());
        assertEquals(myGo, myGo.getPreviousLocation().getNextLocation());
        assertEquals(myGo, myGo.getNextLocation().getPreviousLocation());
        board.insertAtPosition(myTax, 0);
        assertEquals(7, board.getSize());
        assertEquals(0, myTax.getLocationNumber());
        assertEquals(1, myTax.getNextLocation().getLocationNumber());
        assertEquals(myTax, myTax.getPreviousLocation().getNextLocation());
        assertEquals(myTax, myTax.getNextLocation().getPreviousLocation());
    }

    @Test
    public void deleteAtPositionTest() {
        for (int i = 0; i < 5; i++) {
            board.insertAtStart(new MoneySpace(GO, 0, 300));
        }
        board.insertAtPosition(flag, 3);
        board.deleteAtPosition(3);
        assertNonExistent(board.getSize(), flag);
        board.insertAtStart(flag);
        board.deleteAtPosition(0);
        assertNonExistent(board.getSize(), flag);
        board.insertAtEnd(flag);
        board.deleteAtPosition(board.getSize() - 1);
        assertNonExistent(board.getSize(), flag);
    }

    private void assertNonExistent(int size, Location flagGo) {
        for (int i = 0; i < size; i++) {
            assertNotEquals(board.getLocation(i), flagGo);
        }
    }

    @Test
    public void getLocationTest() {
        board = new Board(locations);
        board.insertAtPosition(flag, 0);
        assertEquals(board.getLocation(0), flag);
        assertEquals(board.getLocation(1), flag.getNextLocation());
        assertEquals(board.getLocation(1), myGo);
        assertEquals(board.getLocation(2), flag.getNextLocation().getNextLocation());
        assertEquals(board.getLocation(2), myTax);
        assertEquals(board.getLocation(board.getSize() - 1), flag.getPreviousLocation());
        assertEquals(board.getLocation(3), myPlace);
    }
}
