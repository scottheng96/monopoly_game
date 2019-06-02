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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static model.player.PlayerState.ROLLING;
import static org.junit.jupiter.api.Assertions.*;

class DoubleMoneyOnGoRuleTest {


    private Rule myRule;
    private List<String> playerNames = new ArrayList<>(Arrays.asList("LEAH", "JORDAN", "Q"));
    private List<Player> myPlayers;
    private List<Location> myLocations = new ArrayList<>();
    private Board myBoard;
    private Player current;
    private Roll myRoll = new Roll(new ArrayList<>(Collections.singleton(new NSidedDie(1))));
    private Location go;


    @BeforeEach
    public void setup(){
        for (int i = 0; i < 4; i++){
            myLocations.add(new MoneySpace("END", i, 200));
        }
        myBoard = new Board(myLocations);
        myPlayers = new ArrayList<>();
        for (String name : playerNames){
            myPlayers.add(new HumanPlayer(name));
        }
        myBoard.setOfficialGo(1);
        myBoard.setOfficialJail(3);
        current = myPlayers.get(0);
        go = myBoard.getLocation(myBoard.getOfficialGoIndex());
        myRule = new DoubleMoneyOnGoRule();
        current = myPlayers.get(0);
    }


    @Test
    void shouldRuleHappen_RuleShouldHappen() {
        current.changeState(ROLLING);
        current.moveTo(myLocations.get(0));
        current.moveTo(go);
        assert(myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, current, myPlayers, myRoll));
    }

    @Test
    void shouldRuleHappen_RuleShouldNotHappen() {
        current.moveTo(myLocations.get(0));
        assertFalse(myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, current, myPlayers, myRoll));
    }

    @Test
    void doRule_PlayerGetsMoney() {
        current.moveTo(go);
        myRule.doRule(myBoard, GameStatus.PLAYING, current, myPlayers, new Bank(""), myRoll);
        assertEquals(1, current.getTransactions().size());
        current.executeStateChange(new Bank(""), myRoll);
        assertEquals(400, current.getMoneyBalance());
    }
}