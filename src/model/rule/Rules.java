package model.rule;

import model.location.propertySpace.PropertySpace;
import model.player.HumanPlayer;
import model.player.Player;
import java.util.Collection;

@Deprecated
public class Rules {

    // BASED ON NEW DESIGN IDEAS THIS CLASS WILL BE SPLIT INTO INDIVIDUAL RULES


    /**
     * Assesses if a player can build on a given PropertySpace at the instantaneous state of the game
     * @param builder A player object that is attempting to build on its PropertySpace
     * @param propertySpaceToBuildOn A PropertySpace object on which the builder is attempting to build
     * @return a boolean answering the question, "can player x build on PropertySpace y?"
     */
    public boolean canPlayerBuildOn(Player builder, PropertySpace propertySpaceToBuildOn){
        //TODO
        return false;
    }

    /**
     * Checks if game has ended
     * @param allPlayers all player in game
     * @return boolean
     */
    public boolean isGameOver(Collection<Player> allPlayers){
        //TODO
        return false;
    }

    /**
     * Chooses who wins based on particular game rule
     * @param allPlayers all player in game
     * @return winning player
     */
    public Player getWinner(Collection<Player> allPlayers){
        //TODO
        return new HumanPlayer("s");
    }

    /**
     * Decides if rent is necessary to pay - in some instances, a player might be unable to collect rent
     * @param lander player landed on PropertySpace
     * @param owner Owner of PropertySpace
     * @return boolean
     */
    public boolean doesLanderPaysRent(Player lander, Player owner){
        //TODO
        return false;
    }

    /**
     * Conducts the pass Go rule, since this commonly changes based on Game version
     */
    public void doPassGoRule(){
        //TODO
    }

    /**
     * Conducts the free parking rule, since this commonly changes based on Game version
     */
    public void doFreeParkingRule(){
        //TODO
    }

    public void doRollingDoublesRule(int numberOfTimesDoublesRolled, Player currentPlayer){
        //TODO
    }

}
