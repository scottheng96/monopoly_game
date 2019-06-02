package model.location;

import model.card.Card;
import model.card.MovementCard;
import model.player.Player;

/**
 * Represents a location on a Monopoly board that moves a player. Upon landing, moves player to a different space
 * based on initial configurations and activates that space's action
 *
 * Dependent on abstract Location, Player, Card, classes
 *
 * @author leahschwartz
 */
public class TeleportSpace extends Location {

    private int myLocationToTeleportPlayerTo;
    private Card myAction;

    public TeleportSpace(String name, int locationNumber, int locationToTeleportPlayerTo){
        super(name, locationNumber);
        myLocationToTeleportPlayerTo = locationToTeleportPlayerTo;
        myAction = new MovementCard(0, "", new int[]{locationToTeleportPlayerTo});
    }

    @Override
    public void activateSpaceAction(Player player, Player bank, int lastRoll) {
        myAction.doAction(player, bank, lastRoll);
        finishLanding(player);
    }

    public int getMyLocationToTeleportPlayerTo() {
        return myLocationToTeleportPlayerTo;
    }
}
