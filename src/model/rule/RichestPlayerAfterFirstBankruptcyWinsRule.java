package model.rule;

import model.Board;
import model.GameStatus;
import model.roll.Roll;
import model.player.Player;
import model.player.PlayerState;
import java.util.Collection;

/**
 * This class is used for the richest player after first bankruptcy wins the game rule
 *
 * @author leahschwartz
 */
public class RichestPlayerAfterFirstBankruptcyWinsRule extends Rule{

    @Override
    public boolean shouldRuleHappen(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player>
            allPlayers, Roll roll) {
        for (Player player : allPlayers){
            if (player.getCurrentState().equals(PlayerState.BANKRUPT)){
                return true;
            }
        }
        return false;
    }

    @Override
    public GameStatus doRule(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll) {
        Player richest = currentPlayer;
        for (Player player : allPlayers){
            if (player.getNetWorth() > richest.getNetWorth()){
                richest = player;
            }
        }
        richest.setIsWinner(true);
        return GameStatus.GAME_OVER;
    }
}
