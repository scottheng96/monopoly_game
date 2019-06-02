package model.location;

import model.card.Card;
import model.card.PaymentCard;
import model.player.Player;

/**
 * Represents a Money giving or taking space on a Monopoly board. Upon landing, gives the player a Transaction to
 * complete
 *
 * Dependent on abstract Location, Player, Card, and Transaction classes
 *
 * @author leahschwartz
 */
public class MoneySpace extends Location {

    private int myAmountToGet;
    private Card myAction;

    public MoneySpace(String name, int locationNumber, int amountToGet){
        super(name, locationNumber);
        myAmountToGet = amountToGet;
        myAction = new PaymentCard(0, "", new int[]{amountToGet});
    }

    @Override
    public void activateSpaceAction(Player player, Player bank, int lastRoll) {
        myAction.doAction(player, bank, lastRoll);
        finishLanding(player);
    }

    public int getMyAmountToGet() {
        return myAmountToGet;
    }
}
