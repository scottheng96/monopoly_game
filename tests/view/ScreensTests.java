package view;

import engine.GameMain;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ScreensTests extends ApplicationTest {
    private ChoiceBox myRulesBox;
    private Button testButton;
    private TextInputControl myTextField;
    private ComboBox myTokenSelector;
    private Spinner myNumberOfPlayers;

    @BeforeEach
    protected void setUp() throws Exception {

        launch(GameMain.class);
        Button myButton2 = lookup("NEW GAME").queryButton();

        clickOn(myButton2);
        Button myButton3 = lookup("READY").queryButton();

        clickOn(myButton3);
        myRulesBox = lookup("#win").queryAs(ChoiceBox.class);


    }

    private void clickOn (Button b) {
        simulateAction(b, () -> b.fire());
    }

    private void simulateAction (Node n, Runnable action) {
        // simulate robot motion
        moveTo(n);
        // fire event using given action on the given node
        Platform.runLater(action);
        // make it "later" so the requested event has time to run
        WaitForAsyncUtils.waitForFxEvents();
    }

    private void select (ChoiceBox<String> cb, String value) {
        simulateAction(cb, () -> cb.getSelectionModel().select(value));
    }
    private void select (ComboBox<String> cb, String value) {
        simulateAction(cb, () -> cb.getSelectionModel().select(value));
    }



    @Test
    protected void checkWelcomeButtonIsEnabled() {
        getNextButton();
        boolean actual = testButton.isDisabled();
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    protected void checkNumberOfPlayersButtonIsAlwaysEnabled() {
        getToNumberOfPlayersScreen();
        boolean actual = testButton.isDisabled();
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    protected void numberOfPlayersButtonIsEnabled() {
        getToNumberOfPlayersScreen();
        Button ready = lookup("READY").queryButton();
        clickOn(ready);
        boolean actual = ready.isDisabled();
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    protected void playerSelectionScreenButtonIsDisabled() {
        getToNumberOfPlayersScreen();
        Button ready = lookup("READY").queryButton();
        clickOn(ready);
        getTexts();
        getNextButton();
        boolean actual = testButton.isDisabled();
        boolean expected = true;
        assertEquals(expected, actual);
    }

    @Test
    protected void playerSelectionScreenButtonIsEnabled() {
     playerSelectionScreenButtonIsDisabled();
        clickOn(myTextField).write("Team 07");
        boolean actual = testButton.isDisabled();
        boolean expected = false;
        assertEquals(expected, actual);
    }

    @Test
    protected void successfullyReachBoardView() {
        getToNumberOfPlayersScreen();
        myNumberOfPlayers = lookup("#numberPlayers").queryAs(Spinner.class);
        myNumberOfPlayers.getValueFactory().setValue(2);
        Button ready = lookup("READY").queryButton();
        clickOn(ready);
        getTexts();
        clickOn(myTextField).write("Is The Best");

        Button next = lookup("NEXT").queryButton();
        clickOn(next);
        getTexts();
        clickOn(myTextField).write("Team 07");

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myTokenSelector.getSelectionModel().selectLast(); // if you change the UI, do it here !
            }
        });
        Button next2 = lookup("NEXT").queryButton();
        clickOn(next2);
         long startTime = System.currentTimeMillis();
        long endTime = startTime+(60 * 60 ); // this is for 1 hr

        while(System.currentTimeMillis()<endTime){
            moveTo(100,100);
            moveTo(300,100);
            moveTo(100,300);
            moveTo(0,400);
        }

    }

    private void getToNumberOfPlayersScreen() {
        getNextButton();
        select(myRulesBox, "Last Player Standing Wins");
        clickOn(testButton);
    }

    private void getNextButton() {
        testButton = lookup("NEXT").queryButton();
    }

    private void getTexts() {
        myTextField = lookup("#playerName").queryTextInputControl();

        myRulesBox = lookup("#humanOrCpu").queryAs(ChoiceBox.class);
        select(myRulesBox, "HUMAN");
        myTokenSelector =lookup("#tokenSelection").queryComboBox();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                myTokenSelector.getSelectionModel().select(1); // if you change the UI, do it here !
            }
        });

        clickOn(myTokenSelector);

    }

    @AfterEach
    protected void tearDown () throws Exception {
        // remove stage of running app
        FxToolkit.cleanupStages();
        // clear any key or mouse presses left unreleased
        release(new KeyCode[] {});
        release(new MouseButton[] {});
    }
}
