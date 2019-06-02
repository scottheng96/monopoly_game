package view.screen;

import controller.FrontEndDataReader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.Board;
import model.location.Location;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import view.BoardPane;
import view.BoardToolTip;
import view.BuildingManager;
import view.tilepane.PlayerTabPane;

import java.util.*;

/**
 * To set up and update the game view (including board, player tokens, ect.) during the game
 */
public class GameSceneView extends AbstractScene {

    private static final double BOARD_SIZE = 450;
    private static final double BUFFER = 50;
    private static final String HINT = "PRESS ANY BUTTON TO START THE GAME";
    private static final int SIZE = 40;
    private static final String FONT = "Verdana";
    private static final Color RECTANGLE_COLOR = Color.LIGHTGRAY;
    private int numberPerSide;
    private double myTileWidth;
    private double myTileHeight;
    private Group myRoot;
    private double mySceneWidth;
    private double mySceneHeight;
    private Map<Integer, double[]> tokenLocationCenters;
    private Map<String, ImageView> myPlayerTokens;
    private Pane myBoardView;
    private Board myGameBoard;
    private BoardPane myBoardPane;
    private ScrollPane myLoggingScrollPane;
    private TextArea myLoggingField;
    private PlayerTabPane playerTabs;
    private ResourceBundle myLoggingMessages;
    private StackPane textPane;
    private BuildingManager myBuildingManager;


    /**
     * To set up some basic information of the game
     * @param scene The scene of the game
     * @param root The root that contains all the elements that are shown on the screen
     * @param sceneWidth The length of the game scene
     * @param sceneHeight The height of the game scene
     * @param gameBoard The game board that appears on the screen
     * @param players The Queue of all the players in the game
     * @param frontEndDataReader A reader that reads data for frontend
     */

    public GameSceneView (Scene scene, Group root, double sceneWidth, double sceneHeight, Board gameBoard,
                          Queue<Player> players, FrontEndDataReader frontEndDataReader){
        super(scene, root, sceneWidth, sceneHeight);
        myRoot = super.getRoot();
        mySceneWidth = sceneWidth;
        mySceneHeight = sceneHeight;
        numberPerSide = gameBoard.getSize()/4;
        myGameBoard = gameBoard;
        myBoardPane = new BoardPane(BOARD_SIZE, gameBoard.getSize(), gameBoard.getLocation(gameBoard.getSize() / 2));
        myBoardView = myBoardPane.getMyBoardView();
        myTileWidth = myBoardPane.getTileWidth();
        myTileHeight = myBoardPane.getTileHeight();
        myLoggingMessages = ResourceBundle.getBundle("logging_messages");
        makeLogBox();
        createVerticalSetUp();
        tokenLocationCenters = new HashMap<>();
        myPlayerTokens = new HashMap<>();
        setUpTokenCenters();
        setUpPlayerTokens(frontEndDataReader);

        playerTabs = new PlayerTabPane(players, frontEndDataReader);
        tokenLocationCenters = new HashMap<>();
        setUpTokenCenters();

        TabPane playerTabPane = playerTabs.getPlayerTabPane();
        playerTabPane.relocate(BUFFER,BUFFER);
        myRoot.getChildren().add(playerTabPane);
        setUpGameStartHintText();
        setStartingTokenSpots(players);
    }


    private void setUpPlayerTokens(FrontEndDataReader myFrontEndDataReader) {
        for (String playerID: myFrontEndDataReader.getPlayerTokensMap().keySet()) {
            ImageView oneTokenImageView = new ImageView(myFrontEndDataReader.getPlayerTokensMap().get(playerID));
            oneTokenImageView.setFitHeight(myTileWidth/3);
            oneTokenImageView.setFitWidth(myTileWidth/3);
            oneTokenImageView.setX(tokenLocationCenters.get(0)[0]);
            oneTokenImageView.setY(tokenLocationCenters.get(0)[1]);
            myPlayerTokens.put(playerID,oneTokenImageView);
            myRoot.getChildren().add(oneTokenImageView);
        }
    }

    private void setStartingTokenSpots(Collection<Player> players){
        for (Player player : players){
            updatePlayerTokenMove(player.getUniqueID(), player.getStartingLocationNumber());
        }
    }


    private void setUpGameStartHintText() {
        Text text = new Text(HINT);
        text.setFont(Font.font(FONT, SIZE));
        final double prefWidth = text.getBoundsInLocal().getWidth();
        final double prefHeight = text.getBoundsInLocal().getHeight();
        final Rectangle rectangle = new Rectangle(prefWidth, prefHeight);
        rectangle.setFill(RECTANGLE_COLOR);
        textPane = new StackPane();
        textPane.getChildren().addAll(rectangle, text);
        final double layOutX = mySceneWidth / 2 - textPane.getBoundsInLocal().getWidth() / 2;
        final double layOutY = mySceneHeight / 3;
        textPane.setLayoutX(layOutX);
        textPane.setLayoutY(layOutY);
        myRoot.getChildren().add(textPane);
    }

    private void setUpTokenCenters() {
        final double startX = mySceneWidth / 2 + (numberPerSide + 0.5) * myTileWidth + 10;
        final double startY = BUFFER + (numberPerSide + 0.5) * myTileWidth + 10;
        int centerLocation = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < myGameBoard.getSize() / 4; j++) {
                double[] bounds = new double[2];
                final double xCorrection = myTileWidth * j;
                switch (i) {
                    case 0:
                        bounds[0] = startX - xCorrection;
                        bounds[1] = startY;
                        break;
                    case 1:
                        bounds[0] = startX - numberPerSide * myTileWidth - myTileWidth / 3;
                        bounds[1] = startY - xCorrection;
                        break;
                    case 2:
                        bounds[0] = startX - numberPerSide * myTileWidth + xCorrection;
                        bounds[1] = startY - numberPerSide * myTileWidth - myTileWidth / 3;
                        break;
                    case 3:
                        bounds[0] = startX;
                        bounds[1] = startY - numberPerSide * myTileWidth + xCorrection;
                        break;
                }
                tokenLocationCenters.put(centerLocation, bounds);
                centerLocation++;
            }
        }
    }

    /**
     * To update the location of player tokens when needed.
     * @param playerID The name of the player choosen
     * @param locationNumber The location number where the player is at
     */
    public void updatePlayerTokenMove(String playerID, int locationNumber) {
        System.out.println(playerID);
        System.out.println(locationNumber);
        myPlayerTokens.get(playerID).toFront();
        myPlayerTokens.get(playerID).setX(tokenLocationCenters.get(locationNumber)[0]);
        myPlayerTokens.get(playerID).setY(tokenLocationCenters.get(locationNumber)[1]);
    }

    private void makeLogBox() {
        myLoggingField = new TextArea();
        myLoggingScrollPane = new ScrollPane();
        myLoggingScrollPane.setId("logScrollPane");
        myLoggingScrollPane.setContent(myLoggingField);
        myLoggingField.setEditable(false);
    }

    private void createVerticalSetUp(){
        VBox BoardLogBox = new VBox();
        BoardLogBox.setId("scrollPaneVBox");
        BoardLogBox.getChildren().addAll(myBoardView, myLoggingScrollPane);
        BoardLogBox.relocate(mySceneWidth / 2, BUFFER);
        myRoot.getChildren().add(BoardLogBox);
    }

    public String getMessage(String key){
        return myLoggingMessages.getString(key);
    }

    public BoardToolTip getToolTip(Location location) {
        return myBoardPane.getToolTip(location);
    }

    public void logMessage(String message){
        myLoggingField.appendText(message);
    }

    public PlayerTabPane getPlayerTabs() { return playerTabs;}

    public Map<Integer, double[]> getTokenLocationCenters(){
        return tokenLocationCenters;
    }

    public int getNumberPerSide(){
        return numberPerSide;
    }

    public double getTileWidth(){
        return myTileWidth;
    }

    public double getTileHeight(){
        return myTileHeight;
    }

    public void removeHintText() {
        myRoot.getChildren().remove(textPane);
    }

}