package model.rule;

import model.Board;
import model.GameStatus;
import model.roll.Roll;
import model.player.Player;
import model.player.PlayerState;
import java.util.Collection;

/**
 * This class is used for modeling the double money recieved on go rule
 *
 * @author leahschwartz
 */
public class DoubleMoneyOnGoRule extends Rule{


    @Override
    public boolean shouldRuleHappen(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Roll roll) {
        return playerIsOnGo(board, currentPlayer) && currentPlayer.getCurrentState().equals(PlayerState.ROLLING) &&
                !currentPlayer.getCurrentLocation().equals(currentPlayer.getPreviousLocation());
    }

    @Override
    public GameStatus doRule(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll) {
        currentPlayer.getCurrentLocation().activateSpaceAction(currentPlayer, bank, 0);
        currentPlayer.getCurrentLocation().setIsResolved(false);
        return gameStatus;
    }

    private boolean playerIsOnGo(Board board, Player currentPlayer){
        return currentPlayer.getCurrentLocation().equals(board.getLocation(board.getOfficialGoIndex()));

    }
}
