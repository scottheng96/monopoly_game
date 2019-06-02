package view.screen;

import controller.FrontEndDataReader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import java.util.*;

/**
 * This class set up the rules of the game, including the number of dice, the number of sides for dice, the way a player
 * wins, the money received by the player when he/she passes Go.
 */
public class RuleSetUpScreen extends SplashScreen{
    static private final String DICE_NUM = "Number of dice";
    static private final String DICE_SIDE = "Number of sides for dice";
    static private final String CHECK_WIN = "How do players win";
    static private final String LAST_MAN_STANDING = "Last Player Standing Wins";
    static private final String CHECK_AFTER_FIRST_BANKRUPTCY = "Richest Player After First Bankruptcy Wins";
    static private final ArrayList<String> PERMANENT_RULES = new ArrayList<>(Arrays.asList("Money On Passing Go"));
    static private final String PASSING_GO = "How much money for passing 'Go'";

    static private final int TEXT_SIZE = 20;
    static private final int SCREEN_LEFT_SIDE_BUFFER_WIDTH = 50;
    static private final int SCREEN_RIGHT_SIDE_BUFFER_WIDTH = 70;
    static private final int INITIAL_NUMBER_DICE = 2;
    static private final int INITIAL_SIDE_DICE = 6;
    static private final int INITIAL_PASSING_FEE_VALUE = 200;
    static private final int DISTANCE = 15;
    final private int LEFT_SIDE = getSceneWidth() / 4 - SCREEN_LEFT_SIDE_BUFFER_WIDTH;
    final private  int RIGHT_SIDE = getSceneWidth() / 10 * 7 - SCREEN_RIGHT_SIDE_BUFFER_WIDTH;
    static private final int DICE_NUM_MIN = 1;
    static private final int DICE_NUM_MAX = 4;
    static private final int DICE_SIDE_MIN = 1;
    static private final int DICE_SIDE_MAX = 9;
    static private final int CORRECTION = 50;

    private Map<String,String> dataPropertyMap;
    private static final String NEXT = "NEXT";
    private static final int BUTTON_CORRECTION = 10;
    private static final int SCREEN_BOTTOM_BUFFER_HEIGHT = 100;

    private Spinner<Integer> myDiceNum;
    private Spinner<Integer> myDiceSide;
    private Spinner<Integer> myParkingFee;
    private ChoiceBox myCheckWin;
    private Button myNextButton;
    private List<CheckBox> myCheckableRuleBoxes;
    private VBox myStack;
    private ResourceBundle myCheckableRules = ResourceBundle.getBundle("rule_numbers");
    private Text optimalRuleText;
    private CheckBox myOfficialRulesBox;

    /** The constructor that set up a RuleSetUpScreen
     * @param root Root of the scene
     * @param sceneWidth Width of the scene
     * @param sceneHeight Height of the scene
     * @param frontEndDataReader A reader that can be used to read the front end data
     */
    public RuleSetUpScreen(Group root, int sceneWidth, int sceneHeight, FrontEndDataReader frontEndDataReader) {
        super(root, sceneWidth, sceneHeight, frontEndDataReader);
        setUpButtons();
        setRuleScreen();
        getMyStack();
        myCheckableRuleBoxes = new ArrayList<>();
        setUpRuleCheckers();
        ScrollPane scrollPane = getScrollPane(sceneWidth);
        root.getChildren().add(scrollPane);
        dataPropertyMap = frontEndDataReader.getOptimalRulesMap();
        optimalRuleText = makeOptimalRuleText();
        optimalRuleText.setX(sceneWidth/2+CORRECTION);
        optimalRuleText.setY(sceneHeight/2-CORRECTION);
        myOfficialRulesBox = makeOfficialRulesBox();
        root.getChildren().addAll(optimalRuleText, myOfficialRulesBox);
    }

    /** This method will return a list of String, containing all the inputs indicating the rules of the game.
     * @return A list of Strings
     */
    public List<String> getCheckedRuleNames(){
        List<String> checked = new ArrayList<>();
        for (int numberBox = 0; numberBox < myCheckableRuleBoxes.size(); numberBox++){
            if (myCheckableRuleBoxes.get(numberBox).isSelected()){
                System.out.println(myCheckableRules.getString(Integer.toString(numberBox)) + "is checked");
                checked.add(myCheckableRules.getString(Integer.toString(numberBox)));
            }
        }
        checked.addAll(PERMANENT_RULES);
        return checked;
    }

    private void getMyStack() {
        myStack = new VBox();
        final int v = 15;
        final int v1 = 12;
        myStack.setPadding(new Insets(v, v1, v, v1));
        final int spacing = 10;
        myStack.setSpacing(spacing);
        final int height = 600;
        myStack.setPrefHeight(height);
        final int width = 500;
        myStack.setPrefWidth(width);
        myStack.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
    }

    private ScrollPane getScrollPane(int width) {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(myStack);
        final double v = width / 2.0 + scrollPane.getWidth();
        final int v1 = 50;
        scrollPane.relocate(v, v1);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        return scrollPane;
    }

    @Override
    protected void setUpButtons() {
        myNextButton = makeRuleSetUpScreenNextButton();
        myNextButton.setDisable(true);
    }

    private Button makeRuleSetUpScreenNextButton() {
        Button myButton = new Button(NEXT);
        final int xPosition = getSceneWidth() / 2 - BUTTON_CORRECTION * 15;
        final int yPosition = getSceneHeight() / 5 * 4 - SCREEN_BOTTOM_BUFFER_HEIGHT;
        return makeButton(myButton, xPosition, yPosition);
    }

    private CheckBox makeOfficialRulesBox() {
        CheckBox checkBox = new CheckBox("USE OFFICIAL RULES");
        checkBox.relocate(getSceneWidth() / 2.0 + CORRECTION, optimalRuleText.getY() + optimalRuleText.getBoundsInLocal().getHeight());
        checkBox.selectedProperty().addListener((property, old, newValue) ->{
            validateInput();
        });
        return checkBox;
    }

    public boolean useOfficialRules(){
        return myOfficialRulesBox.isSelected();
    }

    private void setRuleScreen(){
        setDiceNum();
        setDiceSide();
        setWin();
        setPassingFee();
    }

    private void setUpRuleCheckers(){
        Enumeration<String> enumeration = myCheckableRules.getKeys();
        while (enumeration.hasMoreElements()) {
            HBox hBox = new HBox();
            final int v = 15;
            final int v1 = 12;
            hBox.setPadding(new Insets(v, v1, v, v1));
            final int v2 = 50;
            hBox.setSpacing(v2);
            String nextElement = enumeration.nextElement();
            final int v3 = 2;
            hBox.getChildren().add(setUpWords(myCheckableRules.getString(nextElement), TEXT_SIZE, RIGHT_SIDE, getSceneHeight() / DISTANCE * (Integer.parseInt(nextElement) + v3)));
            CheckBox checkBox = new CheckBox();
            checkBox.setScaleX(v3);
            checkBox.setScaleY(v3);
            checkBox.setAlignment(Pos.BOTTOM_CENTER);
            myCheckableRuleBoxes.add(checkBox);
            hBox.getChildren().add(checkBox);
            myStack.getChildren().add(hBox);

        }
    }


    private void setSpinner(Spinner<Integer> spinner, int min, int max, int initial, double Xpos, double Ypos, int adding){
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial, adding));
        spinner.setEditable(true);
        spinner.setLayoutY(Ypos);
        spinner.setLayoutX(Xpos);
        getRoot().getChildren().add(spinner);
    }

    private void setDiceNum(){
        setUpWords(DICE_NUM, TEXT_SIZE, LEFT_SIDE, getSceneHeight() / DISTANCE);
        setDiceNumberChooser();
    }

    private void setDiceNumberChooser(){
        myDiceNum = new Spinner<>();
        final double xpos = LEFT_SIDE + myDiceNum.getPrefWidth() * 90;
        final int adding = 1;
        setSpinner(myDiceNum, DICE_NUM_MIN, DICE_NUM_MAX, INITIAL_NUMBER_DICE, xpos, getSceneHeight() / DISTANCE + TEXT_SIZE, adding);
    }

    private void setDiceSide(){
        final int yPosition = getSceneHeight() / DISTANCE * 7 / 2;
        setUpWords(DICE_SIDE, TEXT_SIZE, LEFT_SIDE, yPosition);
        setDiceSideChooser();
    }

    private void setDiceSideChooser(){
        myDiceSide = new Spinner<>();
        final double xpos = LEFT_SIDE + myDiceSide.getPrefWidth() * 90;
        final int ypos = getSceneHeight() / DISTANCE * 7 / 2 + TEXT_SIZE;
        final int adding = 1;
        setSpinner(myDiceSide, DICE_SIDE_MIN, DICE_SIDE_MAX, INITIAL_SIDE_DICE, xpos, ypos, adding);
    }

    private void setWin(){
        final int yPosition = getSceneHeight() / DISTANCE * 6;
        setUpWords(CHECK_WIN, TEXT_SIZE, LEFT_SIDE, yPosition);
        setWinChooser();
    }

    private void setWinChooser(){
        myCheckWin = new ChoiceBox();
        myCheckWin.setId("win");
        myCheckWin.getItems().addAll(LAST_MAN_STANDING, CHECK_AFTER_FIRST_BANKRUPTCY);
        final double layoutX = LEFT_SIDE + myCheckWin.getPrefWidth() * 90;
        myCheckWin.setLayoutX(layoutX);
        final int layoutY = getSceneHeight() / DISTANCE * 6 + TEXT_SIZE;
        myCheckWin.setLayoutY(layoutY);
        getRoot().getChildren().add(myCheckWin);
        myCheckWin.valueProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
    }

    private void validateInput() {
        myNextButton.setDisable((myCheckWin.getValue() == null && !myOfficialRulesBox.isSelected()));
    }

    private void setPassingFee(){
        final int yPosition = getSceneHeight() / DISTANCE * 17 / 2;
        setUpWords(PASSING_GO, TEXT_SIZE, LEFT_SIDE, yPosition);
        setPassingFeeChooser();
    }

    private void setPassingFeeChooser(){
        myParkingFee = new Spinner<>();
        final int min = 50;
        final int max = 10000;
        final int ypos = getSceneHeight() / DISTANCE * 17 / 2 + TEXT_SIZE;
        final double xpos = LEFT_SIDE + myCheckWin.getPrefWidth() * 90;
        setSpinner(myParkingFee, min, max, INITIAL_PASSING_FEE_VALUE, xpos, ypos, min);

    }

    /** This method returns a Button that allows the user to click and step to the next screen after the rules are set.
     * @return Button
     */
    public Button getRuleNextButton(){
        return myNextButton;
    }

    /** This method returns an integer that representing the number of the dice of the current game.
     * @return Integer
     */
    public int getDiceNum() {
        return myDiceNum.getValue();
    }

    /** This method returns an integer representing the side of the dice of the current game.
     * @return Integer
     */
    public int getDiceSide() {
        System.out.println("SIDE NUMBERS FROM METHOD   " + myDiceSide.getValue());
        return myDiceSide.getValue();
    }

    /** This method returns an integer representing the money received by a player when he/she passes Go.
     * @return Integer
     */
    public int getGoMon() {
        return myParkingFee.getValue();
    }

    /** This method returns a String representing the winning condition of the game.
     * @return String
     */
    public String getWinRule() {
        return myCheckWin.getSelectionModel().getSelectedItem().toString();
    }

    private Text makeOptimalRuleText() {
        Text optimalRuleText = new Text();
        String myString = "For an optimal gaming experience, adhere to the following!\n";
        for (String key: dataPropertyMap.keySet()) {
            if(!key.equals("GO") && !key.equals("JAIL") && !key.equals("FREE_PARKING")) {
                if (key.equals("RULES")) {
                    String rulesString = key+ "\n";
                    String[] rules = dataPropertyMap.get(key).split(",");
                    for (String each: rules) {
                        rulesString += (each + "\n");
                    }
                    myString += rulesString;
                } else {
                    myString += (key + ": " + dataPropertyMap.get(key) + "\n");
                }
            }
        }
        optimalRuleText.setText(myString);
        return optimalRuleText;
    }
}
