package view.popup;

import controller.FrontEndDataReader;
import model.roll.Roll;
import model.player.Player;
import java.util.Collection;
import java.util.Map;

/**
 * This class is responsible to set up a drawing card pop up.
 */
public class DrawingCardPopUp extends AbstractPopUp {
    /** This method constructs a drawing card pop up.
     * @param currentPlayer current player of the game
     * @param allPlayers all the players of the game
     * @param bank an imaginary player that serves as a bank
     * @param roll roll
     * @param myFrontEndDataReader a reader that can be used to read front end data
     */
    public DrawingCardPopUp(Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll, FrontEndDataReader myFrontEndDataReader) {
        super(currentPlayer, allPlayers, bank,  roll, myFrontEndDataReader);
        Map<String,String> myCardTextsMap = myFrontEndDataReader.getMyCardTexts();
        String cardType = currentPlayer.getLatestCard().getDeckType();
        int number = currentPlayer.getLatestCard().getNumber();
        System.out.println(cardType + number);
        getMainText().setText(myCardTextsMap.get(cardType + number));
    }
}
