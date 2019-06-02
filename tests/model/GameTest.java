package model;

import controller.exceptions.InvalidDataFileValueException;
import model.location.Location;
import model.location.propertySpace.PropertySpace;
import model.location.propertySpace.RealEstate;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import model.player.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game myGame;
    private static final String GAME_VERSION = "./resources/monopoly_regular/";
    private Player player1;
    private Player player2;
    private Player player3;
    private Queue<Player> playerQueue;
    private PropertySpace yellow1;
    private PropertySpace yellow2;
    private List<Location> myLocations;
    private List<PropertySpace> myProperties;
    private Board myBoard;
    private Map<Integer, Integer> rents = new HashMap<>();

    @BeforeEach
    void setUp() {
        try {
            myGame = new Game(GAME_VERSION);
        } catch (InvalidDataFileValueException e) {
            e.printStackTrace();
        }
        rents.put(0,100);
        player1 = new HumanPlayer("SARA");
        player2 = new HumanPlayer("JORDAN");
        player3 = new HumanPlayer("Q");
        playerQueue = new LinkedList<>(Arrays.asList(player1,player2,player3));
        myGame.setPlayers(playerQueue);
        yellow1 = new RealEstate("yellow1", 2, "YELLOW", 2, 300,20, rents);
        yellow2 = new RealEstate("yellow2", 3, "YELLOW", 2, 300, 20, rents);
        myLocations = new ArrayList<>(Arrays.asList(yellow1, yellow2));
        myProperties = new ArrayList<>(Arrays.asList(yellow1, yellow2));
        myBoard = new Board(myLocations);
    }


    @Test
    void getCurrentPlayer_StartingPlayerIsCurrentPlayer() {
        myGame.startGame();
        assertEquals(player1, myGame.getCurrentPlayer());
    }

    @Test
    void getCurrentPlayer_SecondPlayerIsNotCurrentPlayer() {
        myGame.startGame();
        assertNotEquals(player2, myGame.getCurrentPlayer());
    }


    @Test
    void startGame_CurrentPlayerGetsSet() {
        myGame.startGame();
        assertEquals(player1, myGame.getCurrentPlayer());
    }

    @Test
    void startGame_GameStatusChanges() {
        assertEquals(GameStatus.NOT_STARTED, myGame.getGameStatus());
        myGame.startGame();
        assertEquals(GameStatus.PLAYING, myGame.getGameStatus());
    }

    @Test
    void startNextTurn_ChangesToNextPlayersTurn() {
        myGame.startGame();
        Player player1 = myGame.getCurrentPlayer();
        myGame.startNextTurn();
        assertNotEquals(myGame.getCurrentPlayer(), player1);
    }

    @Test
    void startNextTurn_BankruptPlayerDoesNotGo() {
        myGame.startGame();
        myGame.getCurrentPlayer().changeState(PlayerState.BANKRUPT);
        myGame.startNextTurn();
        myGame.startNextTurn();
        myGame.startNextTurn();
        assertNotEquals(myGame.getCurrentPlayer(), player1);
    }

    @Test
    void startTurn_PlayersStateChanges() {
        myGame.startGame();
        myGame.startNextTurn();
        Player player1 = myGame.getCurrentPlayer();
        assertEquals(PlayerState.PRE_ROLL, player1.getCurrentState());
    }

    @Test
    void endTurn_ChangesToNextPlayersTurn() {
        myGame.startGame();
        Player player1 = myGame.getCurrentPlayer();
        myGame.endTurn();
        assertNotEquals(myGame.getCurrentPlayer(), player1);
    }

    @Test
    void endTurn_PlayersStateChanges() {
        myGame.startGame();
        Player player1 = myGame.getCurrentPlayer();
        player1.changeState(PlayerState.POST_ROLL);
        myGame.endTurn();
        assertEquals(PlayerState.NOT_YOUR_TURN, player1.getCurrentState());
    }

    @Test
    void endTurn_BankruptPlayerGetsTakenOut(){
        myGame.startGame();
        Player current = myGame.getCurrentPlayer();
        current.addTransaction(new Transaction(current, player3, -400));
        current.executeStateChange(new Bank(""), myGame.getMyRoll());
        assertEquals(PlayerState.BANKRUPT, current.getCurrentState());
        myGame.endTurn();
        assert(!myGame.getMyPlayers().contains(current));
    }

    @Test
    void endTurn_BankruptPlayerLosesProperties(){
        myGame.startGame();
        Player current = myGame.getCurrentPlayer();
        myProperties.get(1).changeOwner(current);
        current.changeState(PlayerState.BANKRUPT);
        myGame.endTurn();
        assert(!myGame.getMyPlayers().contains(current));
        assert(myProperties.get(1).getOwner().equals(myGame.getMyBank()));
    }

    @Test
    void startAuction_AuctioneesStateChanges(){
        myGame.startAuction(player1, myProperties.get(0));
        assertEquals(PlayerState.AUCTIONING, player2.getCurrentState());
    }

    @Test
    void startAuction_AuctionContinuesOnStateChange(){
        myGame.startAuction(player1, myProperties.get(0));
        player2.setBid(10);
        player2.changeState(PlayerState.NOT_YOUR_TURN);
        assertEquals(PlayerState.AUCTIONING, player3.getCurrentState());
    }

    @Test
    void startAuction_AuctionEndsWhenAllPlayersBidTooLow(){
        myProperties.get(0).changeOwner(player1);
        myGame.startAuction(player1, myProperties.get(0));
        player2.setBid(10);
        player2.changeState(PlayerState.NOT_YOUR_TURN);
        player3.setBid(20);
        player3.changeState(PlayerState.NOT_YOUR_TURN);
        assertEquals(PlayerState.AUCTIONING, player2.getCurrentState());
        player2.setBid(10);
        player2.changeState(PlayerState.NOT_YOUR_TURN);
        assertEquals(PlayerState.NOT_YOUR_TURN, player2.getCurrentState());
        assertEquals(PlayerState.NOT_YOUR_TURN, player3.getCurrentState());
        assertEquals(player3, myProperties.get(0).getOwner());
    }
}