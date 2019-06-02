package model.card;

import model.Transaction;
import model.player.Player;

/**
 * Represents a Card object which can be given to a player and upon activation, moves a player to a space on the board
 * where they must pay and makes them pay multiplied by the roll, traditionally this is the nearest utility card
 *
 * Dependent on abstract Card, MovementCard, Player classes
 *
 * @author leahschwartz
 */
public class MoveNearestUtilityCard extends MovementCard{

    private static int MULTIPLIER = 10;

    public MoveNearestUtilityCard(int cardNumber, String deckType, int[] movementConfigurations){
            super(cardNumber, deckType, movementConfigurations);
    }

    @Override
    public void doAction(Player drawer, Player bank, int lastRoll) {
        super.doAction(drawer, bank, lastRoll);

        for (Transaction transaction : drawer.getTransactions()){
            drawer.removeTransaction(transaction);
            drawer.addTransaction(new Transaction(transaction.getPayer(), transaction.getPayee(),
                    -1 * lastRoll * MULTIPLIER));
        }
    }
}
