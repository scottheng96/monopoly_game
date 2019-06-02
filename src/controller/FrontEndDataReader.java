package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import javafx.scene.image.Image;

/**
 * This class is used by the front end for all data reading
 */
public class FrontEndDataReader extends DataReader{
    private final String PICTURE_RESOURCES = "picture";
    private final String SPLASH_SCREEN_RESOURCES = "splash_screen_pictures.properties";
    private final String GAME_VERSIONS = "GAME_VERSIONS";
    private final String TOKEN_PROPERTY_FILE = "/gameTokens.properties";
    private static final String PATH_CARDS = "cardsFrontEnd/cards.properties";
    private final String RULE_FILE = "/rules/rules.properties";
    private Map<String, Image> myGameVersions;
    private Map<String, Image> playerTokensMap;
    private Map<String, String> myCardTexts;
    private Map<String, String> optimalRulesMap;

    public FrontEndDataReader() throws IOException{
        myCardTexts = new HashMap<>();
        myGameVersions = new HashMap<>();
        readSplashScreenData();
    }

    /**
     * Reads all front end data
     * @param gamePath A string representing the game folder
     * @throws IOException
     */
    public void readAllFrontEndData(String gamePath) throws IOException {
        readCardTextData(gamePath);
        readPlayerTokenData(gamePath);
    }

    /**
     * Reads rule data from a game folder
     * @param gameVersion a String representing the game folder
     * @throws IOException
     */
    public void readOptimalRulesData(String gameVersion) throws IOException{
        optimalRulesMap = new HashMap<>();
        File optimalRulesFile = super.getFile(gameVersion+RULE_FILE);
        optimalRulesMap = readInputStream(optimalRulesFile);
    }

    /**
     * Reads the token data for each player
     * @param gamePath a String representing the folder of a game
     * @throws IOException
     */
    public void readPlayerTokenData(String gamePath) throws IOException{
        playerTokensMap = new HashMap<>();
        File tokenFile = super.getFile(gamePath + TOKEN_PROPERTY_FILE);
        FileInputStream fileInput = new FileInputStream(tokenFile);
        Properties properties = new Properties();
        properties.load(fileInput);
        for (Object key: properties.keySet()) {
            Image onePlayerToken = new Image(this.getClass().getClassLoader().getResourceAsStream((String)properties.get(key)));
            playerTokensMap.put((String) key, onePlayerToken);
        }
    }

    /**
     * Reads the splash screen data
     * @throws IOException
     */
    public void readSplashScreenData() throws IOException {
        File pictures = super.getFile(PICTURE_RESOURCES);
        Properties picturePaths = super.getPropertiesForAllFilesInFolder(pictures).get(SPLASH_SCREEN_RESOURCES);
        myGameVersions = new HashMap<>();

        for (String gameVersion: picturePaths.getProperty(GAME_VERSIONS).split(super.getSEPARATOR())) {
            myGameVersions.put(gameVersion, new Image(this.getClass()
                            .getClassLoader()
                            .getResourceAsStream(picturePaths.getProperty(gameVersion))));
        }
    }

    private void readCardTextData(String gameVersion) throws IOException {
        myCardTexts = new HashMap<>();
        File cardsFrontEnd = new File(gameVersion + PATH_CARDS);
        myCardTexts = readInputStream(cardsFrontEnd);
    }

    private HashMap<String, String> readInputStream(File file) throws IOException {
        HashMap<String, String> oneMap = new HashMap<>();
        FileInputStream fileInput = new FileInputStream(file);
        Properties properties = new Properties();
        properties.load(fileInput);
        for (Object key: properties.keySet()) {
            oneMap.put((String)key,(String)properties.get(key));
        }
        return oneMap;
    }


    public Map<String, Image> getGameVersions() {
        return myGameVersions;
    }

    public Map<String, String> getMyCardTexts() {return myCardTexts;}

    public Map<String, Image> getPlayerTokensMap() {return playerTokensMap;}

    public Map<String, String> getOptimalRulesMap() {return optimalRulesMap;}

}