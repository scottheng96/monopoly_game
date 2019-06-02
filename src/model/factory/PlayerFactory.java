package model.factory;

import model.player.CPUPlayer;
import model.player.HumanPlayer;
import model.player.Player;
import java.util.Properties;

/**
 * Represents a factory design to create new players
 *
 * Dependent on Player and subclasses
 *
 * @author leahschwartz
 */
public class PlayerFactory {

    private static final String NAME_KEY = "NAME";
    private static final String UNIQUE_ID_KEY = "UNIQUE_ID";
    private static final String TYPE_KEY = "TYPE";
    private static final String STARTING_SPACE_KEY = "STARTING_SPACE";
    private static final String MONEY_KEY = "MONEY";
    private static final String STARTING_LOCATION_NUMBER = "";
    private static final String CPU_TYPE = "CPUPLAYER";

    public Player create(Properties properties){
        String name = properties.getProperty(NAME_KEY);
        String ID = properties.getProperty(UNIQUE_ID_KEY);
        Player player = makePlayer(name, properties.getProperty(TYPE_KEY), ID);
        configurePlayer(player, properties.getProperty(STARTING_SPACE_KEY), properties.getProperty(MONEY_KEY));
        return player;
    }

    public Player create(String name, String type, String money){
        Player player = makePlayer(name, type, "");
        configurePlayer(player, STARTING_LOCATION_NUMBER, money);
        return player;
    }

    private void configurePlayer(Player player, String startingSpace, String money) {
        int startingLocationNumber = Integer.parseInt(startingSpace);
        int moneyNumber = Integer.parseInt(money);
        player.setStartingLocationNumber(startingLocationNumber);
        player.increaseMoney(moneyNumber);
    }


    private Player makePlayer(String name, String type, String ID) {
        if (type.equalsIgnoreCase(CPU_TYPE)) {
            return new CPUPlayer(name, ID);
        }
        else{
            return new HumanPlayer(name, ID);
        }
    }
}


