package view.popup;

import controller.FrontEndDataReader;
import model.Transaction;
import model.roll.Roll;
import model.Trade;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import model.player.PlayerState;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This method creates a trading pop up.
 */
public class TradingPopUp extends AbstractPopUp{

    private Player myTradee;
    private Trade myTrade;

    /** This method constructs a trading pop up.
     * @param currentPlayer current player of the game
     * @param allPlayers all the players of the game
     * @param bank an imaginary player that serves as a bank
     * @param roll roll
     * @param myFrontEndDataReader a reader that can be used to read front end data
     */
    public TradingPopUp(Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll, FrontEndDataReader myFrontEndDataReader){
        super(currentPlayer, allPlayers, bank,  roll, myFrontEndDataReader);

        String name = currentPlayer.getName();
        myTrade = (Trade)currentPlayer.getLatestTransaction();
        String propertiesOffered = makePropertyNameString(myTrade.getOfferedPropertyNames());
        String propertiesRequested = makePropertyNameString(myTrade.getAskedForPropertyNames());

        getMainText().setText(String.format(getMessageString(currentPlayer.getCurrentState().name()),
                name, name, propertiesOffered, propertiesRequested,
                myTrade.getPayer().getName(), myTrade.getPayee().getName(), myTrade.getAmount()));

        myTradee = currentPlayer;

        getButtonBox().addNewButtonByID(REFUSE_BUTTON_ID);
        getButtonBox().getButtonByID(REFUSE_BUTTON_ID).setOnAction((refuse)->{
            close();
        });

    }

    private String makePropertyNameString(List<PropertySpace> properties){
        ArrayList<String> names = new ArrayList<>();
        for (PropertySpace property : properties){
            names.add(property.getName());
        }
        return String.join(",", names);
    }

    /**
     * This method closes the myTradee screen.
     */
    @Override
    public void close(){
        myTradee.changeState(PlayerState.NOT_YOUR_TURN);
        myTradee.removeTransaction(myTrade);
        super.close();
    }
}
