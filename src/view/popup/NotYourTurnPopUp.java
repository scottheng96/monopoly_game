package view.popup;

import controller.FrontEndDataReader;
import model.roll.Roll;
import model.player.Player;
import java.util.Collection;

public class NotYourTurnPopUp extends AbstractPopUp {
    public NotYourTurnPopUp(Player myCurrentPlayer, Collection<Player> allPlayers, Player bank, Roll roll, FrontEndDataReader myFrontEndDataReader) {
        super( myCurrentPlayer, allPlayers, bank,  roll, myFrontEndDataReader);
        // No popup depiction for this state
    }

    @Override
    public void show() {
        super.show();
        close();
    }
}
