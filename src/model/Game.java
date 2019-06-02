package model;

import controller.BackEndDataReader;
import controller.exceptions.InvalidDataFileValueException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import model.factory.TurnFactory;
import model.location.propertySpace.PropertySpace;
import model.player.HumanPlayer;
import model.player.Turn;
import model.player.Player;
import model.player.PlayerState;
import model.roll.Roll;
import model.rule.Rule;

import java.util.*;

/**
 * Represents a full Game of Monopoly that can begin and end with players who are able to move around the board, do
 * actions such as buying cards or landing on spaces, and participate in auctions.
 *
 * Dependent on Board, Player, roll, GameStatus, PlayerState, Rule, Turn, TurnFactory, DataReader, Location classes
 *
 * @author leahschwartz
 */
public class Game {

    private Board myBoard;
    private Queue<Player> myPlayers;
    private Player myBank;
    private Roll myRoll;
    private SimpleObjectProperty<GameStatus> myGameStatus;
    private List<Rule> myRules;
    private Turn myTurn;
    private SimpleObjectProperty<Player> myCurrentPlayer;
    private TurnFactory myTurnFactory;
    private Set<PropertySpace> myAllProperties;
    private int greatestBid;
    private ChangeListener<PlayerState> myAuctionListener;
    private Player myAuctionee;

    public Game(String gameVersion) throws InvalidDataFileValueException {
        setUpGame(gameVersion);
        myTurnFactory = new TurnFactory();
        myGameStatus = new SimpleObjectProperty<>(GameStatus.NOT_STARTED);
    }

    /**
     * This method is for Testing purposes ONLY
     * @param newPlayers player of game
     */
    public void setPlayers(Queue<Player> newPlayers){
        myPlayers = newPlayers;
        setUpPlayersForGame();
    }

    public GameStatus getGameStatus() {
        return myGameStatus.getValue();
    }

    public Queue<Player> getMyPlayers() {return myPlayers;};

    public Player getMyBank() {return myBank;}

    public Roll getMyRoll() {return myRoll;}

    public SimpleObjectProperty<Player> getMyCurrentPlayer(){
        return myCurrentPlayer;
    }

    public Set<PropertySpace> getAllProperties(){
        return myAllProperties;
    }

    public SimpleObjectProperty<GameStatus> getMyGameStatus() {return myGameStatus;}

    public Board getBoard(){
        return myBoard;
    }

    public Player getCurrentPlayer() {
        return myCurrentPlayer.get();
    }

    private void setUpGame(String gameDataFilesPath) throws InvalidDataFileValueException {
        BackEndDataReader myDataReader = new BackEndDataReader();
        myDataReader.readAllGameFiles(gameDataFilesPath);
        myBoard = myDataReader.getBoard();
        myPlayers = new LinkedList<>(myDataReader.getPlayers());
        myRoll = new Roll(myDataReader.getDice());
        myRules = myDataReader.getRules();
        myBank = myDataReader.getBank();
        myCurrentPlayer = new SimpleObjectProperty<>(new HumanPlayer(""));
        setUpPlayersForGame();
        collectAllProperties();
    }

    private void setUpPlayersForGame(){
        for (Player player : myPlayers){
            player.moveTo(myBoard.getLocation(player.getStartingLocationNumber()));
            player.changeState(PlayerState.NOT_YOUR_TURN);
        }
    }

    private void collectAllProperties(){
        myAllProperties = new HashSet<>();
        for (Player player : myPlayers){
            myAllProperties.addAll(player.getProperties());
        }
        myAllProperties.addAll(myBank.getProperties());
    }


    private void setCurrentPlayer(Player player){
        myCurrentPlayer.setValue(player);
    }

    /**
     * Begins set-up game of Monopoly by changing the GameStatus and starting the first player's turn
     */
    public void startGame() {
        myGameStatus.setValue(GameStatus.PLAYING);
        startNextTurn();
    }

    public void makeNewTurn(Player playerForTurn) {
        myTurn = myTurnFactory.create(myBoard, myPlayers, playerForTurn, myRules, myBank, myRoll);
    }

    private Player chooseNextPlayer(){
        setCurrentPlayer(myPlayers.remove());
        while (getCurrentPlayer().getCurrentState().equals(PlayerState.BANKRUPT)) {
            setCurrentPlayer(myPlayers.remove());
        }
        myPlayers.add(getCurrentPlayer());
        return getCurrentPlayer();
    }

    /**
     * Begins the turn of the next player in game order
     */
    void startNextTurn(){
        makeNewTurn(chooseNextPlayer());
        myTurn.begin(this);
    }

    public void rollToMove(){
        myTurn.rollDiceToMove();
    }

    public GameStatus checkRules(){
       return myTurn.checkRules();
    }

    /**
     * Finishes the turn of the current player and begins the next player's turn if the game is not over
     */
    public void endTurn() {
        myGameStatus.setValue(checkRules());
        if (!myGameStatus.getValue().equals(GameStatus.GAME_OVER)){
            myRoll.resetPlayersDoubleStreak(getCurrentPlayer().getUniqueID());
            getCurrentPlayer().executeStateChange(myBank, myRoll);
            for (int locationNumber = 0; locationNumber < myBoard.getSize(); locationNumber++) {
                myBoard.getLocation(locationNumber).resetForNextTurn();
            }
            removeBankruptPlayers();
            startNextTurn();
        }
    }

    private void removeBankruptPlayers(){
        Queue<Player> stillInPlayers = new LinkedList<>();
        for (Player player : myPlayers){
            if (!player.getCurrentState().equals(PlayerState.BANKRUPT)){
                stillInPlayers.add(player);
            }
            else {
                for (PropertySpace property : player.getProperties()){
                    property.changeOwner(myBank);
                }
            }
        }
        myPlayers = stillInPlayers;
    }

    /**
     * Begins an auction in which players bid until only the highest bidder is left and recieves the property
     * @param auctioner Player auctioning their property
     * @param auctioned property being auctioned
     */
    public void startAuction(Player auctioner, PropertySpace auctioned) {
        greatestBid = -1;
        LinkedList<Player> auctionPlayers = new LinkedList<>(myPlayers);
        auctionPlayers.remove(auctioner);
        myAuctionListener = (obs, oldValue, newValue) -> startAuctionRound(auctionPlayers, auctioned);
        configureAuctioneeForNextRound(auctionPlayers);
    }

    private void startAuctionRound(LinkedList<Player> auctionPlayers, PropertySpace auctioned) {
        if (myAuctionee.getBid() > greatestBid) {
            greatestBid = myAuctionee.getBid();
            auctionPlayers.addLast(myAuctionee);
        }
        myAuctionee.getStateProperty().removeListener(myAuctionListener);
        if (auctionPlayers.size() <= 1) {
            endAuction(auctionPlayers, auctioned, auctionPlayers.getFirst().getBid());
        } else {
            configureAuctioneeForNextRound(auctionPlayers);
        }
    }

    private void configureAuctioneeForNextRound(LinkedList<Player> auctionPlayers){
        myAuctionee = auctionPlayers.removeFirst();
        myAuctionee.changeState(PlayerState.AUCTIONING);
        myAuctionee.getStateProperty().addListener(myAuctionListener);
        makeNewTurn(myAuctionee);
    }

    private void endAuction(LinkedList<Player> allAuctionPlayers, PropertySpace auctioned, int winningBid){
        Player winner = allAuctionPlayers.getFirst();
        auctioned.getOwner().increaseMoney(winner.withdrawMoney(winningBid));
        auctioned.changeOwner(winner);
        for (Player player : myPlayers){
            player.setBid(0);
        }
    }


    @Deprecated
    /**
     * a Player object buys the Location that they are currently on
     */
    public void playerBuysProperty() {
        PropertySpace space = (PropertySpace) getCurrentPlayer().getCurrentLocation();
        space.changeOwner(getCurrentPlayer());
        getCurrentPlayer().withdrawMoney(space.getCost());
        getCurrentPlayer().changeState(PlayerState.POST_ROLL);
    }
}
