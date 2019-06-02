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

class TeleportSpaceTest {

    Player player = new HumanPlayer("LEAH");
    Location teleport = new TeleportSpace("", 4, 2);
    private List<Location> myLocations = new ArrayList<>();
    private Board myBoard;


    @BeforeEach
    public void setUp(){
        for (int i = 0; i < 4; i++){
            myLocations.add(new MoneySpace("END", i, 200));
        }
        myBoard = new Board(myLocations);
        myBoard.insertAtEnd(teleport);
    }

    @Test
    void activateSpaceAction_playerMoves() {
        player.moveTo(teleport);
        teleport.activateSpaceAction(player, new Bank("bank"), 4);
        assertEquals(2, player.getCurrentLocation().getLocationNumber());
    }

    @Test
    void activateSpaceAction_newSpaceActivates() {
        player.moveTo(teleport);
        teleport.activateSpaceAction(player, new Bank("bank"), 4);
        assertEquals(1, player.getTransactions().size());
    }
}