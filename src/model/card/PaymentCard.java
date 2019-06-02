package model.card;

import model.Transaction;
import model.player.Player;

/**
 * Represents a Card object which can be given to a player and upon activation, gives the player a transaction
 * which will either require them to pay, or pay them
 *
 * Dependent on abstract Card, Transaction, Player classes
 *
 * @author leahschwartz
 */
public class PaymentCard extends Card{

    public PaymentCard(int cardNumber, String deckType, int[] amountConfigurations){
        super(cardNumber, deckType, amountConfigurations);
    }

    @Override
    public void doAction(Player drawer, Player bank, int lastRoll) {

        for (int numberPayment = 0; numberPayment < getConfigurations().length; numberPayment++) {
            drawer.addTransaction(new Transaction(drawer, bank, getConfigurations()[numberPayment]));
        }
    }
}
