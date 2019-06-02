package controller;

import controller.exceptions.InvalidDataFileValueException;
import model.*;
import model.card.Card;
import model.factory.*;
import model.location.Location;
import model.location.MoneySpace;
import model.location.propertySpace.PropertySpace;
import model.player.Bank;
import model.player.Player;
import model.roll.Die;
import model.rule.Rule;
import java.io.*;
import java.util.*;

/**
 * This class is used for reading in data for the backend
 *
 */
public class BackEndDataReader extends DataReader {

    private final String GAMEVERSION_RESOURCES = "game_version";
    private final String GAMEVERSION_INFORMATION_RESOURCES = "information.properties";
    private final String GAMEVERSION_PATH_RESOURCES = "paths.properties";
    private final String BANK_NAME = "BANK";
    private final String INFORMATION_KEY = "SUBDIRECTORIES";
    private static final String SEPARATOR = ",";
    private static final String BAD_DATA_FILE_MESSAGE = "INCORRECT VALUE IN DATAFILE: ";
    private static final String MISSING_FILE_MESSAGE = "FILE NOT FOUND";
    private static final String GO_KEY = "GO";
    private static final String JAIL_KEY = "JAIL";
    private static final String FREE_PARKING_KEY = "FREE_PARKING";
    private static final String MONEY_KEY = "GO_MONEY";
    private static final String RULES_KEY = "RULES";
    private static final String BOARD_INFORMATION_NAME = "BOARD";
    private static final String PLAYER_INFORMATION_NAME = "PLAYER";
    private static final String RULES_INFORMATION_NAME = "RULES";
    private static final String CARDS_INFORMATION_NAME = "CARDS";
    private static final String CARD_DECKTYPE_KEY = "DECK";
    private static final String CARD_CONFIGURATIONS_KEY = "CONFIGURATIONS";
    private static final String CARD_NUMBER_KEY = "NUMBER";
    private static final String CARD_TYPE_KEY = "TYPE";

    private List<Location> myLocations;
    private List<Player> myPlayers;
    private List<Die> myDice;
    private List<Rule> myRules;
    private Player myBank;
    private Board myBoard;
    private LocationFactory myLocationFactory = new LocationFactory();
    private PlayerFactory myPlayerFactory = new PlayerFactory();
    private DieFactory myDieFactory = new DieFactory();
    private RuleFactory myRuleFactory = new RuleFactory();
    private CardFactory myCardFactory = new CardFactory();

    private Properties rules;
    private Properties tokens;

    public BackEndDataReader(){
        myLocations = new ArrayList<>();
        myPlayers = new ArrayList<>();
    }

    /**
     * Reads in all game files
     * @param gameVersion A String representing the file path to a folder for a game
     * @throws InvalidDataFileValueException Thrown if folder does not contain all necessary subfolders and files
     */
    public void readAllGameFiles(String gameVersion) throws InvalidDataFileValueException {
        File gameVersionFolder = super.getFile(GAMEVERSION_RESOURCES);
        try {
            Map<String, Properties> allProperties = super.getPropertiesForAllFilesInFolder(gameVersionFolder);
            String[] allInformation = allProperties.get(GAMEVERSION_INFORMATION_RESOURCES).getProperty(INFORMATION_KEY).split(super.getSEPARATOR());
            Properties allPaths = allProperties.get(GAMEVERSION_PATH_RESOURCES);
            createBank();
            for (String information: allInformation) {
                String path = allPaths.getProperty(information);
                String filePath = gameVersion + path;
                switch (information) {
                    case BOARD_INFORMATION_NAME:
                        System.out.println("The file path is : " + filePath);
                        readLocationFiles(filePath);
                        break;
                    case PLAYER_INFORMATION_NAME:
                        readPlayerFiles(filePath);
                        break;
                    case RULES_INFORMATION_NAME:
                        readRulesFile(filePath);
                        break;
                    case CARDS_INFORMATION_NAME:
                        readCardsFile(filePath);
                        break;
                }
            }
        } catch (IOException e) {
            throw new IllegalArgumentException(MISSING_FILE_MESSAGE);
        }
    }

    private void readPlayerFiles(String playerInformationResources) throws IOException {
        for (Properties properties : super.getPropertiesForAllFilesInFolder(new File(playerInformationResources)).values()){
            Player temp = myPlayerFactory.create(properties);
            myPlayers.add(temp);

            for (String p : ((String) properties.get("PROPERTIES")).split(",")) {
                for (Location l : myLocations) {
                    if (l.getName().equals(p)) {
                        ((PropertySpace) l).changeOwner(temp);
                    }
                }
            }
        }
    }

    /**
     * Gets the properties file mapping unique ID of players to token image file name
     * @param gameVersion A String representing the file path to a folder for a game
     * @return returns a Properties object mapping unique ID to token image
     * @throws IOException Thrown if the gameVersion does not contain a tokens properties file
     */
    public Properties getTokens(String gameVersion) throws IOException {
        tokens = new Properties();
        for (Properties properties : super.getPropertiesForAllFilesInFolder(new File (gameVersion)).values()) {
            tokens.putAll(properties);
        }
        return tokens;
    }

    private void readLocationFiles(String locationInformationResources) throws IOException, InvalidDataFileValueException {
        for (Properties properties : super.getPropertiesForAllFilesInFolder(new File(locationInformationResources)).values()){
            Location location = myLocationFactory.create(properties, myBank);
            myLocations.add(location);
        }
    }

    public List<Location> getLocations() {
        Board b = new Board(myLocations);
        return b.getAllLocations();
    }

    public List<Player> getPlayers(){
        return myPlayers;
    }

    public Player getBank(){
        return myBank;
    }

    public Board getBoard(){
        return myBoard;
    }

    private void createBank(){
        myBank = new Bank(BANK_NAME);
    }

    public List<Rule> getRules(){
        return myRules;
    }

    public Properties getRulesFile() {
        return rules;
    }

    public List<Die> getDice() {
        return myDice;
    }

    private void readRulesFile(String rulesInformationResources) throws IOException, InvalidDataFileValueException {
        for (Properties properties : super.getPropertiesForAllFilesInFolder(new File(rulesInformationResources)).values()) {
            addDice(properties, rulesInformationResources);
            addRules(properties, rulesInformationResources);
            rules = properties; // TODO error check to make sure only one properties file is entered for game setup
        }
    }

    private void addDice(Properties properties, String rulesInformationResources) throws InvalidDataFileValueException {
        try {
            myDice = new ArrayList<>();
            for (int dieNumber = 0; dieNumber < Integer.parseInt(properties.getProperty("NUMBER_OF_DICE")); dieNumber++) {
                myDice.add(myDieFactory.create("NSidedDie", Integer.parseInt(properties.getProperty("NUMBER_OF_DICE_SIDES"))));
            }
        } catch (IllegalArgumentException e){
            throw new InvalidDataFileValueException(BAD_DATA_FILE_MESSAGE + rulesInformationResources);
        }
    }

    private void addRules(Properties properties, String rulesInformationResources) throws InvalidDataFileValueException {
        myRules = new ArrayList<>();
        try {
            String[] ruleNames = properties.getProperty(RULES_KEY).split(SEPARATOR);
            for (String ruleName : ruleNames) {
                myRules.add(myRuleFactory.create(ruleName));
            }
            configureBoardWithRuleData(properties, rulesInformationResources);
        } catch (IllegalArgumentException e){
            throw new InvalidDataFileValueException(BAD_DATA_FILE_MESSAGE + rulesInformationResources);
        }
    }

    private void configureBoardWithRuleData(Properties properties, String rulesInformationResources) throws InvalidDataFileValueException {
        try{
        myBoard = new Board(myLocations);
        myBoard.setOfficialGo(Integer.parseInt(properties.getProperty(GO_KEY)));
        myBoard.setOfficialJail(Integer.parseInt(properties.getProperty(JAIL_KEY)));
        myBoard.setOfficialFreeParking(Integer.parseInt(properties.getProperty(FREE_PARKING_KEY)));
        MoneySpace goSpace = new MoneySpace(myBoard.getLocation(myBoard.getOfficialGoIndex()).getName(),
                myBoard.getOfficialGoIndex(), (Integer.parseInt(properties.getProperty(MONEY_KEY))));
        myBoard.deleteAtPosition(myBoard.getOfficialGoIndex());
        myBoard.insertAtPosition(goSpace, myBoard.getOfficialGoIndex());

        } catch (IllegalArgumentException e){
            throw new InvalidDataFileValueException(BAD_DATA_FILE_MESSAGE + rulesInformationResources);
        }
    }

    private void readCardsFile(String cardInformationResources) throws InvalidDataFileValueException, IOException {
        List<Properties> allCardProperties = new ArrayList<>(super.getPropertiesForAllFilesInFolder(new File(cardInformationResources)).values());
        Collections.shuffle(allCardProperties);
        for (Properties properties : allCardProperties) {
            try {
                String cardType = properties.getProperty(CARD_TYPE_KEY);
                int[] configurations = Arrays.stream(properties.getProperty(CARD_CONFIGURATIONS_KEY).split(",")).
                        mapToInt(Integer::parseInt).toArray();
                Card newCard = myCardFactory.create(cardType, Integer.parseInt(properties.getProperty(CARD_NUMBER_KEY)), configurations,
                        properties.getProperty(CARD_DECKTYPE_KEY));
                myBank.addCard(newCard);
            } catch (NullPointerException e){
                throw new InvalidDataFileValueException(BAD_DATA_FILE_MESSAGE + cardInformationResources);
            }
        }
    }
}
