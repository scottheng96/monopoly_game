package view.tilepane;

import controller.FrontEndDataReader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import java.util.*;

/**
 * This class is a player tab pane.
 */
public class PlayerTabPane {
    TabPane playerTabPane;
    private Map<Player, Integer> playerIntegerMap;
    private static final double paneWidth = 500;
    private static final double paneHeight = 400;
    private static Map<String, Text> playerToPaneHashMap;
    private ResourceBundle myBundle;

    /** This method constructs a player tab pane.
     * @param allPlayers all the players in the game
     * @param frontEndDataReader a data reader that can be used to read front end data
     */
    public PlayerTabPane(Collection<Player> allPlayers, FrontEndDataReader frontEndDataReader) {
        myBundle = ResourceBundle.getBundle("player_tab");
        playerTabPane = new TabPane();
        playerTabPane.setId("playerPane");
        playerTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        playerToPaneHashMap = new HashMap<>();
        playerIntegerMap = new HashMap<>();

        int temp = 0;
        for (Player player: allPlayers) {
            ImageView representationToken = new ImageView(frontEndDataReader.getPlayerTokensMap().get(player.getUniqueID()));
            representationToken.setPreserveRatio(true);
            representationToken.setFitWidth(100);
            Tab newTab = new Tab();
            newTab.setText(player.getName());
            GridPane playerInfoPane = new GridPane();
            Text playerInfo = new Text();
            playerInfo.setId("onePlayerText");
            playerInfo.setText(String.format(myBundle.getString("PLAYERTABINFO"), player.getName(), player.getMoneyBalance(), ""));
            playerInfoPane.setPrefSize(paneWidth,paneHeight);
            playerInfoPane.add(representationToken,0,0);
            playerInfoPane.add(playerInfo,0,1);
            playerToPaneHashMap.put(player.getUniqueID(), playerInfo);
            newTab.setContent(playerInfoPane);
            playerTabPane.getTabs().add(newTab);
            playerIntegerMap.put(player, temp);
            temp++;
            update(player);
        }
    }

    public TabPane getPlayerTabPane() { return playerTabPane;}

    public void update(Player newPlayer) {
        String locationString = "";
        for (PropertySpace eachProperty: newPlayer.getProperties()) {
            locationString = locationString + eachProperty.getName() + "\n";
        }
        playerToPaneHashMap.get(newPlayer.getUniqueID()).setText(String.format(myBundle.getString("PLAYERTABINFO"), newPlayer.getName(), newPlayer.getMoneyBalance(),locationString));
    }

    public void updateCurrentTab(Player currentPlayer) {
        playerTabPane.getSelectionModel().select(playerIntegerMap.get(currentPlayer));
    }
}
