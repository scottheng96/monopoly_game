package model.rule;

import model.Board;
import model.GameStatus;
import model.roll.Roll;
import model.player.Player;
import java.util.*;

/**
 * This abstract class is used for all customizable game rules
 *
 * @author leahschwartz
 */
public abstract class Rule{

    /**
     * Checks if rule should happen at this time in the game
     * @return boolean
     */
    public abstract boolean shouldRuleHappen(Board board, GameStatus gameStatus, Player currentPlayer,
                                             Collection<Player> allPlayers, Roll roll);

    /**
     * Does action associated with the rule
     * @return new status of game after the rule
     */
    public abstract GameStatus doRule(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll);
}
