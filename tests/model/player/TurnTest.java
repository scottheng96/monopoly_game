package model.player;

import model.*;
import model.location.Location;
import model.location.propertySpace.PropertySpace;
import model.location.propertySpace.RealEstate;
import model.roll.NSidedDie;
import model.roll.Roll;
import model.rule.RichestPlayerAfterFirstBankruptcyWinsRule;
import model.rule.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class TurnTest {

    private Turn myTurn;
    private Player player1;
    private Player player2;
    private Queue<Player> playerQueue;
    private Roll myRoll;
    private PropertySpace yellow1;
    private PropertySpace yellow2;
    private PropertySpace blue1;
    private List<Location> myLocations;
    private Board myBoard;
    private Map<Integer, Integer> rents = new HashMap<>();
    private Rule myRule;
    private Player myBank;

    @BeforeEach
    void setUp() {
         myRoll = new Roll(new ArrayList<>(Collections.singletonList(new NSidedDie(2))));
//        myDie = new NSidedDie(2);
        rents.put(0,100);
        player1 = new HumanPlayer("SARA");
        player2 = new HumanPlayer("JORDAN");
        playerQueue = new LinkedList<>(Arrays.asList(player1,player2));
        yellow1 = new RealEstate("yellow1", 2, "YELLOW", 2, 300,20, rents);
        yellow2 = new RealEstate("yellow2", 3, "YELLOW", 2, 300, 20, rents);
        blue1 = new RealEstate("blue1", 4, "BLUE", 2, 300, 20, rents);
        myLocations = new ArrayList<>(Arrays.asList(yellow1, yellow2, blue1));
        player1.moveTo(yellow1);
        myBoard = new Board(myLocations);
        myRule = new RichestPlayerAfterFirstBankruptcyWinsRule();
        myBank = new Bank("BANK");
        myTurn = new Turn(myBoard, playerQueue, player1, new ArrayList<>(Collections.singletonList(myRule)),
                myBank,  myRoll);
    }

    @Test
    void checkRules_GameEndsOnEndingStatus() {
        assertEquals(GameStatus.PLAYING, myTurn.checkRules());
        player1.changeState(PlayerState.BANKRUPT);
        assertEquals(GameStatus.GAME_OVER, myTurn.checkRules());
    }

    @Test
    void rollDiceToMove_PlayerMovesCorrectAmount() {
        int playerStartingLocation = player1.getCurrentLocation().getLocationNumber();
      // System.out.println(playerStartingLocation);
        myTurn.rollDiceToMove();
        int roll = 0;
        for (Integer number : myTurn.getAllRolls()){
            roll += number;
           System.out.println(number);
        }
     //   System.out.println(myTurn.getAllRolls());
        int playerEndingLocation = player1.getCurrentLocation().getLocationNumber();
     //  System.out.println(playerEndingLocation);
        assertEquals(playerStartingLocation + roll, playerEndingLocation);
    }

    @Test
    void rollDiceToMove_PlayersStateChanges() {
        player1.changeState(PlayerState.PRE_ROLL);
        myTurn.rollDiceToMove();
        assertNotEquals(PlayerState.PRE_ROLL, player1.getCurrentState());
        assertNotEquals(PlayerState.NOT_YOUR_TURN, player1.getCurrentState());
        assertNotEquals(PlayerState.BANKRUPT, player1.getCurrentState());
    }


    @Test
    void rollDice_DoublesAffectRoll() {
        myRoll = new Roll(new ArrayList<>(Arrays.asList(new NSidedDie(1), new NSidedDie(1))));
        myTurn = new Turn(myBoard, playerQueue, player1, new ArrayList<>(Collections.singletonList(myRule)),
                new Bank("BANK"), myRoll);
        myTurn.rollDice();
        assertEquals(1, myRoll.getPlayersDoublesStreak(player1.getUniqueID()));
        assert(myRoll.gotDoubles());
    }

    @Test
    void resolve_PlayerStateChanges() {
        player1.changeState(PlayerState.PRE_ROLL);
      //  myTurn.resolve();
        player1.executeStateChange(myBank, myRoll);
        assertEquals(PlayerState.ROLLING, player1.getCurrentState());
    }
}