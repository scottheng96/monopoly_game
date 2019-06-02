package view.popup;

import controller.FrontEndDataReader;
import model.roll.Roll;
import model.player.Player;
import java.util.Collection;

/**
 * This class sets up a bankrupt pop up.
 */
public class BankruptPopUp extends AbstractPopUp {

    /** This method constructs a pop up.
     * @param currentPlayer current player of the game
     * @param allPlayers all the players of the game
     * @param bank an imaginary player that serves as a bank
     * @param roll roll
     * @param myFrontEndDataReader a reader that can be used to read front end data
     */
    public BankruptPopUp(Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll, FrontEndDataReader myFrontEndDataReader) {
        super(currentPlayer, allPlayers, bank, roll, myFrontEndDataReader);
        getMainText().setText(String.format(getMessageString(currentPlayer.getCurrentState().name()), currentPlayer.getName()));
    }

    @Override
    protected void doResolveButtonAction(){
        close();
    }
}