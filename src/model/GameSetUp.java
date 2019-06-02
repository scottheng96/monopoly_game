package model;

import controller.BackEndDataReader;
import controller.DataWriter;

import controller.exceptions.InvalidDataFileValueException;

import model.player.HumanPlayer;
import model.player.Player;
import model.roll.Die;
import model.roll.NSidedDie;
import model.rule.Rule;

import java.util.*;

@Deprecated
public class GameSetUp {

    private Board myBoard;
    private Queue<Player> myPlayers;
    private BackEndDataReader myDataReader;
    private DataWriter myWriter;
    private List<Die> myDice;
    private List<Rule> myRules;

    public GameSetUp() {
    }

    @Deprecated
    public void loadGame(String filename) throws InvalidDataFileValueException {
        myDataReader = new BackEndDataReader();
        myDataReader.readAllGameFiles(filename);
        loadPlayersFromFile();
        setUpGame();
    }
    @Deprecated
    public void makeNewGame(String gameVersion) throws InvalidDataFileValueException {
        myDataReader = new BackEndDataReader();
        myDataReader.readAllGameFiles(gameVersion);
        makeNewPlayers();
        setUpGame();
    }


    @Deprecated
    private void setUpGame(){
        myBoard = new Board(myDataReader.getLocations());
        myPlayers = new LinkedList<>(myDataReader.getPlayers());
        fillDice();
        fillRules();
    }

    @Deprecated
    private void fillPlayers(List<Player> allPlayersInGame){
        myPlayers = new LinkedList<>(allPlayersInGame);
        //TODO
    }

    @Deprecated
    private void fillDice(){
        myDice = new ArrayList<>();
        myDice.add(new NSidedDie(6));
        //TODO need dynamic dice creation
    }

    @Deprecated
    private void fillRules(){
        //TODO need dynamic rule creation
     //   myRules.add(new EvenBuildingRule());
    }

    @Deprecated
    private void loadPlayersFromFile(){
        fillPlayers(myDataReader.getPlayers());
    }

    @Deprecated
    private void makeNewPlayers(){
        fillPlayers(new ArrayList<>(Arrays.asList(new HumanPlayer("h"))));
        // TODO use data from splashscreen
    }


}
