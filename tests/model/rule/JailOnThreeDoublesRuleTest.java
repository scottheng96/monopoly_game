package model.rule;

import model.Board;
import model.GameStatus;
import model.roll.NSidedDie;
import model.roll.Roll;
import model.location.Location;
import model.location.MoneySpace;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class JailOnThreeDoublesRuleTest {

    private Rule myRule;
    private List<String> playerNames = new ArrayList<>(Arrays.asList("LEAH", "JORDAN", "Q"));
    private List<Player> myPlayers;
    private Location start = new MoneySpace("GO", 0, 300);
    private Location end = new MoneySpace("END", 1, -300);
    private List<Location> myLocations = new ArrayList<>(Arrays.asList(start, end));
    private Board myBoard;
    private Bank myBank;
    private Roll myRoll = new Roll(new ArrayList<>(Collections.singleton(new NSidedDie(1))));


    @BeforeEach
    public void setup(){
        myPlayers = new ArrayList<>();
        myBoard = new Board(myLocations);
        myBoard.setOfficialJail(1);
        myRule = new JailOnThreeDoublesRule();
        for (String name : playerNames){
            Player player = new HumanPlayer(name);
            player.moveTo(new MoneySpace("place", 7, 300));
            myPlayers.add(player);
        }
        myBank = new Bank("BANK");

    }

    @Test
    void shouldRuleHappen_ruleShouldHappen() {
//        myPlayers.get(0).increaseNumberOfDoubles();
//        myPlayers.get(0).increaseNumberOfDoubles();
//        myPlayers.get(0).increaseNumberOfDoubles();
       myRoll = new Roll(new ArrayList<>(Arrays.asList(new NSidedDie(1), new NSidedDie(1))));
        for (int num = 0; num < 4; num++){
            myRoll.doNewRoll(myPlayers.get(0).getUniqueID());
            System.out.println(myRoll.getPlayersDoublesStreak(myPlayers.get(0).getUniqueID()));
        //    myRoll.updatePlayersDoubleStreak(myPlayers.get(0).getUniqueID());
        }
        System.out.println(myRoll.getPlayersDoublesStreak(myPlayers.get(0).getUniqueID()));
        boolean happens = myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myRoll);
        assert(happens);
    }

    @Test
    void shouldRuleHappen_ruleShouldNotHappen() {
    //    myPlayers.get(0).increaseNumberOfDoubles();
        boolean happens = myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myRoll);
        assertFalse(happens);
    }

    @Test
    void doRule() {
        GameStatus newStatus = myRule.doRule(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myBank, myRoll);
        assertEquals(GameStatus.PLAYING, newStatus);
        assertEquals(1, myPlayers.get(0).getCurrentLocation().getLocationNumber());
    }
}