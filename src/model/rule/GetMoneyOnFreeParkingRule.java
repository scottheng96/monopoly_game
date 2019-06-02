package model.rule;

import model.Board;
import model.GameStatus;
import model.Transaction;
import model.location.Location;
import model.roll.Roll;
import model.player.Player;
import model.player.PlayerState;
import java.util.Collection;

/**
 * This class is used for getting money on free parking rule
 *
 * @author leahschwartz
 */
public class GetMoneyOnFreeParkingRule extends Rule{

    @Override
    public boolean shouldRuleHappen(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player>
            allPlayers, Roll roll) {
        return playerJustGotToSpace(board, currentPlayer) && currentPlayer.getCurrentState().equals(PlayerState.ROLLING);
    }

    @Override
    public GameStatus doRule(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll) {
        int banksMoney = bank.withdrawAllMoney();
        System.out.println(currentPlayer.getName() + " got " + banksMoney + " for landing on free parking!");
        currentPlayer.addTransaction(new Transaction(currentPlayer, bank, banksMoney));
        return gameStatus;
    }

    private boolean playerJustGotToSpace(Board board, Player currentPlayer){
        Location freeParking = board.getLocation(board.getOfficialFreeParkingIndex());
        return currentPlayer.getCurrentLocation().equals(freeParking) && !currentPlayer.getPreviousLocation().equals(freeParking);
    }



}
