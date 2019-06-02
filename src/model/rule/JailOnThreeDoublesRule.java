package model.rule;

import model.Board;
import model.GameStatus;
import model.roll.Roll;
import model.location.Location;
import model.player.Player;
import java.util.Collection;

/**
 * This class is used for the going to jail on three consecutive doubles rule
 *
 * @author leahschwartz
 */
public class JailOnThreeDoublesRule extends Rule{

    private static final int ROLLS_BEFORE_JAIL = 3;

    /**
     * Checks if rule should happen at this time in the game
     * @return boolean
     */
    public boolean shouldRuleHappen(Board board, GameStatus gameStatus, Player currentPlayer,
                                    Collection<Player> allPlayers, Roll roll){
        if (roll.getPlayersDoublesStreak(currentPlayer.getUniqueID()) >= ROLLS_BEFORE_JAIL){
            roll.resetPlayersDoubleStreak(currentPlayer.getUniqueID());
            return true;
        }
        return false;
    }

    /**
     * Does action associated with the rule
     * @return new status of game after the rule
     */
    public GameStatus doRule(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll){
        Location jail = board.getLocation(board.getOfficialJailIndex());
        currentPlayer.moveTo(jail);
        currentPlayer.getCurrentLocation().activateSpaceAction(currentPlayer, bank, 0);
        System.out.println("GO TO JAIL!!");
        return gameStatus;
    }
}
