package model.rule;

import model.Board;
import model.GameStatus;
import model.roll.Roll;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import java.util.Collection;

/**
 * This class is used for the even building rule
 *
 * @author leahschwartz
 */
public class EvenBuildingRule extends Rule{


    @Override
    public boolean shouldRuleHappen(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Roll roll) {
        return true;
    }

    @Override
    public GameStatus doRule(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll) {
        for (Player player : allPlayers) {
            for (PropertySpace property : player.getProperties()) {
                property.setHouseCanBeAdded(true);
                    for (PropertySpace propertyOfGroup :  player.getMyPropertiesOfGroup(property.getGroup())) {
                        if (!property.isPartOfMonopoly() || !propertiesAreEvenlyBuilt(property, propertyOfGroup)){
                            property.setHouseCanBeAdded(false);
                            break;
                        }
                    }
            }
        }
        return gameStatus;
    }

    private boolean propertiesAreEvenlyBuilt(PropertySpace wantToBuildOn, PropertySpace groupMember){
        return wantToBuildOn.getNumberOfHouses() <= groupMember.getNumberOfHouses();
    }
}
