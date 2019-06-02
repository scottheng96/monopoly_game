package model.roll;

import java.util.*;

/**
 * This class is used for modelling a roll in order to keep and determine information about doubles and streaks
 *
 * @author leahschwartz
 */
public class Roll {

    private List<Die> myDice;
    private List<Integer> myRollsForDoubles;
    private List<Integer> myAllRolls;
    private int myTotalRoll;
    private Map<String, Integer> myDoublesStreaks;

    public Roll(List<Die> dice){
        myDice = List.copyOf(dice);
        myDoublesStreaks = new HashMap<>();
        myRollsForDoubles = new ArrayList<>();
        myAllRolls = new ArrayList<>();
    }

    private void resetForNextRoll(){
        myTotalRoll = 0;
        myRollsForDoubles.clear();
        myAllRolls.clear();
    }

    /**
     * Does an additional roll
     * @param rollerID a String representing the ID of the roller
     */
    public void doNewRoll(String rollerID){
        resetForNextRoll();
        for (Die die : myDice) {
            int roll = die.roll();
            myTotalRoll += roll;
            myAllRolls.add(roll);
        }
        addToDoublesList();
        updatePlayersDoubleStreak(rollerID);
    }

    /**
     * @return a boolean telling the caller if the roll included a doubles
     */
    public boolean gotDoubles(){
        return (new HashSet<>(myRollsForDoubles)).size() < myRollsForDoubles.size();
    }

    public int getTotalRoll() {
        return myTotalRoll;
    }

    public List<Integer> getAllRolls() {
        return List.copyOf(myAllRolls);
    }

    private void addToDoublesList(){
        for (Die die : myDice){
            if (die.isConsideredForDoubles()){
                myRollsForDoubles.add(die.getLastRoll());
            }
        }
    }

    /**
     * Gets the length of a streak of doubles for a player
     * @param ID a String representing the ID of a player
     * @return an int representing the number of consecutive doubls a player has rolled
     */
    public int getPlayersDoublesStreak(String ID){
        if (!myDoublesStreaks.containsKey(ID)){
            return 0;
        }
        return myDoublesStreaks.get(ID);
    }

    private void updatePlayersDoubleStreak(String ID){
        if (!myDoublesStreaks.containsKey(ID)){
            resetPlayersDoubleStreak(ID);
        }
        if (gotDoubles()){
            myDoublesStreaks.put(ID, myDoublesStreaks.get(ID) + 1);
        }
    }

    public void resetPlayersDoubleStreak(String ID){
        myDoublesStreaks.put(ID, 0);
    }

}
