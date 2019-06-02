package model.player;

import model.*;
import model.roll.Roll;
import model.rule.Rule;
import java.util.*;

/**
 * This class models a turn in the game
 *
 * @author leahschwartz
 */
public class Turn {

    private Board myBoard;
    private Queue<Player> myPlayers;
    private Player myBank;
    private GameStatus myGameStatus;
    private Player myCurrentPlayer;
    private List<Rule> myRules;
    private Roll myRoll;

    protected Player getMyCurrentPlayer() {
        return myCurrentPlayer;
    }

    protected Player getBank(){
        return myBank;
    }

    protected Roll getRoll(){
        return myRoll;
    }

    public Turn(Board board, Collection<Player> players, Player currentPlayer, List<Rule> rules,
                Player bank, Roll roll) {
        myBoard = board;
        myPlayers = new LinkedList<>(players);
        myCurrentPlayer = currentPlayer;
        myRoll = roll;
        myRules = rules;
        myBank = bank;
        myGameStatus = GameStatus.PLAYING;
    }

    /**
     * Checks and does all appropriate rules
     * @return a GameStatus checking if the game is over or not
     */
    public GameStatus checkRules(){
        for (Rule rule : myRules) {
            if (rule.shouldRuleHappen(myBoard, myGameStatus, myCurrentPlayer, myPlayers, myRoll)) {
                myGameStatus = rule.doRule(myBoard, myGameStatus, myCurrentPlayer, myPlayers, myBank, myRoll);
                if (myGameStatus.equals(GameStatus.GAME_OVER)){
                    break;
                }
            }
        }
        return myGameStatus;
    }

    public List<Integer> getAllRolls() {
        return myRoll.getAllRolls();
    }

    public void begin(Game game){
        myCurrentPlayer.executeStateChange(myBank, myRoll);
    }

    /**
     * Specifically rolls dice for on turn movement
     */
    public void rollDiceToMove() {
        myCurrentPlayer.executeStateChange(myBank, myRoll);
        rollDice();
        if (myRoll.gotDoubles()){
            myCurrentPlayer.setImprisonedCountdown(0);
        }
        checkRules();
        if (!myCurrentPlayer.isImprisoned()) {
            myCurrentPlayer.moveTo(myCurrentPlayer.getCurrentLocation().getForwardLocation(myRoll.getTotalRoll()));
        }
        checkRules();
    }

    /**
     * Rolls all dice in game and adds rolls to lists in order to determine total roll and if player got doubles
     * Could be used to roll dice on turn, when trying to get out of jail, or on a utility
     */
    public void rollDice() {
        myRoll.doNewRoll(myCurrentPlayer.getUniqueID());
    }

}
