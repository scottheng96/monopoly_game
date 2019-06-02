package model.card;

import model.Board;
import model.Transaction;
import model.location.MoneySpace;
import model.location.propertySpace.PropertySpace;
import model.location.propertySpace.Utility;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import model.player.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class MoveNearestUtilityCardTest {


    private Card myCard = new MoveNearestUtilityCard(0, "nextLocation", new int[]{1, 2});
    private Player player1;
    private Board myBoard;
    private PropertySpace myUtility1;
    private PropertySpace myUtility2;
    private MoneySpace myMoneySpace;
    private HashMap<Integer, Integer> rents = new HashMap<>();

    @BeforeEach
    void setUp() {
        rents.put(0,100);
        player1 = new HumanPlayer("Leah");
        myMoneySpace = new MoneySpace("", 0, 0);
        myUtility1 = new Utility("", 1, "R", 2, 100, 10, rents);
        myUtility2 = new Utility("", 2, "R", 2, 100, 10, rents);
        myBoard = new Board(new ArrayList<>(Arrays.asList(myMoneySpace, myUtility1, myUtility2)));
    }

    @Test
    void doAction_PlayerPaysTimesDiceRoll() {
        player1.increaseMoney(500);
        myUtility1.changeOwner(new HumanPlayer(""));
        player1.moveTo(myMoneySpace);
        myCard.doAction(player1, new Bank(""), 3);
        for (Transaction t : player1.getTransactions()){
            t.doTransaction();
        }
        assertEquals(470, player1.getMoneyBalance());
    }

    @Test
    void doAction_PlayerCanBuyUtility() {
        Player bank = new Bank("");
        player1.increaseMoney(1000);
        myUtility1.changeOwner(bank);
        player1.moveTo(myMoneySpace);
        myCard.doAction(player1, bank, 4);
        assertEquals(0, player1.getTransactions().size());
        assertEquals(PlayerState.BUYING, player1.getCurrentState());
    }
}