package view.popup;

import controller.FrontEndDataReader;
import model.Transaction;
import model.roll.Roll;
import model.player.Player;
import java.util.Collection;

/**
 * This class sets up a debt pop up..
 */
public class DebtPopUp extends AbstractPopUp {
    /** This method constructs a debt pop up.
     * @param currentPlayer current player of the game
     * @param allPlayers all the players of the game
     * @param bank an imaginary player that serves as a bank
     * @param roll roll
     * @param myFrontEndDataReader a reader that can be used to read front end data
     */
    public DebtPopUp(Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll, FrontEndDataReader myFrontEndDataReader) {
        super(currentPlayer, allPlayers, bank,  roll, myFrontEndDataReader);
        Transaction mostRecent = currentPlayer.getLatestTransaction();
        getMainText().setText(String.format(getMessageString(currentPlayer.getCurrentState().name()),
                currentPlayer.getName(), mostRecent.getAmount()));
    }

    @Override
    protected void doResolveButtonAction(){
        close();
    }
}