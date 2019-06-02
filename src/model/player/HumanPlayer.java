package model.player;

/**
 * This class is used for Human type Players to play the game
 *
 * @author leahschwartz
 */
public class HumanPlayer extends Player {

    public HumanPlayer(String name){
        super(name);
    }

    public HumanPlayer(String name, String uniqueID){
        super(name, uniqueID);
    }

    /**
     * Sets the current state of a player
     * @param newPlayerState enum representing player's state
     */
    @Override
    public void changeState(PlayerState newPlayerState){
        if (getCurrentState() == null || !getCurrentState().equals(PlayerState.BANKRUPT)) {
            setCurrentState(newPlayerState);
        }
    }
}
