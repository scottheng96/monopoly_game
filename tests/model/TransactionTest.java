package model;

import model.location.propertySpace.PropertySpace;
import model.location.propertySpace.RealEstate;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import model.player.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {


    Transaction manager;
    Player player1;
    Player player2;
    Player bank;
    Map<Integer,Integer> rents = new HashMap<>();
    PropertySpace property;



    @BeforeEach
    public void setup(){
        player1 = new HumanPlayer("One");
        player2 = new HumanPlayer("Two");
        rents.put(0, 100);
        bank = new Bank("BANK");
        property  = new RealEstate("place", 3, "yellow", 4, 200, 100, rents);
    }

    @Test
    void doTransaction_PlayerPaysOther() {
        player1.increaseMoney(200);
        manager = new Transaction(player1, player2, -200);
        manager.doTransaction();

      //  manager.doTransaction(player1, player2, -200);
        assertEquals(0, player1.getMoneyBalance());
        assertEquals(200, player2.getMoneyBalance());
    }


       @Test
    void doTransaction_PlayerBecomesBankrupt() {
        player1.increaseMoney(50);
        manager = new Transaction(player1, player2, -200);
        manager.doTransaction();
        assertEquals(0, player1.getMoneyBalance());
        assertEquals(50, player2.getMoneyBalance());
        assertEquals(PlayerState.BANKRUPT, player1.getCurrentState());
    }

    @Test
    void doTransaction_PlayerGoesIntoDebt() {
        player1.increaseMoney(50);
        player1.addProperty(property);
        manager = new Transaction(player1, player2, -100);
        manager.doTransaction();
        assertEquals(0, player1.getMoneyBalance());
        assertEquals(50, player2.getMoneyBalance());
        assertEquals(PlayerState.DEBT, player1.getCurrentState());
    }


    @Test
    void doTransaction_PlayerGetsPaidByOther() {
        player2.increaseMoney(400);
        manager = new Transaction(player1, player2, 40);
        manager.doTransaction();
        assertEquals(40, player1.getMoneyBalance());
        assertEquals(360, player2.getMoneyBalance());
    }

    @Test
    void doTransaction_PlayerPaysBank() {
        assertEquals(0, bank.getMoneyBalance());
        manager = new Transaction(player1, bank, 40);
        manager.doTransaction();
        assertEquals(40, player1.getMoneyBalance());
        assertEquals(0, bank.getMoneyBalance());
    }

    @Test
    void doTransaction_TransactionIsNotCompleteWhenPlayerCannotPay(){
        player1.increaseMoney(15);
        manager = new Transaction(player1, bank, -40);
        manager.doTransaction();
        assertFalse(manager.isComplete());
    }

    @Test
    void doTransaction_PlayerGetsOutOfDebt(){
        player1.increaseMoney(15);
        property.changeOwner(player1);
        manager = new Transaction(player1, bank, -40);
        manager.doTransaction();
        assertEquals(0, player1.getMoneyBalance());
        property.mortgage();
        assertEquals(100, player1.getMoneyBalance());
        assertEquals(1, player1.getTransactions().size());
        for (Transaction t: player1.getTransactions()){
            t.doTransaction();
        }
        assertEquals(75, player1.getMoneyBalance());
    }






}