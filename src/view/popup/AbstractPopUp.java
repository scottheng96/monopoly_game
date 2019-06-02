package view.popup;

import controller.FrontEndDataReader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.roll.Roll;
import model.player.Player;
import view.ButtonBox;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;

/**
 * This class is the parent class of all the pop ups.
 */
public class AbstractPopUp {

    public static final String REFUSE_BUTTON_ID = "refuseCommand";
    private static final int NUMBER_OF_COLUMNS = 4;
    private static final int COLUMN_SIZE = 150;
    private static final int ROW_SIZE = 300;
    private static final int X_COORDINATE = 100;
    private static final int Y_COORDINATE = 300;
    private static final int FONT_SIZE = 20;
    private static final String RESOLVE_PLAYER_COMMAND = "resolvePlayerCommand";
    private static final String POPUP_PANE_ID = "popupPane";
    private ButtonBox myButtonBox;
    private Stage myStage;
    private GridPane myPane;
    private Text mainText;
    private ResourceBundle myMessageResources = ResourceBundle.getBundle("popup_messages");
    private Player myCurrentPlayer;
    private Player myBank;
    private Roll myRoll;

    /** This method constructs a pop up.
     * @param currentPlayer current player of the game
     * @param allPlayers all the players of the game
     * @param bank an imaginary player that serves as a bank
     * @param roll roll
     * @param myFrontEndDataReader a reader that can be used to read front end data
     */
    public AbstractPopUp (Player currentPlayer, Collection<Player> allPlayers, Player bank, Roll roll, FrontEndDataReader myFrontEndDataReader) {
        myCurrentPlayer = currentPlayer;
        myBank = bank;
        myRoll = roll;
        myStage = new Stage();
        myStage.initStyle(StageStyle.UNDECORATED);

        setUpLayout();
        mainText = makeMiddleText();
        myPane.add(mainText, 1,0);
        setUpButtons();

        myButtonBox.getButtonByID(RESOLVE_PLAYER_COMMAND).setOnAction((stateEvent) -> {
            doResolveButtonAction();
        });
    }

    private void setUpLayout(){
        myPane = new GridPane();
        myPane.setId(POPUP_PANE_ID);
        for (int i=0;i<NUMBER_OF_COLUMNS;i++) {
            myPane.getColumnConstraints().add(new ColumnConstraints(COLUMN_SIZE));
        }

        myPane.getRowConstraints().add(new RowConstraints(ROW_SIZE));
        final int v = ROW_SIZE / 3;
        myPane.getRowConstraints().add(new RowConstraints(v));

    }

    protected void doResolveButtonAction(){
        System.out.println("popup is executing state change for " + myCurrentPlayer.getName());
        myCurrentPlayer.executeStateChange(myBank, myRoll);
    }

    protected Player getCurrentPlayer(){
        return myCurrentPlayer;
    }

    private Text makeMiddleText() {
        Text myText = new Text();
        myText.setWrappingWidth(COLUMN_SIZE * 2);
        myText.setFont(new Font(FONT_SIZE));
        myText.setTextAlignment(TextAlignment.CENTER);
        return myText;
    }


    /** This method returns a button box.
     * @return ButtonBox
     */
    public ButtonBox getButtonBox() {
        return myButtonBox;
    }

    /**
     * This method shows the pop up.
     */
    public void show() {
        Scene scene = new Scene(myPane);
        myStage.setResizable(false);
        myStage.setScene(scene);
        myStage.setX(X_COORDINATE);
        myStage.setY(Y_COORDINATE);
        myStage.show();
    }

    /**
     * This method closes the pop up.
     */
    public void close(){
        myStage.close();
    }

    /** This method returns the main text shown on the screen.
     * @return Text
     */
    public Text getMainText() {
        return mainText;
    }

    private void setUpButtons(){
        myButtonBox = new ButtonBox();
        myButtonBox.makeButtonsByID(new ArrayList<>(Collections.singletonList(RESOLVE_PLAYER_COMMAND)));
        myButtonBox.makeButtonBox();
        myPane.add(myButtonBox.getButtonBox(), 1, 1);
        myPane.setColumnSpan(myButtonBox.getButtonBox(),2);
    }

    protected String getMessageString(String stateName){
        return myMessageResources.getString(stateName);
    }

    protected void addToPane(Node toAdd, int row, int column){
        myPane.add(toAdd, row, column);
    }
}
