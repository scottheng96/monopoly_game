package model.player;

/**
 * Represents the Bank, which owns all Properties and Cards at the start
 *
 * @author leahschwartz
 */
public class Bank extends Player {


    public Bank(String name){
        super(name);
    }

    /**
     * Sets the current state of a player, in Bank's case its state will always be that it is not the Bank's turn
     * @param newPlayerState enum representing player's state
     */
    @Override
    public void changeState(PlayerState newPlayerState){
        setCurrentState(PlayerState.NOT_YOUR_TURN);
    }

    /**
     * Normally withdrawing from bank will not decrease money
     * @param money amount of money to withdraw
     * @return amount of money withdrawn
     */
    @Override
    public int withdrawMoney(int money){
        return money;
    }


    @Override
    public int withdrawAllMoney(){
        int allMoney = getMoneyBalance();
        super.withdrawMoney(getMoneyBalance());
        return allMoney;
    }

    /**
     * Bank can never go into debt, so it always has enough money
     * @return boolean
     */
    @Override
    public boolean hasEnoughMoney(int moneyNeeded){
        return true;
    }

}
