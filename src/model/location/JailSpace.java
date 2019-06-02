package model.location;

import model.Transaction;
import model.player.Player;

/**
 * Represents a JailSpace on a Monopoly board. A Player may pass by or land on it naturally, but if they are sent to
 * a JailSpace from elsewhere on the board, they will become imprisoned and only escape upon doubles or a fee.
 *
 * Dependent on abstract Location, Player, classes
 *
 * @author leahschwartz
 */
public class JailSpace extends Location{

    private static final int MAX_TURNS_IN_JAIL = 3;
    private static final int COST_TO_GET_OUT = 50;

    public JailSpace(String name, int locationNumber){
        super(name, locationNumber);
    }


    @Override
    public void activateSpaceAction(Player player, Player bank, int lastRoll) {
        if (!wasLandedOn(player, lastRoll)){
            if (!player.isImprisoned()){
                player.setImprisonedCountdown(MAX_TURNS_IN_JAIL);
            }
            else{
                player.decreaseImprisonedCountdown();
                if (!player.isImprisoned()){
                    player.addTransaction(new Transaction(player, bank, -COST_TO_GET_OUT));
                }
            }
        }
        finishLanding(player);
    }
}
