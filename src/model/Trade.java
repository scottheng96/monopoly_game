package model;

import model.location.propertySpace.PropertySpace;
import model.player.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used for Trading assets between players
 *
 * @author leahschwartz
 */
public class Trade extends Transaction {

    private List<PropertySpace> propertiesFromPlayer2;
    private List<PropertySpace> propertiesFromPlayer1;
    private Player player1;
    private Player player2;

    public Trade(Player p1, List<PropertySpace> fromP1, Player p2, List<PropertySpace> fromP2, int amountP1GetsPaid) {
        super(p1, p2, amountP1GetsPaid);
        propertiesFromPlayer2 = new ArrayList<>(fromP2);
        propertiesFromPlayer1 = new ArrayList<>(fromP1);
        player1 = p1;
        player2 = p2;
    }


    @Override
    public void doTransaction() {
        super.doTransaction();
        exchangeProperties(player1, propertiesFromPlayer2);
        exchangeProperties(player2, propertiesFromPlayer1);
    }

    private void exchangeProperties(Player getter, List<PropertySpace> toExchange){
        for (PropertySpace property : toExchange) {
            property.changeOwner(getter);
        }
    }

    public List<PropertySpace> getOfferedPropertyNames(){
        return propertiesFromPlayer1;
    }

    public List<PropertySpace> getAskedForPropertyNames(){
        return propertiesFromPlayer2;
    }


}
