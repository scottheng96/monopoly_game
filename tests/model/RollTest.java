package model;

import model.player.HumanPlayer;
import model.player.Player;
import model.roll.NSidedDie;
import model.roll.Roll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class RollTest {

    private Roll myRoll;
    private Player player1;

    @BeforeEach
    void setUp(){
        myRoll = new Roll(Collections.singletonList(new NSidedDie(5)));
        player1 = new HumanPlayer("Leah");
    }

    @Test
    void doNewRoll_DoesANewRoll() {
        assertEquals(0, myRoll.getTotalRoll());
        myRoll.doNewRoll(player1.getUniqueID());
        assertNotEquals(0, myRoll.getTotalRoll());
    }

    @Test
    void gotDoubles_GotDoubles() {
        myRoll = new Roll(new ArrayList<>(Arrays.asList(new NSidedDie(1), new NSidedDie(1))));
        myRoll.doNewRoll(player1.getUniqueID());
        assert(myRoll.gotDoubles());
    }

    @Test
    void gotDouble_PlayerDidNotGetDoubles(){
        myRoll.doNewRoll(player1.getUniqueID());
        System.out.println(myRoll.getAllRolls());
        assertFalse(myRoll.gotDoubles());
    }

    @Test
    void getLastTotalRoll() {
    }

    @Test
    void getLastAllRolls() {
    }

    @Test
    void getPlayersDoublesStreak() {
    }

    @Test
    void resetPlayersDoubleStreak() {
    }
}