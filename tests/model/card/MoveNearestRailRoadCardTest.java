package model.card;

import model.Board;
import model.Transaction;
import model.location.Location;
import model.location.MoneySpace;
import model.location.propertySpace.PropertySpace;
import model.location.propertySpace.RailRoad;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import model.player.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MoveNearestRailRoadCardTest {

    private Card myCard = new MoveNearestRailRoadCard(0, "nextLocation", new int[]{3, 4});
    private Player player1;
    private Bank bank = new Bank("BANK");
    private List<Location> myLocations = new ArrayList<>();
    private Board myBoard;
    private Map<Integer,Integer> rents = new HashMap<>();
    private PropertySpace railRoad1;
    private PropertySpace railRoad2;

    @BeforeEach
    void setUp(){
        for (int i = 0; i < 3; i++){
            myLocations.add(new MoneySpace("", i, 50));
        }
        player1 = new HumanPlayer("Leah");
        rents.put(0,100);
        railRoad1 = new RailRoad("", 3, "R", 2, 100, 10, rents);
        railRoad2 = new RailRoad("", 4, "R", 2, 100, 10, rents);
        myLocations.addAll(Arrays.asList(railRoad1, railRoad2));
        myBoard = new Board(myLocations);
        railRoad1.changeOwner(bank);
        railRoad2.changeOwner(bank);
    }

    @Test
    void doAction_PlayerPaysTwoTimesRent() {
        player1.increaseMoney(1000);
        railRoad2.changeOwner(new HumanPlayer("Trudy"));
        player1.moveTo(railRoad1);
        myCard.doAction(player1, bank, 3);
        assertEquals(1, player1.getTransactions().size());
        for (Transaction t : player1.getTransactions()){
            t.doTransaction();
        }
        assertEquals(800, player1.getMoneyBalance());
    }

    @Test
    void doAction_PlayerCanBuyRailRoad() {
        player1.increaseMoney(1000);
        player1.moveTo(myLocations.get(1));
        myCard.doAction(player1, bank, 3);
        assertEquals(0, player1.getTransactions().size());
        assertEquals(PlayerState.BUYING, player1.getCurrentState());
    }
}