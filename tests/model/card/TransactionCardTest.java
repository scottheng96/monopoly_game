package model.card;

import model.Transaction;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import model.player.PlayerState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionCardTest {

    Card myCard = new PaymentCard(0, "Chance", new int[]{-50});
    Player player1 = new HumanPlayer("Leah");
    Bank bank = new Bank("BANK");


    @Test
    void doAction_CardGivesPlayerTransaction() {
        myCard.doAction(player1, bank, 5);
        assertEquals(1, player1.getTransactions().size());
    }

    @Test
    void doAction_CardResultsInPlayerPayment() {
        player1.increaseMoney(100);
        myCard.doAction(player1, bank, 5);
        for (Transaction t : player1.getTransactions()){
            t.doTransaction();
        }
        assertEquals(50, player1.getMoneyBalance());
    }

    @Test
    void doAction_CardResultsInPlayerBankruptcy() {
        myCard.doAction(player1, bank, 5);
        for (Transaction t : player1.getTransactions()){
            t.doTransaction();
        }
        assertEquals(0, player1.getMoneyBalance());
        assertEquals(PlayerState.BANKRUPT, player1.getCurrentState());

    }

}