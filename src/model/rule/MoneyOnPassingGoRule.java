package model.rule;

import model.Board;
import model.GameStatus;
import model.roll.Roll;
import model.player.Player;
import java.util.Collection;

/**
 * This class is used for the money on passing go rule
 *
 * @author leahschwartz
 */
public class MoneyOnPassingGoRule extends Rule {

    @Override
    public boolean shouldRuleHappen(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player>
            allPlayers, Roll roll) {
        return !playerWentToJail(currentPlayer, board) && playerPassedGo(currentPlayer, board) &&
                playerWasAtCorrectPointInTurn(currentPlayer);
    }

    @Override
    public GameStatus doRule(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll) {
       board.getLocation(board.getOfficialGoIndex()).activateSpaceAction(currentPlayer, bank, 0);
        return gameStatus;
    }


    private boolean playerPassedGo(Player player, Board board){
        int goIndex = board.getOfficialGoIndex();
        int currentIndex = player.getCurrentLocation().getLocationNumber();
        int prevIndex = player.getPreviousLocation().getLocationNumber();

        return  ((goIndex > prevIndex || prevIndex > currentIndex) && goIndex < currentIndex);
    }

    private boolean playerWentToJail(Player player, Board board){
        return player.getCurrentLocation() == board.getLocation(board.getOfficialJailIndex());
    }

    private boolean playerWasAtCorrectPointInTurn(Player player){
        return true;
    }

}
