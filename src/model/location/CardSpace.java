package model.location;

import model.card.Card;
import model.player.Player;

/**
 * Represents a Card giving space on a Monopoly board such as Chance or Community Chest. Upon landing, gives the
 * player a Card of a particular decktype
 *
 * Dependent on abstract Location, Player, and Card classes
 *
 * @author leahschwartz
 */
public class CardSpace extends Location {

    private String myCardType;

    public CardSpace(String name, int locationNumber, String deckType){
        super(name, locationNumber);
        myCardType = deckType;
    }

    @Override
    public void activateSpaceAction(Player player, Player bank, int lastRoll) {
        if (bank.hasCardsOfType(myCardType)) {
            Card given = bank.removeCardByType(myCardType);
            player.addCard(given);
        }
        finishLanding(player);
    }

    /**
     * CardSpace should always be resolved after landing on it, since player would not need to draw a second card
     * @param player
     */
    @Override
    protected void finishLanding(Player player){
        setIsResolved(true);
    }

    public String getCardType(){
        return myCardType;
    }
}
