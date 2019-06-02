package model.player;

/**
 * Holds all human player profiles. Used for them to save stats and accomplishments.
 *
 * This class was not completed, but would be useful for future extension of the project
 */
public interface PlayerProfile {

    /**
     * makes a new player profile
     * @param username A String representing the username for a certain human player
     * @param password A String representing the password for a certain huamn player to log in
     */
    public void makeNewProfile(String username, String password);

    /**
     * used so that a human player can log in to their personal profile
     * @param username A String representing the username for a certain human player
     * @param password A String representing the password for a certain huamn player to log in
     */
    public void logIn(String username, String password);
}
