package model.player;

import model.Board;
import model.Game;
import model.roll.Roll;
import model.rule.Rule;
import java.util.*;

/**
 * Represents a turn in the game for a CPU player. Enables CPU to take action on game decisions such as trading,
 * auctioning, or getting out of debt and keep the game moving.
 *
 * Dependent on Player, Board, Rule, Roll, Turn classes
 *
 * @author leahschwartz
 */
public class CPUPlayerTurn extends Turn {

    private final static double PROBABILITY_OF_TAKING_ACTION = .5;
    private Random myDecider;

    private List<PlayerState> myStatesToEndResolving = new ArrayList<>(Arrays.asList(PlayerState.POST_ROLL, PlayerState.BANKRUPT));

    public CPUPlayerTurn(Board board, Collection<Player> players, Player currentPlayer, List<Rule> rules, Player bank, Roll roll) {
        super(board, players, currentPlayer, rules, bank, roll);
        myDecider = new Random();
        checkForActionNeeded(bank, roll);
    }

    private void decideBid(Player bank, Roll roll){
        if (myDecider.nextDouble() <= PROBABILITY_OF_TAKING_ACTION){
            int bid = (int)(myDecider.nextDouble() * getMyCurrentPlayer().getMoneyBalance());
            while (bid > getMyCurrentPlayer().getMoneyBalance() / 2){
                bid = bid / 2; // ensures CPU does not bid ridiculously
            }
            getMyCurrentPlayer().setBid(bid);
        }
        getMyCurrentPlayer().executeStateChange(bank, roll);
    }

    private void decideTrade(Player bank, Roll roll){
        if (myDecider.nextDouble() <= PROBABILITY_OF_TAKING_ACTION){
            getMyCurrentPlayer().executeStateChange(bank, roll);
        }
        else {
            getMyCurrentPlayer().removeTransaction(getMyCurrentPlayer().getLatestTransaction());
            getMyCurrentPlayer().changeState(PlayerState.NOT_YOUR_TURN);
        }
    }

    private void checkForActionNeeded(Player bank, Roll roll){
        if (getMyCurrentPlayer().getCurrentState().equals(PlayerState.TRADING)){
            decideTrade(bank, roll);
        }
        if (getMyCurrentPlayer().getCurrentState().equals(PlayerState.AUCTIONING)){
            decideBid(bank, roll);
        }
    }

    @Override
    public void begin(Game game) {
        super.begin(game);
        doRobotTurn(game);
    }

    private void doRobotTurn(Game game) {
        rollDiceToMove();
        while (!myStatesToEndResolving.contains(getMyCurrentPlayer().getCurrentState())) {
            getMyCurrentPlayer().executeStateChange(game.getMyBank(), game.getMyRoll());
            if (getMyCurrentPlayer().getCurrentState().equals(PlayerState.PRE_ROLL)) {
                rollDiceToMove();
            }
        }
        game.endTurn();
    }

}