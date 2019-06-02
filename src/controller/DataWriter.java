package controller;

import controller.exceptions.MissingValueException;
import model.card.Card;
import model.location.CardSpace;
import model.location.Location;
import model.location.MoneySpace;
import model.location.TeleportSpace;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import java.io.*;
import java.util.*;

/**
 * This class is used for writing all data to file
 */
public class DataWriter{

    private static final String FILE_ENDING = ".properties";
    private static final String PATH_SEPERATOR = "/";
    private static final String CARD_FRONT_END_DATA = "cardsFrontEnd" + PATH_SEPERATOR + "cards";
    private static final String RULES = "rules" + PATH_SEPERATOR + "rules";
    private Properties myProp;
    private Properties myRules;
    private Date myDate;
    private String timestamp;
    private static final String HumanPlayer = "model.player.HumanPlayer";
    private static final String Bank = "model.player.Bank";
    private String myPath;
    private Map<String, String> tokens;


    public DataWriter(String writeToPath) {
        myPath = writeToPath;
        myRules = new Properties();
        myDate = new Date(System.currentTimeMillis());
        timestamp = join(myDate.toString().split(" "), "");
        tokens = new HashMap<>();
    }

    /**
     * Writes a new Player of a game
     * @param name a String representing the name of the player
     * @param type a String representing the type of player
     * @param money an int representing the amount of money a player has
     * @param token a String representing the file name of a token image that the player has chosen to use
     * @throws MissingValueException thrown when a null is found
     */
    public void newGamePlayers(String name, String type, int money, String token) throws MissingValueException {
        myProp = new Properties();
        String ID = makeID();
        
        try {
            myProp.setProperty("NAME", name.toUpperCase());
            myProp.setProperty("UNIQUE_ID", ID);
            myProp.setProperty("TYPE", type.toUpperCase());
            myProp.setProperty("STARTING_SPACE", 0 + "");
            myProp.setProperty("MONEY", money + "");
            myProp.setProperty("PROPERTIES", "");
            myProp.setProperty("CARDS", "");
            myProp.setProperty("TOKEN", token);
            writeFile("player" + PATH_SEPERATOR + ID);
        } catch (NullPointerException e){
            throw new MissingValueException("PLEASE ENTER VALUE");
        }

        tokens.put(ID, token);
    }

    /**
     * Writes a file mapping players in the game to their tokens
     * @param gamePath a string representing the folder of the game
     * @throws IOException
     */
    public void writeTokens(String gamePath) throws IOException{
        File file = new File(gamePath);
        FileInputStream fileInput = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(fileInput);
        myProp = new Properties();
        myProp.putAll(properties);
        writeFile("gameTokens");
    }

    public void writeTokens() {
        myProp = new Properties();
        myProp.putAll(tokens);

        writeFile("gameTokens");
    }

    /**
     * Writes a player file from an already constructed Player object
     * @param toBeWritten a Player object that is to be written
     */
    public void makePlayerFile(Player toBeWritten) {
        myProp = new Properties();

        String type = toBeWritten.getClass().toString();
        String[] classPath = (type.split(" ")[1]).split("\\.");
        type = classPath[classPath.length - 1].toUpperCase();

        String props = "";
        for (PropertySpace p : toBeWritten.getProperties()) {
            if (props.equals("")) {
                props = p.getName();
            } else {
                props = props + "," + p.getName();
            }
        }

        myProp.setProperty("NAME", toBeWritten.getName());
        myProp.setProperty("UNIQUE_ID", toBeWritten.getUniqueID());
        myProp.setProperty("TYPE", type);
        try {
            myProp.setProperty("STARTING_SPACE", toBeWritten.getCurrentLocation().getLocationNumber() + "");
        } catch (NullPointerException e) {
            myProp.setProperty("STARTING_SPACE", 0 + "");
        }
        myProp.setProperty("MONEY", toBeWritten.getMoneyBalance() + "");
        myProp.setProperty("PROPERTIES", props);
        myProp.setProperty("CARDS", ""); //TODO: DECIDE ON FORMAT OF CARDS IN DATA
        writeFile("player" + PATH_SEPERATOR + toBeWritten.getUniqueID());
    }

    /**
     * Writes a location file from an already constructed location
     * @param location a Location object that is to be written
     */
    public void makeLocationFile(Location location) {
        myProp = new Properties();
        String filename = join(location.getName().toLowerCase().split(" "), "_");
        String type = location.getClass().toString();
        String[] classPath = (type.split(" ")[1]).split("\\.");
        type = classPath[classPath.length - 1].toUpperCase();

        if (type.equalsIgnoreCase("REALESTATE") || type.equalsIgnoreCase("UTILITY") ||
                type.equalsIgnoreCase("RAILROAD")) {
            PropertySpace property = (PropertySpace) location;
            type = classPath[classPath.length - 2];
            type = type.substring(0, 1).toUpperCase() + type.substring(1);
            myProp.setProperty("TYPE", type);
            String propertyType = classPath[classPath.length - 1];
            myProp.setProperty("PROPERTY_TYPE", propertyType);
            myProp.setProperty("COST", (property.getCost() + ""));
            myProp.setProperty("GROUP", property.getGroup());
            myProp.setProperty("BUILDING_COST", property.getBuildingCost() + "");
            myProp.setProperty("ISMORTGAGED", property.isMortgaged() + "");
            myProp.setProperty("HOUSES", property.getNumberOfHouses() + "");
            myProp.setProperty("NUMBER_PROPERTIES_IN_GROUP", property.getNumberPropertiesInGroup() + "");

            String rents = property.getRentByHouses(0) + "";
            for (int i = 1; i <= property.getTopNumberOfHouses(); i ++) {
                rents = rents + "," + property.getRentByHouses(i);
            }
            myProp.setProperty("RENT", rents);
        } else {
            type = classPath[classPath.length - 1].toUpperCase();
            myProp.setProperty("TYPE", type);
            if (type.equalsIgnoreCase("TELEPORTSPACE")) {
                TeleportSpace teleport = (TeleportSpace) location;
                myProp.setProperty("DESTINATION", teleport.getMyLocationToTeleportPlayerTo() + "");
            } else if (type.equalsIgnoreCase("MONEYSPACE")) {
                MoneySpace money = (MoneySpace) location;
                myProp.setProperty("AMOUNT_MONEY_GIVEN", money.getMyAmountToGet() + "");
            } else if (type.equalsIgnoreCase("CARDSPACE")) {
                myProp.setProperty("DECK", ((CardSpace)location).getCardType());
                filename = filename + location.getLocationNumber();
            }
        }
        myProp.setProperty("NAME", location.getName());
        myProp.setProperty("LOCATION_NUMBER", location.getLocationNumber() + "");

        writeFile("board" + PATH_SEPERATOR + filename);
    }

    /**
     * Writes rules to file
     * @param temp a Properties file representing the defaults for a game
     * @param diceNum an int representing the number of dice
     * @param sideNum an int representing the number of sides on each die
     * @param goMon an int representing the amount of money from passing go
     * @param rules a List of Strings for each rule a user wants to use
     * @throws MissingValueException throws when a null is passed through
     */
    public void makeRules(Properties temp, int diceNum, int sideNum, int goMon, List<String> rules) throws MissingValueException {
        try {
            myProp = new Properties();
            myProp.setProperty("GO", temp.get("GO").toString());
            myProp.setProperty("JAIL", temp.get("JAIL").toString());
            myProp.setProperty("FREE_PARKING", temp.get("FREE_PARKING").toString());
            myProp.setProperty("NUMBER_OF_DICE", diceNum + "");
            myProp.setProperty("NUMBER_OF_DICE_SIDES", sideNum + "");
            myProp.setProperty("GO_MONEY", goMon + "");
            String rule = "";
            for (String s : rules) {
                if (rule.equals("")) {
                    rule = join(s.split(" "), "") + "Rule";
                } else {
                    rule = rule + "," + join(s.split(" "), "") + "Rule";
                }
            }
            myProp.setProperty("RULES", rule);
            writeFile("rules" +PATH_SEPERATOR + "rules");
        }  catch (NullPointerException e) {
            throw new MissingValueException("PLEASE ENTER VALUE");
        }

    }

    /**
     * Updates the rules file by copying a new Properties file
     * @param rule a Properties object containing rules
     */
    public void updateRules(Properties rule) {
        myProp = new Properties();
        myProp.putAll(rule);
        writeFile("rules" + PATH_SEPERATOR + "rules");
    }

    private void writeFile(String fileName) {
        makePath("player");
        makePath("board");
        makePath("rules");
        makePath("cards");
        makePath("cardsFrontEnd");
        File writing = new File(myPath + PATH_SEPERATOR + timestamp + PATH_SEPERATOR + fileName + FILE_ENDING);
        try (FileWriter out = new FileWriter(writing)) {
            myProp.store(out, null);
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    /**
     * Writes card files
     * @param cards a Collection of Card objects to be written
     * @param gameVersion a String representing the game file
     * @throws IOException
     */
    public void writeCardFiles(Collection<Card> cards, String gameVersion) throws IOException {
        myProp = new Properties();
        for (Card card : cards){
            String[] classPath = (card.getClass().toString().split(" ")[1]).split("\\.");
            myProp.setProperty("NUMBER", Integer.toString(card.getNumber()));
            myProp.setProperty("DECK", card.getDeckType());
            String[] configurations = Arrays.stream(card.getConfigurations()).mapToObj(String::valueOf).toArray(String[]::new);
            myProp.setProperty("CONFIGURATIONS", String.join(",", configurations));
            myProp.setProperty("TYPE", classPath[classPath.length - 1]);
            String fileName = card.getDeckType().toLowerCase() + card.getNumber();
            writeFile("cards" + PATH_SEPERATOR + fileName);
        }
        myProp.clear();
        copyCardFrontEnd(gameVersion);
    }

    private void copyCardFrontEnd(String gameVersion) throws IOException {
        File cardFrontEnd = new File(gameVersion + CARD_FRONT_END_DATA + FILE_ENDING);
        myProp.load(new FileReader(cardFrontEnd));
        writeFile(CARD_FRONT_END_DATA);
    }

    /**
     * Copies rules from a game file
     * @param gameVersion a String representing a game folder
     * @throws IOException
     */
    public void copyRules(String gameVersion) throws IOException {
        myProp.clear();
        File rules = new File(gameVersion + RULES + FILE_ENDING);
        myProp.load(new FileReader(rules));
        writeFile(RULES);
    }

    private String makePath(String subFolder){
        String folder = myPath + PATH_SEPERATOR + timestamp + PATH_SEPERATOR + subFolder;
        File file = new File(folder);
        if (!file.exists()) {
            file.mkdirs();
        }
        return folder;
    }

    private String join(String[] myArray, String delimiter) {
        String joined = "";
        for (String s : myArray) {
            if (joined.equals("")) {
                joined = joined + s;
            } else {
                joined = joined + delimiter + s;
            }
        }
        return joined;
    }

    private String makeID() {
        return UUID.randomUUID().toString();
    }

    public String getTimestamp() {
        return timestamp;
    }

}
