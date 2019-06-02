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
import model.player.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MoneyOnPassingGoTest {

    private Rule myRule;
    private List<String> playerNames = new ArrayList<>(Arrays.asList("LEAH", "JORDAN", "Q"));
    private List<Player> myPlayers;
    private List<Location> myLocations = new ArrayList<>();
    private Board myBoard;
    private Player current;
    private Roll myRoll = new Roll(new ArrayList<>(Collections.singleton(new NSidedDie(1))));


    @BeforeEach
    public void setup(){
        for (int i = 0; i < 4; i++){
            myLocations.add(new MoneySpace("END", i, 200));
        }
        myBoard = new Board(myLocations);
        myPlayers = new ArrayList<>();
        myRule = new MoneyOnPassingGoRule();
        for (String name : playerNames){
            myPlayers.add(new HumanPlayer(name));
        }
        myBoard.setOfficialGo(1);
        myBoard.setOfficialJail(3);
        current = myPlayers.get(0);
    }

    @Test
    void shouldRuleHappen_RuleShouldHappen() {
        current.changeState(PlayerState.ROLLING);
        current.moveTo(myLocations.get(3));
        current.moveTo(myLocations.get(2));
        assert(myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, current, myPlayers, myRoll));
    }

    @Test
    void shouldRuleHappen_PlayerGoesToJail_RuleShouldNotHappen() {
        current.changeState(PlayerState.ROLLING);
        current.moveTo(myLocations.get(0));
        current.moveTo(myLocations.get(3));
        assertFalse(myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, current, myPlayers, myRoll));
    }

    @Test
    void shouldRuleHappen_PlayerWasOnGoAlready_RuleShouldNotHappen() {
        current.changeState(PlayerState.ROLLING);
        current.moveTo(myLocations.get(1));
        assertFalse(myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, current, myPlayers, myRoll));
    }

    @Test
    void shouldRuleHappen_PlayerDidNotPassGo_RuleShouldNotHappen() {
        current.changeState(PlayerState.ROLLING);
        current.moveTo(myLocations.get(2));
        current.moveTo(myLocations.get(2));
        assertFalse(myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, current, myPlayers,myRoll));
    }

    @Test
    void doRule_PlayerGetsMoney() {
        Player bank = new Bank("BANK");
        current.changeState(PlayerState.ROLLING);
        current.moveTo(new MoneySpace("filler", 5, 0));
        myRule.doRule(myBoard, GameStatus.PLAYING, current, myPlayers, bank, myRoll);
        current.changeState(current.getCurrentState().execute(current, bank, myRoll));
        assertEquals(200, current.getMoneyBalance());
    }
}