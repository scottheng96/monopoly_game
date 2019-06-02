package model.player;


import model.Transaction;
import model.roll.Roll;
import model.card.Card;
import java.util.HashSet;

/**
 * This enum is used for the state of a Player during game play
 *
 * @author leahschwartz
 */
public enum PlayerState {

    NOT_YOUR_TURN() {
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            return PRE_ROLL;
        }
    },

    PRE_ROLL() {
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            return ROLLING;
        }
    },

    ROLLING(){
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            if (!current.getCurrentLocation().isResolved()){
                current.getCurrentLocation().activateSpaceAction(current, bank, roll.getTotalRoll());
            }
            if (current.getCurrentState().equals(BUYING) || current.getCurrentState().equals(DRAWING_CARD)){
                return current.getCurrentState();
            }
            if (current.getCurrentState().equals(ROLLING)) {
                if (roll.gotDoubles() && !current.isImprisoned()){
                    return PRE_ROLL;
                }
                return POST_ROLL;
            }
            return current.getCurrentState().execute(current, bank, roll);
        }
    },

    BUYING(){
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            current.getCurrentLocation().activateSpaceAction(current, bank, roll.getTotalRoll());
            current.changeState(PlayerState.ROLLING);
            return current.getCurrentState().execute(current, bank, roll);
        }
    },

    DRAWING_CARD(){
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            for (Card card : new HashSet<>(current.getCardsNeedingActivation())) {
                card.doAction(current, bank, roll.getTotalRoll());
                card.setIsResolved(false);
                bank.addCard(current.removeCard(card));
                if (current.getCardsNeedingActivation().isEmpty()){
                    current.changeState(ROLLING);
                }
            }
            if (!current.getTransactions().isEmpty()){
                current.changeState(IN_TRANSACTION);
                return current.getCurrentState().execute(current, bank, roll);
            }
            else if (current.getCurrentState().equals(BUYING) || current.getCurrentState().equals(DRAWING_CARD)){
                return current.getCurrentState(); // could be DRAWING_CARD or BUYING
            }
            current.changeState(ROLLING);
            return current.getCurrentState().execute(current, bank, roll);
        }
    },

    IN_TRANSACTION(){
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            for (Transaction transaction : new HashSet<>(current.getTransactions())) {
                current.removeTransaction(transaction);
                transaction.doTransaction();
                if (current.getCurrentState().equals(BANKRUPT)){
                    return current.getCurrentState();
                }
            }
            if (!current.getTransactions().isEmpty()) {
                return current.getCurrentState().execute(current, bank, roll); // could be in transaction or in debt
            }
            current.changeState(ROLLING);
            return current.getCurrentState().execute(current, bank, roll);
        }
    },

    TRADING(){
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            for (Transaction transaction : new HashSet<>(current.getTransactions())) {
                transaction.doTransaction();
                current.removeTransaction(transaction);
            }
            return NOT_YOUR_TURN;
        }
    },

    AUCTIONING(){
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            return NOT_YOUR_TURN;
        }
    },

    POST_ROLL(){
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            return NOT_YOUR_TURN;
        }
    },

    DEBT(){
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            if (current.getNetWorth() <= 0){
                return BANKRUPT;
            }
            return current.getCurrentState();
        }
    },

    BANKRUPT(){
        @Override
        public PlayerState execute(Player current, Player bank, Roll roll) {
            return BANKRUPT;
        }
    };

    /**
     * Does the action a Player needs to do depending on its state
     * @param current a Player object that currently needs to do an action
     * @param bank
     * @param roll
     * @return a PlayerState representing the next state of the player
     */
    public abstract PlayerState execute(Player current, Player bank, Roll roll);
}
