package model;

import javafx.beans.property.SimpleBooleanProperty;
import model.player.Player;
import model.player.PlayerState;

/**
 * This class is used for all transactions
 *
 * @author leahschwartz
 */
public class Transaction {

    private SimpleBooleanProperty isComplete;
    private Player myPayer;
    private Player myPayee;
    private int myTotalAmountToPay;

    /**
     * Defines a transaction between two players for some amount of money
     * @param currentPlayer the player in which the money is in terms of (positive or negative)
     * @param other the other player in the deal
     * @param amountCurrentPlayerGetsPaid the money in the deal, in terms of the current player
     */
    public Transaction(Player currentPlayer, Player other, int amountCurrentPlayerGetsPaid) {
        myTotalAmountToPay = amountCurrentPlayerGetsPaid;
        assignPaymentRoles(currentPlayer, other);
        isComplete = new SimpleBooleanProperty(false);
        System.out.println(myPayer.getName() + " will pay " + myTotalAmountToPay + " to " + myPayee);

    }

    public SimpleBooleanProperty getIsCompleteProperty(){
        return isComplete;
    }

    public boolean isComplete(){
        return isComplete.getValue();
    }

    private void assignPaymentRoles(Player currentPlayer, Player other){
        if (myTotalAmountToPay < 0) {
            myPayer = currentPlayer;
            myPayee = other;
            myTotalAmountToPay = Math.abs(myTotalAmountToPay);
        } else {
            myPayer = other;
            myPayee = currentPlayer;
        }
    }

    private void dealWithPlayerDebt(int debt){
        if (myPayer.getNetWorth() == 0){
            myPayer.changeState(PlayerState.BANKRUPT);
        }
        else {
            myPayer.addTransaction(new Transaction(myPayer, myPayee, -debt));
            myPayer.changeState(PlayerState.DEBT);
        }
    }

    /**
     * Does the transaction defined by the constructor
     */
    public void doTransaction() {
        if (myPayer.hasEnoughMoney(myTotalAmountToPay)) {
            myPayee.increaseMoney(myPayer.withdrawMoney(myTotalAmountToPay));
            isComplete.setValue(true);
        } else if (myPayer.getMoneyBalance() > 0) {
            int moneyPlayerHas = myPayer.getMoneyBalance();
            Transaction smallTransaction = new Transaction(myPayer, myPayee, -moneyPlayerHas);
            myPayer.addTransaction(smallTransaction);
            smallTransaction.doTransaction();
            myPayer.removeTransaction(smallTransaction);
            dealWithPlayerDebt(myTotalAmountToPay - moneyPlayerHas);
        }
        else {
            dealWithPlayerDebt(myTotalAmountToPay);
            System.out.println(myPayer.getName() + " is in debt");
        }
    }

    public int getAmount(){
        return myTotalAmountToPay;
    }

    public Player getPayee(){
        return myPayee;
    }

    public Player getPayer(){
        return myPayer;
    }
}
