package view.popup;

import controller.FrontEndDataReader;
import model.roll.Roll;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import model.player.PlayerState;
import java.util.Collection;

/**
 * This method sets up a buying pop up.
 */
public class BuyingPopUp extends AbstractPopUp {
    /** This method constructs a buying pop up.
     * @param currentPlayer current player of the game
     * @param allPlayers all the players of the game
     * @param bank an imaginary player that serves as a bank
     * @param roll roll
     * @param myFrontEndDataReader a reader that can be used to read front end data
     */
    public BuyingPopUp(Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll, FrontEndDataReader myFrontEndDataReader) {
        super(currentPlayer, allPlayers, bank,  roll, myFrontEndDataReader);

        PropertySpace myLocation = (PropertySpace) currentPlayer.getCurrentLocation();
        String locationName = myLocation.getName();
        int locationCost = myLocation.getCost();
        String playerName = currentPlayer.getName();
        getMainText().setText(String.format(getMessageString(currentPlayer.getCurrentState().name()), playerName,
                locationName, locationCost));

       makeRefuseToBuyButton(currentPlayer, bank, roll);
    }

    private void makeRefuseToBuyButton(Player currentPlayer, Player bank, Roll roll){
        getButtonBox().addNewButtonByID(REFUSE_BUTTON_ID);
        getButtonBox().getButtonByID(REFUSE_BUTTON_ID).setOnAction((refuse)->{
            currentPlayer.getCurrentLocation().setIsResolved(true);
            currentPlayer.changeState(PlayerState.ROLLING);
            currentPlayer.executeStateChange(bank, roll);
            close();
        });
    }

}
