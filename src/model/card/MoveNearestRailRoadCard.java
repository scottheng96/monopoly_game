package model.card;
import model.Transaction;
import model.player.Player;

/**
 * Represents a Card object which can be given to a player and upon activation, moves a player to a space on the board
 * where they must pay and makes them pay twice as much, traditionally this is the nearest railroad card
 *
 * Dependent on abstract Card, MovementCard, Player classes
 *
 * @author leahschwartz
 */
public class MoveNearestRailRoadCard extends MovementCard{

    private static int MULTIPLIER = 2;

    public MoveNearestRailRoadCard(int cardNumber, String deckType, int[] movementOptions){
        super(cardNumber, deckType, movementOptions);
    }

    @Override
    public void doAction(Player drawer, Player bank, int lastRoll) {
        super.doAction(drawer, bank, lastRoll);
        for (Transaction transaction : drawer.getTransactions()){
            drawer.removeTransaction(transaction);
            drawer.addTransaction(new Transaction(transaction.getPayer(), transaction.getPayee(),
                    -1 * MULTIPLIER * transaction.getAmount()));
        }
    }
}
