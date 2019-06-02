package controller;

import controller.exceptions.InvalidDataFileValueException;
import controller.exceptions.MissingValueException;
import controller.exceptions.PropertyManagementException;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.DirectoryChooser;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import model.Game;
import model.GameStatus;
import model.location.Location;
import model.player.Player;
import view.DiceView;
import view.panel.GameButtonControlPanel;
import view.panel.ManagementPanel;
import view.panel.TradePanel;
import view.panel.AuctionPanel;
import view.screen.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import static model.player.PlayerState.*;


/**
 * This class is used to control the game screens and button functionality, therefore, it acts as an intermediary between the model and the view
 */
public class Controller {

    public static final String SCENE_LOOKUP_TAG = "#";
    public static final String ROLL_BUTTON_ID = "rollCommand";
    public static final String END_TURN_BUTTON_ID = "endTurnCommand";
    public static final String MANAGEMENT_BUTTON_ID = "managementCommand";
    public static final String TRADE_BUTTON_ID = "tradeCommand";
    public static final String AUCTION_BUTTON_ID = "auctionCommand";
    public static final String PROPOSE_TRADE_BUTTON_ID = "proposeTradeCommand";
    public static final String BEGIN_AUCTION_BUTTON_ID = "beginAuctionCommand";
    private static final String GAME_TOKEN_PROPERTIES = "/gameTokens.properties";
    private static final String SAVE_BUTTON_ID = "saveCommand";
    private static final int SCENE_WIDTH = 1400;
    private static final int SCENE_HEIGHT = 1000;
    private static final Color BACKGROUND = Color.LIGHTSKYBLUE;
    private static final int DICE_SCREEN_EXIST_SECONDS = 3;
    private static final String RESTART_KEY = "r";
    private ResourceBundle myButtonNameResources = ResourceBundle.getBundle("button_text");
    private Group myRoot;
    private Scene myScene;
    private Game myGame;

    private GameButtonControlPanel myControlPanel;
    private ManagementPanel myManagementPanel;
    private TradePanel myTradePanel;
    private AuctionPanel myAuctionPanel;
    private BackEndDataReader tempReader;
    private String myLoadPath;
    private WelcomeScreen myWelcomeScreen;
    private PlayerSetUpScreen myPlayerScreen;
    private GameVersionChosenScreen myGameVersionScreen;
    private RuleSetUpScreen myRuleScreen;
    private WinGameScreen myWinGameScene;
    private GameListener myGameListener;
    private GameSceneView myGameScene;
    private DataWriter myWriter;

    private static final String GAME_PATH = "./resources/loadable_games";
    private Location myTemporaryLocation;
    private Properties myTemporaryRule;
    private final Map<String, ImageView> playerTokens = new HashMap<>();
    private Button mySaveButton;
    private FrontEndDataReader myFrontEndDataReader;

    /**
     * Constructs a new Controller object
     */
    public Controller() {
        setUp();
    }

    private void setUp() {
        myRoot = new Group();
        myScene = new Scene(myRoot, SCENE_WIDTH, SCENE_HEIGHT, BACKGROUND);
        startSpashScreens();
    }

    public Scene getScene(){
        return myScene;
    }

    private void startSpashScreens(){
        myWelcomeScreen = new WelcomeScreen(myRoot, SCENE_WIDTH, SCENE_HEIGHT, myFrontEndDataReader);
        myWriter = new DataWriter(GAME_PATH);
        try {
            myFrontEndDataReader = new FrontEndDataReader();
        } catch (IOException e) {
            showError(e.getMessage());
        }
        setSplashScreenActions();
    }

    private void copyGameVersionFiles(String gameVersion){
        try {
            tempReader = new BackEndDataReader();
            tempReader.readAllGameFiles(gameVersion);
            myTemporaryLocation = tempReader.getLocations().get(0);
            myTemporaryRule = tempReader.getRulesFile();
            myWriter.writeCardFiles(tempReader.getBank().getCards(), gameVersion);
        } catch (InvalidDataFileValueException | IOException invalid) {
            showError(invalid.getMessage());
        }
    }

    private void setSplashScreenActions() {
        myWelcomeScreen.getWelcomeReadyButton().setOnAction((ActionEvent event) -> {
            switchToGameVersionScreen();
            myGameVersionScreen.getReadyButton().setOnAction((ActionEvent readyEvent) -> {
                switchToRuleScreen();
                myRuleScreen.getRuleNextButton().setOnAction((ActionEvent nextnextEvent) -> {
                    copyGameVersionFiles(myGameVersionScreen.getGameVersion());
                    writeLocationFiles(myTemporaryLocation);
                    switchToPlayerScreen();
                });
            });
        });


        myWelcomeScreen.getWelcomeLoadButton().setOnAction((ActionEvent event) -> {
            Node node = (Node) event.getSource();
            DirectoryChooser directoryChooser = new DirectoryChooser();
            File selectedDirectory = directoryChooser.showDialog(node.getScene().getWindow());
            if (selectedDirectory != null) {
                myLoadPath = selectedDirectory.getAbsolutePath();
                int startingIndex = myLoadPath.indexOf("resource");
                myLoadPath = myLoadPath.substring(startingIndex - 1);
                myLoadPath = "." + myLoadPath;
                System.out.println(myLoadPath);
                myRoot.getChildren().clear();
                loadGame(myLoadPath);
            }
        });
    }

    private void switchToGameVersionScreen(){
        myRoot.getChildren().clear();
        myGameVersionScreen = new GameVersionChosenScreen(myRoot, SCENE_WIDTH, SCENE_HEIGHT, myFrontEndDataReader);
    }

    private void switchToRuleScreen(){
        try { myFrontEndDataReader.readOptimalRulesData(myGameVersionScreen.getGameVersion()); } catch (IOException e) { e.printStackTrace(); }
        myRoot.getChildren().clear();
        myRuleScreen = new RuleSetUpScreen(myRoot, SCENE_WIDTH, SCENE_HEIGHT, myFrontEndDataReader);
    }

    private void switchToPlayerScreen(){
        myRoot.getChildren().clear();
        myPlayerScreen = new PlayerSetUpScreen(myRoot, SCENE_WIDTH, SCENE_HEIGHT, myFrontEndDataReader);
        myPlayerScreen.getPlayerSetUpScreenButton().setOnAction((ActionEvent makePlayerScreen) -> {
            makeAnotherPlayerScreen();
        });
    }

    private void makeAnotherPlayerScreen(){
        myRoot.getChildren().clear();
        myPlayerScreen.setNewPlayerScreen();
        myPlayerScreen.getNextPlayerScreenButton().setOnAction((ActionEvent makeAnotherScreen) ->{
        myPlayerScreen.setTokenAlreadyChosen();
        if (myPlayerScreen.getPlayerCount() == myPlayerScreen.getPlayerNumber()) {
            createPlayerDataFromFrontScreen();
            try {
                createRuleDataFromScreen();
                myRoot.getChildren().clear();
                myWriter.writeTokens();
                myGame = new Game(GAME_PATH + "/" + myWriter.getTimestamp() + "/");
                setUpGameView(GAME_PATH + "/" + myWriter.getTimestamp() + "/");
            } catch (InvalidDataFileValueException | IOException | MissingValueException invalid){
                System.out.println("SOMETHING WRONG WITH GAME FILES: " + invalid.getMessage());
                showError(invalid.getMessage());
            }
        }
        else {
            createPlayerDataFromFrontScreen();
            makeAnotherPlayerScreen();
            }
        });
    }

    private void setUpGameView(String gamePath){
        try {
            myFrontEndDataReader = new FrontEndDataReader();
            myFrontEndDataReader.readAllFrontEndData(gamePath);
        } catch (IOException e) {
            System.out.println("INVALID GAME VERSION PROCESSED");
        }
        myGameScene = new GameSceneView(myScene, myRoot, SCENE_WIDTH, SCENE_HEIGHT, myGame.getBoard(),
                myGame.getMyPlayers(), myFrontEndDataReader);
        myControlPanel = new GameButtonControlPanel(myRoot, myScene);
        try {
            myManagementPanel = new ManagementPanel();
        } catch (PropertyManagementException e){
            showError(e.getMessage());
        }
        myTradePanel = new TradePanel();
        myAuctionPanel = new AuctionPanel();
        setSaveButton();
        myGameListener = new GameListener(myGame, new ArrayList<>(Arrays.asList(myControlPanel,
                myManagementPanel, myTradePanel, myAuctionPanel)), myGameScene, myFrontEndDataReader);
        myGameListener.addAllListeners();
        setMenuButtonActions();
        addEndGameListener();
        myControlPanel.disableAllButtons();
        setUpGameInteractionPanelButtons();
    }

    private void setUpGameInteractionPanelButtons(){
        myTradePanel.getButtonByID(PROPOSE_TRADE_BUTTON_ID).setOnAction((ActionEvent proposeTrade) ->{
            myTradePanel.setUpTradeUsingChosenData();
            myGame.makeNewTurn(myTradePanel.getRecipient());
            myTradePanel.close();
        });
        myAuctionPanel.getButtonByID(BEGIN_AUCTION_BUTTON_ID).setOnAction((auction) ->{
            myGame.startAuction(myGame.getCurrentPlayer(), myAuctionPanel.getAuctionProperty());
            myAuctionPanel.close();
        });
    }

    private void showError (String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
        restart();
    }

    private void setMenuButtonActions(){
        Map<String, EventHandler<ActionEvent>> actions = new HashMap<>();
        actions.put(ROLL_BUTTON_ID, (ActionEvent roll) -> {
            myGame.rollToMove();
            setUpDiceView(myGame.getMyRoll().getAllRolls());
            myGame.getCurrentPlayer().executeStateChange(myGame.getMyBank(), myGame.getMyRoll());
        });
        actions.put(END_TURN_BUTTON_ID, (ActionEvent endTurn) ->{
            myGame.endTurn();
        });
        actions.put(MANAGEMENT_BUTTON_ID, (ActionEvent showManagementPanel) ->{
            myManagementPanel.show();
        });
        actions.put(TRADE_BUTTON_ID, (ActionEvent showTradePanel) -> {
            myTradePanel.show();
        });
        actions.put(AUCTION_BUTTON_ID, (ActionEvent showAuctionPanel) ->{
            myAuctionPanel.show();
        });
        for (Map.Entry<String, EventHandler<ActionEvent>> entry : actions.entrySet()) {
            myControlPanel.getButtonByID(entry.getKey()).setOnAction(entry.getValue());
        }
    }

    private void setUpDiceView(List<Integer> allRolls) {
        DiceView myDiceView = new DiceView(allRolls);
        PauseTransition delay = new PauseTransition(Duration.seconds(DICE_SCREEN_EXIST_SECONDS));
        delay.setOnFinished(event -> myDiceView.getStage().close());
        delay.play();
    }

    private void writeLocationFiles(Location location) {
        Location temp = location.getNextLocation();
        myWriter.makeLocationFile(temp);
        temp = temp.getNextLocation();
        while (!temp.getPreviousLocation().equals(location)) {
            myWriter.makeLocationFile(temp);
            temp = temp.getNextLocation();
        }
    }

    private void createRuleDataFromScreen() throws IOException, MissingValueException {
        if (myRuleScreen.useOfficialRules()){
            myWriter.copyRules(myGameVersionScreen.getGameVersion());
        }
        else {
            List<String> listOfRules = myRuleScreen.getCheckedRuleNames();
            listOfRules.add(myRuleScreen.getWinRule());
            myWriter.makeRules(myTemporaryRule, myRuleScreen.getDiceNum(), myRuleScreen.getDiceSide(), myRuleScreen.getGoMon(), listOfRules);
        }
    }

    /**
     * Handles user input for cheat keys and starting the game
     * @param code User input from the key board
     */
    public void handleKeyInput(KeyCode code) {
        if (myGame != null) {
            if (myGame.getGameStatus().equals(GameStatus.PLAYING)) {
                if (code.isDigitKey()) {
                    myGame.getCurrentPlayer().changeState(ROLLING);
                    myGame.checkRules();
                    Location toMoveTo = myGame.getCurrentPlayer().getCurrentLocation().getForwardLocation(Integer.parseInt(code.getChar()));
                    myGame.getCurrentPlayer().moveTo(toMoveTo);
                    myGame.checkRules();
                    myGame.getCurrentPlayer().executeStateChange(myGame.getMyBank(), myGame.getMyRoll());
                }
            } else if (myGame.getGameStatus().equals(GameStatus.NOT_STARTED)) {
                myGame.startGame();
                myGameScene.removeHintText();
            }
        }
        if (code.getChar().equalsIgnoreCase(RESTART_KEY)){
            restart();
        }
    }

    private void saveGame() {
        String time = myWriter.getTimestamp();
        myWriter = new DataWriter(GAME_PATH);
        copyGameVersionFiles(GAME_PATH + "/" + time + "/");
        writeLocationFiles(myGame.getBoard().getLocation(0));
        myWriter.updateRules(myTemporaryRule);
        for (Player player : myGame.getMyPlayers()) {
            myWriter.makePlayerFile(player);
        }
        try { myWriter.writeTokens(GAME_PATH + "/" + time + GAME_TOKEN_PROPERTIES); } catch (IOException e) { e.printStackTrace(); }
    }

    private void createPlayerDataFromFrontScreen(){
        try {
            ResourceBundle myTokenTypes = ResourceBundle.getBundle("tokenTypes");
            String playerTokenFileName = myTokenTypes.getString("token" + myPlayerScreen.getTokenChoiceIndex());
            myWriter.newGamePlayers(myPlayerScreen.getPlayerName(), myPlayerScreen.getType(), myPlayerScreen.getMoney(), playerTokenFileName);
             playerTokens.put(myPlayerScreen.getPlayerName().toUpperCase(), myPlayerScreen.getSelectedTokenView());

        } catch (MissingValueException noValueOnFrontEnd){
            System.out.println(noValueOnFrontEnd.getMessage());
            showError(noValueOnFrontEnd.getMessage());
        }
    }

    private void loadGame(String gamePath) {
        try {
            myGame = new Game(gamePath + "/");
            setUpGameView(gamePath + "/");
        } catch (InvalidDataFileValueException invalid) {
            System.out.println("SOMETHING WRONG WITH GAME FILES: " + invalid.getMessage());
            showError(invalid.getMessage());
        }
    }

    private void setSaveButton(){
        mySaveButton = new Button();
        mySaveButton.setId(SAVE_BUTTON_ID);
        mySaveButton.setText(myButtonNameResources.getString(SAVE_BUTTON_ID));
        mySaveButton.relocate(SCENE_WIDTH - 2 * 100, 100.0 / 5);
        myRoot.getChildren().add(mySaveButton);
        mySaveButton.setOnAction((save)->{
            saveGame();
        });
    }

    private void addEndGameListener() {
        myGame.getMyGameStatus().addListener((endGameProperty, oldGameStatus, newGameStatus)-> {
            if(newGameStatus.equals(myGame.getGameStatus().GAME_OVER)) {
                myRoot.getChildren().clear();
                myWinGameScene = new WinGameScreen(myRoot, SCENE_WIDTH,SCENE_HEIGHT, myGame.getMyPlayers(),myFrontEndDataReader);
                myWinGameScene.getNewGameButton().setOnAction((ActionEvent winGame) -> {
                    restart();
                });
            }
        });
    }

    private void restart(){
        myRoot.getChildren().clear();
        startSpashScreens();
    }
}