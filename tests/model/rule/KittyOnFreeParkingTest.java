package model.rule;

import model.*;
import model.location.Location;
import model.location.MoneySpace;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import model.player.PlayerState;
import model.roll.NSidedDie;
import model.roll.Roll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class KittyOnFreeParkingTest {

    private Rule myRule;
    private List<String> playerNames = new ArrayList<>(Arrays.asList("LEAH", "JORDAN", "Q"));
    private List<Player> myPlayers;
    private Location start = new MoneySpace("GO", 0, 300);
    private Location end = new MoneySpace("END", 1, -300);
    private Location freeParking = new MoneySpace("FREE PARKING", 2, 0);
    private List<Location> myLocations = new ArrayList<>(Arrays.asList(start, end, freeParking));
    private Board myBoard;
    private Bank myBank;
    private Roll myRoll = new Roll(new ArrayList<>(Collections.singleton(new NSidedDie(4))));

    @BeforeEach
    public void setup(){
        myBoard = new Board(myLocations);
        myPlayers = new ArrayList<>();
        myRule = new GetMoneyOnFreeParkingRule();
        for (String name : playerNames){
            Player player = new HumanPlayer(name);
            myPlayers.add(player);
        }
        myBank = new Bank("BANK");
        myBoard.setOfficialFreeParking(2);
    }



    @Test
    void shouldRuleHappen_RuleShouldHappen() {
        myPlayers.get(0).moveTo(start);
        myPlayers.get(0).changeState(PlayerState.ROLLING);
        myPlayers.get(0).moveTo(myBoard.getLocation(myBoard.getOfficialFreeParkingIndex()));
        assert(myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myRoll));
    }

    @Test
    void shouldRuleHappen_RuleShouldNotHappen() {
        myPlayers.get(0).moveTo((start));
        assertFalse(myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myRoll));
    }

    @Test
    void doRule_PlayerGetsMoneyFromBankKitty() {
        myBank.increaseMoney(700);
        myRule.doRule(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myBank, myRoll);
        assertEquals(PlayerState.IN_TRANSACTION, myPlayers.get(0).getCurrentState());
        for (Transaction t : myPlayers.get(0).getTransactions()){
            t.doTransaction();
        }
        assertEquals(700, myPlayers.get(0).getMoneyBalance());
    }

    @Test
    void doRule_BankKittyHasNoMoneyLeftAfterRule() {
        myBank.increaseMoney(400);
        myRule.doRule(myBoard, GameStatus.PLAYING, myPlayers.get(2), myPlayers, myBank, myRoll);
        assertEquals(0, myBank.getMoneyBalance());
    }


    @Test
    void doRule_PlayerGetsNoMoneyFromEmptyBankKitty() {
        myPlayers.get(2).increaseMoney(400);
        myRule.doRule(myBoard, GameStatus.PLAYING, myPlayers.get(2), myPlayers, myBank, myRoll);
        assertEquals(400, myPlayers.get(2).getMoneyBalance());
    }

}