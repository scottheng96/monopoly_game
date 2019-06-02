package model.location;

import model.Board;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JailSpaceTest {

    Player player = new HumanPlayer("LEAH");
    Location jail = new JailSpace("JAIL", 1);
    private List<Location> myLocations = new ArrayList<>();
    private Board myBoard;


    @BeforeEach
    public void setUp(){
        for (int i = 0; i < 4; i++){
            myLocations.add(new MoneySpace("END", i, 200));
        }
        myBoard = new Board(myLocations);
        myBoard.insertAtPosition(jail, 1);
    }

    @Test
    void wasLandedOn_PlayerDidLandOn(){
        player.moveTo(myLocations.get(0));
        player.moveTo(myLocations.get(1));
        assert(jail.wasLandedOn(player, 1));
    }


    @Test
    void wasLandedOn_PlayerDidLandOnFromAroundBoard(){
        player.moveTo(myLocations.get(3));
        player.moveTo(myLocations.get(1));
        assert(jail.wasLandedOn(player, 2));
    }

    @Test
    void wasLandedOn_PlayerDidNotLandOn(){
        player.moveTo(myLocations.get(0));
        player.moveTo(myLocations.get(1));
        assertFalse(jail.wasLandedOn(player, 3));
    }


    @Test
    void activateSpaceAction_PlayerDidNotLandOn_isImprisoned() {
        player.moveTo(myLocations.get(2));
        player.moveTo(jail);
        jail.activateSpaceAction(player, new Bank(""), 1);
        assert(player.isImprisoned());
    }


    @Test
    void activateSpaceAction_PlayerDidLandOn_isNotImprisoned() {
        player.moveTo(myLocations.get(0));
        player.moveTo(jail);
        jail.activateSpaceAction(player, new Bank(""), 1);
        assertFalse(player.isImprisoned());
    }

    @Test
    void activateSpaceAction_JailLetsPlayerOut(){
        player.moveTo(myLocations.get(2));
        player.moveTo(jail);
        jail.activateSpaceAction(player, new Bank(""), 1);
        for (int jailTurn = 0; jailTurn < 3; jailTurn++) {
            assert (player.isImprisoned());
            jail.activateSpaceAction(player, new Bank(""), 1);
        }
        assertFalse(player.isImprisoned());
    }

    @Test
    void activateSpaceAction_playerPaysOnReleaseFromJail(){
        player.moveTo(myLocations.get(2));
        player.moveTo(jail);
        jail.activateSpaceAction(player, new Bank(""), 1);
        for (int jailTurn = 0; jailTurn < 3; jailTurn++) {
            assert (player.isImprisoned());
            jail.activateSpaceAction(player, new Bank(""), 1);
        }
        assertEquals(1, player.getTransactions().size());
    }


}