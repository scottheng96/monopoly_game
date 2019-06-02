package model.location;

import model.player.Player;

/**
 * Represents space on a Monopoly board which has no particular function. Upon landing, nothing happens to the player
 *
 * Dependent on abstract Location, Player classes
 *
 * @author leahschwartz
 */
public class CommonSpace extends Location {

    public CommonSpace(String name, int locationNumber){
        super(name, locationNumber);
    }

    @Override
    public void activateSpaceAction(Player player, Player bank, int lastRoll) {
        // left empty, nothing happens on a CommonSpace
    }
}
