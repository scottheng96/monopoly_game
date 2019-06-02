package view.screen;

import controller.FrontEndDataReader;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import java.util.*;

/**
 * To make the screen for players to set the player number, tokens, names, types, intial amount of money, ect.
 */
public class PlayerSetUpScreen extends SplashScreen {

    private static final String ASK_PLAYER_NUMBER = "INPUT THE NUMBER OF PLAYERS";
    private static final String NAME = "NAME";
    private static final String TYPE = "TYPE";
    private static final String MONEY = "MONEY";
    private static final String HUMAN = "HUMAN PLAYER";
    private static final String CPU = "CPU PLAYER";
    private static final int TEXT_SIZE = 40;
    private static final int INITIAL_NUMBER_PLAYERS = 3;
    private static final int INITIAL_MONEY_VALUE = 2500;
    private static final int SCREEN_BOTTOM_BUFFER_WIDTH = 50;
    private final double layoutY = super.getSceneHeight() / 2.0;
    private static final String READY = "READY";
    private static final String NEXT = "NEXT";
    private static final int SCREEN_BOTTOM_BUFFER_HEIGHT = 100;
    private static final int myCorrection = 10;
    private int myPlayerNumber;
    private int myPlayerCount;
    private Spinner<Integer> myPlayerAmountChooser;
    private TextField myPlayerNameTextField;
    private ChoiceBox<String> typeChoiceBox;
    private Spinner<Integer> moneyChoicePicker;
    private Button myPlayerSetUpNumberPlayersButton;
    private Button myNextPlayerScreenButton;
    private TokenSelectionButton tokenSelectionBox;
    private ComboBox tokenSelectionComboBox;
    private Set<Integer> tokenAlreadySelected;
    private Map<Integer, ImageView> myTokenMap;


    ResourceBundle myTokenTypes = ResourceBundle.getBundle("tokenTypes");

    /**
     * To extend the abstract SplashScreen class and set up the background, buttons, and features for the playersetupscreen.
     * @param root The root that contains all the elements that are shown on the screen
     * @param width The width of the game scene screen
     * @param height The height of the game scene screen
     * @param frontEndDataReader The frontEndDataReader that reads data from the frontend.
     */
    public PlayerSetUpScreen(Group root, int width, int height, FrontEndDataReader frontEndDataReader) {
        super(root, width, height, frontEndDataReader);
        setUpBackgroundImage();
        setNumberScreen();
        setUpButtons();
        tokenAlreadySelected = new HashSet<>();
        myTokenMap = new HashMap<>();
        fillTokenMap();
    }

    @Override
    protected void setUpButtons() {
        //necessary abstract method
    }

    /**
     * To set up the screen for players to choose how many players will participate in the game
     */
    private void fillTokenMap(){
        for (int i = 0; i < Integer.parseInt(myTokenTypes.getString("numberOfTokens"));i++) {
            ImageView smallToken = new ImageView(new Image(this.getClass().getClassLoader().getResourceAsStream(myTokenTypes.getString(String.format("token%d", i)))));
            smallToken.setPreserveRatio(true);
            smallToken.setFitHeight(20);
            myTokenMap.put(i, smallToken);
        }
    }

    public ImageView getSelectedTokenView(){
        System.out.println("token choice index" + getTokenChoiceIndex());
        return myTokenMap.get(getTokenChoiceIndex());
    }

    private void setNumberScreen(){
        final int xPosition = getSceneWidth() / 2 - SCREEN_BOTTOM_BUFFER_WIDTH;
        final int yPosition = getSceneHeight() / 3;
        setUpWords(ASK_PLAYER_NUMBER, TEXT_SIZE, xPosition, yPosition);
        myPlayerAmountChooser = new Spinner<>();
        myPlayerAmountChooser.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2, 4, INITIAL_NUMBER_PLAYERS));
        myPlayerAmountChooser.setId("numberPlayers");
        final double v = myPlayerAmountChooser.getPrefWidth() * 90;
        final double v2 = 2.0;
        final double v1 = getSceneWidth() / v2;
        myPlayerAmountChooser.setLayoutX(v1 + v - SCREEN_BOTTOM_BUFFER_WIDTH);
        myPlayerAmountChooser.setLayoutY(getSceneHeight() / v2);
        getRoot().getChildren().add(myPlayerAmountChooser);
        myPlayerCount = 0;
        myPlayerSetUpNumberPlayersButton = makePlayerSetUpScreenReadyButtonNumberOfPlayer();
    }

    /**
     * To set up the new player screen
     */
    public void setNewPlayerScreen(){
        myPlayerNumber = myPlayerAmountChooser.getValue();
        myNextPlayerScreenButton = makePlayerSetUpScreenReadyButtonNextPlayer();
        setPlayerScreen();
    }


    /**
     * To set up the screen for players to put in the players' names, types, and money earned at first.
     */
    private void setPlayerScreen(){
        final int xPosition = getSceneWidth() / 4 - SCREEN_BOTTOM_BUFFER_WIDTH;
        final int yPosition = getSceneHeight() / 3;
        final int xPosition1 = getSceneWidth() / 2 - SCREEN_BOTTOM_BUFFER_WIDTH;
        final int xPosition2 = getSceneWidth() / 4 * 3 - SCREEN_BOTTOM_BUFFER_WIDTH;
        myPlayerCount++;
        setUpWords(NAME, TEXT_SIZE, getSceneWidth() / 4 - SCREEN_BOTTOM_BUFFER_WIDTH, getSceneHeight() / 3);
        setUpWords(TYPE, TEXT_SIZE, getSceneWidth() / 2 - SCREEN_BOTTOM_BUFFER_WIDTH, getSceneHeight() / 3);
        setUpWords(MONEY, TEXT_SIZE, getSceneWidth() / 4 * 3 - SCREEN_BOTTOM_BUFFER_WIDTH, getSceneHeight() / 3);
        setUpWords(NAME, TEXT_SIZE, xPosition, yPosition);
        setUpWords(TYPE, TEXT_SIZE, xPosition1, yPosition);
        setUpWords(MONEY, TEXT_SIZE, xPosition2, yPosition);
        setUpFieldChoiceBoxSpinner();
    }

    private Button makePlayerSetUpScreenReadyButtonNumberOfPlayer() {
        final int xPosition = getSceneWidth() / 2 - myCorrection * 10 - SCREEN_BOTTOM_BUFFER_WIDTH;
        final int yPosition = getSceneHeight() / 5 * 4 - SCREEN_BOTTOM_BUFFER_HEIGHT;
        Button myButton = new Button(READY);
        return makeButton(myButton, xPosition, yPosition);
    }

    private Button makePlayerSetUpScreenReadyButtonNextPlayer() {
        final int xPosition = getSceneWidth() / 2 - myCorrection * 12;
        final int yPosition = getSceneHeight() / 5 * 4 - SCREEN_BOTTOM_BUFFER_HEIGHT;
        Button myButton = new Button(NEXT);
        return makeButton(myButton, xPosition, yPosition);
    }


    public int getPlayerCount(){
        return myPlayerCount;
    }

    public int getPlayerNumber(){
        return myPlayerNumber;
    }

    public Button getPlayerSetUpScreenButton(){
        return myPlayerSetUpNumberPlayersButton;
    }

    public Button getNextPlayerScreenButton(){
        return myNextPlayerScreenButton;
    }

    public String getType() {
        System.out.println("player type");
        System.out.println(String.join(typeChoiceBox.getSelectionModel().getSelectedItem()));
        System.out.println(typeChoiceBox.getSelectionModel().getSelectedItem());
        return String.join("", typeChoiceBox.getSelectionModel().getSelectedItem().split(" "));
    }

    public int getMoney() {
        return moneyChoicePicker.getValue();
    }

    public int getTokenChoiceIndex() {return tokenSelectionComboBox.getSelectionModel().getSelectedIndex();}

    /**
     * To set up the Spinner for players to choose numbers.
     */
    private void setUpFieldChoiceBoxSpinner() {
        setTextField();
        setTypeChoicebox();
        setMoneyChoiceSpinner();
        setTokenSelectionBox();
        addInputListeners();
        getRoot().getChildren().addAll(myPlayerNameTextField, typeChoiceBox, moneyChoicePicker, tokenSelectionComboBox);
    }

    /**
     * To set up the text indicating players to input their names.
     */
    private void setTextField() {
        final double x = getSceneWidth() / 6.0;
        final double layoutY = getSceneHeight() / 2.0;
        myPlayerNameTextField = new TextField();

        myPlayerNameTextField.setLayoutX(getSceneWidth() / 6.0 );
        myPlayerNameTextField.setLayoutX(x);
        myPlayerNameTextField.setLayoutY(layoutY);
        myPlayerNameTextField.setId("playerName");
        validateInput();
    }

    /**
     * To set up a ChoiceBox for players to choose the corresponding type (computer vs. human) for a player.
     */
    private void setTypeChoicebox() {
        final int layoutX = super.getSceneWidth() / 2 - TEXT_SIZE - SCREEN_BOTTOM_BUFFER_WIDTH * 2;
        typeChoiceBox = new ChoiceBox<>();
        typeChoiceBox.setId("humanOrCpu");
        typeChoiceBox.getItems().addAll(HUMAN, CPU);
        typeChoiceBox.setLayoutX(layoutX);
        typeChoiceBox.setLayoutY(layoutY);
    }

    /**
     * To set a spinner for players to choose the initial amount of money owned by each player.
     */
    private void setMoneyChoiceSpinner() {
        final int lowerBound = 10;
        final int upperBound = 10000;
        final int step = 100;
        final double layoutX1 = super.getSceneWidth() / 3.0 * 2;
        moneyChoicePicker = new Spinner<>();
        moneyChoicePicker.setId("money");
        moneyChoicePicker.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(lowerBound, upperBound, INITIAL_MONEY_VALUE, step));
        moneyChoicePicker.setLayoutX(layoutX1);
        moneyChoicePicker.setLayoutY(layoutY);
    }

    /**
     * To set a token selection box that allows players to choose the corresponding token for each player.
     */
    private void setTokenSelectionBox() {
        final int x = 100;
        final int y = 100;
        tokenSelectionBox = new TokenSelectionButton();
        tokenSelectionComboBox = new ComboBox();

        tokenSelectionComboBox = tokenSelectionBox.getTokenImageBox();
        tokenSelectionComboBox.setLayoutX(x);
        tokenSelectionComboBox.setLayoutY(y);
        tokenSelectionComboBox.setId("tokenSelection");
    }

    /**
     * To put listeners that track the choices of players
     */
    private void addInputListeners() {
        myPlayerNameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
        typeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
        tokenSelectionComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
    }

    private void validateInput() {
        Tooltip errorTooltip = new Tooltip("Please make sure all input fields are complete");
        for (Integer temp: new ArrayList<>(tokenAlreadySelected)) {
            System.out.println(temp);
        }

        if (myPlayerNameTextField.getText().trim().isEmpty()
                || typeChoiceBox.getValue() == null
                || tokenSelectionComboBox.getValue() == null
                || tokenAlreadySelected.contains(tokenSelectionComboBox.getItems().indexOf(tokenSelectionComboBox.getValue()))) {
            myNextPlayerScreenButton.setDisable(true);
            myNextPlayerScreenButton.setTooltip(errorTooltip);
        } else {
            myNextPlayerScreenButton.setTooltip(null);
            myNextPlayerScreenButton.setDisable(false);
        }
    }

    public String getPlayerName(){
        return myPlayerNameTextField.getText();
    }

    public void setTokenAlreadyChosen() {
        tokenAlreadySelected.add(tokenSelectionComboBox.getItems().indexOf(tokenSelectionComboBox.getValue()));
    }
}

class TokenSelectionButton {

    private ComboBox<Image> tokenImageBox;
    private static ResourceBundle myTokenTypes;
    private static final int tokenSize = 100;

    public TokenSelectionButton () {
        tokenImageBox = new ComboBox<>();
        myTokenTypes = ResourceBundle.getBundle("tokenTypes");
        for (int i = 0; i < Integer.parseInt(myTokenTypes.getString("numberOfTokens"));i++) {
            Image oneToken = new Image(this.getClass().getClassLoader().getResourceAsStream(myTokenTypes.getString(String.format("token%d", i))));
            tokenImageBox.getItems().add(oneToken);
        }
        tokenImageBox.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Image> call(ListView<Image> p) {
                return new ListCell<>() {
                    private final ImageView iconImageView; {
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        iconImageView = new ImageView();
                        iconImageView.setPreserveRatio(true);
                        iconImageView.setFitHeight(tokenSize);
                    }
                    @Override
                    protected void updateItem(Image image, boolean empty) {
                        super.updateItem(image, empty);
                        if (image == null || empty) {
                            setGraphic(null);
                        } else {
                            iconImageView.setImage(image);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        });
        tokenImageBox.setButtonCell(new IconImageCellClass());
    }
    public ComboBox getTokenImageBox() {return tokenImageBox;}
}

class IconImageCellClass extends ListCell<Image> {
    static final private double tokenSize = 100;
    @Override
    protected void updateItem(Image token, boolean empty) {
        super.updateItem(token, empty);
        if (token != null) {
            ImageView tokenImageView = new ImageView(token);
            tokenImageView.setFitHeight(tokenSize);
            tokenImageView.setPreserveRatio(true);
            setGraphic(tokenImageView);
        }
    }
};
