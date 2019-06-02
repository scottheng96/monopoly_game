package view.popup;

import controller.FrontEndDataReader;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import model.roll.Roll;
import model.player.Player;
import java.util.Collection;

/**
 * This class sets up an auctioning pop up.
 */
public class AuctioningPopUp extends AbstractPopUp {

    private final static int MONEY_INCREMENT = 10;
    private Spinner<Integer> myMoneySpinner;


    /** This method creates an auctioning pop up.
     * @param currentPlayer current player
     * @param allPlayers all the players
     * @param bank an imaginary player which serves as a bank
     * @param roll a roll
     * @param myFrontEndDataReader a reader that can be used to read front end data
     */
    public AuctioningPopUp(Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll, FrontEndDataReader myFrontEndDataReader) {
        super(currentPlayer, allPlayers, bank, roll, myFrontEndDataReader);
        getMainText().setText(String.format(getMessageString(currentPlayer.getCurrentState().name()),
                currentPlayer.getName(), findMaxBid(allPlayers)));
        myMoneySpinner = new Spinner<>();
        myMoneySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, currentPlayer.
                getMoneyBalance(), 0, MONEY_INCREMENT));
        addToPane(myMoneySpinner,  2, 1);
        myMoneySpinner.valueProperty().addListener((valueProperty, oldValue, newValue) ->{
            getCurrentPlayer().setBid(myMoneySpinner.getValue());
        });
        myMoneySpinner.setEditable(true);
    }

    private int findMaxBid(Collection<Player> allPlayers){
        int currentHighest = 0;
        for (Player player : allPlayers){
            if (player.getBid() > currentHighest){
                currentHighest = player.getBid();
            }
        }
        return currentHighest;
    }
}
