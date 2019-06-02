package model.rule;

import model.Board;
import model.GameStatus;
import model.roll.Roll;
import model.player.Player;
import model.player.PlayerState;
import java.util.Collection;

/**
 * This class is used for the last player standing wins the game rule which allows the last player to go bankrupt to win
 *
 * @author leahschwartz
 */
public class LastPlayerStandingWinsRule extends Rule{


    @Override
    public boolean shouldRuleHappen(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player>
            allPlayers, Roll roll) {
        int bankruptPlayerCount = 0;
        for (Player player : allPlayers){
            if (player.getCurrentState().equals(PlayerState.BANKRUPT)){
                bankruptPlayerCount++;
            }
        }
        return bankruptPlayerCount == allPlayers.size() - 1;
    }

    @Override
    public GameStatus doRule(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll) {
        for (Player player : allPlayers){
            if (!player.getCurrentState().equals(PlayerState.BANKRUPT)){
                player.setIsWinner(true);
            }
        }
        return GameStatus.GAME_OVER;
    }
}
