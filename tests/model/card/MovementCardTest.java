package model.card;

import model.Board;
import model.location.Location;
import model.location.MoneySpace;
import model.location.propertySpace.PropertySpace;
import model.location.propertySpace.RealEstate;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MovementCardTest {

    private Card myCard = new MovementCard(0, "nextLocation", new int[]{1});
    private Player player1 = new HumanPlayer("Leah");
    private Bank bank = new Bank("BANK");
    private List<Location> myLocations;
    private Board myBoard;
    private

    @BeforeEach
    void setUp(){
        myLocations = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            myLocations.add(new MoneySpace("yellow1", i,20));
       }
        myBoard = new Board(myLocations);
    }

    @Test
    void doAction_CardMovesPlayer() {
        player1.moveTo(myLocations.get(2));
        myCard.doAction(player1, bank, 4);
        assertEquals(myLocations.get(1), player1.getCurrentLocation());
    }

    @Test
    void doAction_CardActivatesNewSpace() {
        player1.moveTo(myLocations.get(2));
        assertEquals(0, player1.getTransactions().size());
        myCard.doAction(player1, bank, 4);
        assertEquals(1, player1.getTransactions().size());
    }


    @Test
    void doAction_PlayerMovesToClosetSpot() {
        myCard = new MovementCard(0, "nextLocation", new int[]{1, 3});
        player1.moveTo(myLocations.get(0));
        myCard.doAction(player1, bank, 5);
        assertEquals(myLocations.get(1), player1.getCurrentLocation());
    }

    @Test
    void doAction_PlayerMovesPastCurrentSpotToNextClosest() {
        myCard = new MovementCard(0, "nextLocation", new int[]{1, 3});
        player1.moveTo(myLocations.get(1));
        myCard.doAction(player1, bank, 5);
        assertEquals(myLocations.get(3), player1.getCurrentLocation());
    }

    @Test
    void doAction_PlayerMovesAroundBoard() {
        myCard = new MovementCard(0, "nextLocation", new int[]{1, 3});
        player1.moveTo(myLocations.get(4));
        myCard.doAction(player1, bank, 5);
        assertEquals(myLocations.get(1), player1.getCurrentLocation());
    }

}