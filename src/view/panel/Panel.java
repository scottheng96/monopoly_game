package view.panel;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.location.propertySpace.PropertySpace;
import model.player.Player;
import model.player.Turn;
import view.ButtonBox;
import java.util.*;

/**
 * The abstract class panel is the parent class of all the panels including the management, the trade panel, and the auction
 * panel.
 */
public abstract class Panel {

    private final int BUTTONS_X = 100;
    private final int BUTTONS_Y = 450;

    private Pane myPane;
    private Group myRoot;
    private ButtonBox myButtonBox;
    private Stage myStage;
    private Scene myScene;
    private static final Color BACKGROUND = Color.AZURE;

    /** Set up a panel
     * @param root the root for the scene
     */
    public Panel(Group root) {
        myPane = new GridPane();
        myRoot = root;
        myButtonBox = new ButtonBox();
    }

    protected void setUpNewWindow(double width, double height, String title){
        myScene = new Scene(getRoot(),width, height, BACKGROUND);
        myStage = new Stage();
        myStage.setScene(myScene);
        myStage.setTitle(title);
        myStage.setResizable(false);
    }

    protected void makeButtonsByID(List<String> buttonIDs){
        myButtonBox.makeButtonsByID(buttonIDs);
    }

    /** This method sets up the pane.
     * @param paneID the ID of this pane
     */
    public void makePane(String paneID) {
        myPane.relocate(BUTTONS_X, BUTTONS_Y);
        myPane.setId(paneID);
        myPane.getChildren().addAll(myButtonBox.getButtonBox());
        myRoot.getChildren().add(myPane);
    }

    protected void makeButtonBox() {
        myButtonBox.makeButtonBox();
    }

    protected HBox getButtonBox(){
        return myButtonBox.getButtonBox();
    }

    /** This method returns all the buttons on the pane
     * @return A list of buttons
     */
    public List<Button> getAllButtons() {
        return myButtonBox.getAllButtons();
    }

    /** This method returns the root of the scene
     * @return root
     */
    public Group getRoot() {
        return myRoot;
    }

    /** This method can be used to set up text on a pane.
     * @param x x Position
     * @param y y Position
     * @param text String of text message
     */
    public void setUpText(int x, int y, String text) {
        Text message = new Text(text);
        message.setFont(Font.font ("Verdana", 40));
        message.setX(x / 2.0 - message.getBoundsInLocal().getWidth() / 2);
        message.setY(y / 4.0);
        getRoot().getChildren().add(message);
    }

    /**
     * This method disable all the buttons on a pane.
     */
    public void disableAllButtons(){
        myButtonBox.disableAllButtons();
    }

    /** This method returns a button according to its unique ID
     * @param ID the ID of a button
     * @return Button
     */
    public Button getButtonByID(String ID){
        return myButtonBox.getButtonByID(ID);
    }

    /** This method update the pane based on the information given.
     * @param current current player
     * @param allPlayers all the players
     */
    public abstract void update(Player current, Collection<Player> allPlayers);


    protected Map<String, PropertySpace> getNewPropertiesMap(List<PropertySpace> properties){
        Map<String, PropertySpace> newProperties = new HashMap<>();
        for (PropertySpace property : new HashSet<>(properties)) {
            newProperties.put(property.getName(), property);
        }
        return newProperties;
    }

    /** This method shows the management panel on the screen.
     */
    public void show(){
        getStage().show();
    }

    /**
     * This method closes the pane.
     */
    public void close(){
        myStage.close();
    }

    protected Scene getScene(){
        return myScene;
    }

    protected Stage getStage(){
        return myStage;
   }
}
