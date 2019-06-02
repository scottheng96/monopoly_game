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

class RichestPlayerAfterFirstBankruptcyWinsRuleTest {


    private Rule myRule;
    private List<String> playerNames = new ArrayList<>(Arrays.asList("LEAH", "JORDAN", "Q"));
    private List<Player> myPlayers;
    private Location start = new MoneySpace("GO", 0, 300);
    private Location end = new MoneySpace("END", 1, -300);
    private List<Location> myLocations = new ArrayList<>(Arrays.asList(start, end));
    private Board myBoard;
    private Bank myBank;
    private Roll myRoll = new Roll(new ArrayList<>(Collections.singleton(new NSidedDie(4))));


    @BeforeEach
    public void setup(){
        myBoard = new Board(myLocations);
        myPlayers = new ArrayList<>();
        myRule = new RichestPlayerAfterFirstBankruptcyWinsRule();
        for (String name : playerNames){
            Player player = new HumanPlayer(name);
            myPlayers.add(player);
        }
        myBank = new Bank("BANK");

    }

    @Test
    void shouldRuleHappen_ruleShouldHappen() {
        myPlayers.get(0).changeState(PlayerState.BANKRUPT);
        boolean ruleHappens = myRule.shouldRuleHappen(myBoard, GameStatus.GAME_OVER, myPlayers.get(0), myPlayers, myRoll);
        assert(ruleHappens);
    }

    @Test
    void shouldRuleHappen_ruleShouldNotHappen() {
        boolean ruleHappens = myRule.shouldRuleHappen(myBoard, GameStatus.GAME_OVER, myPlayers.get(0), myPlayers, myRoll);
        assertFalse(ruleHappens);
    }

    @Test
    void doRule_GameEnds() {
        GameStatus newStatus = myRule.doRule(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myBank, myRoll);
        assertEquals(GameStatus.GAME_OVER, newStatus);
    }
}