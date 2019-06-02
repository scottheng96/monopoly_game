package view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;

public class DiceView {

    private final int DICE_NUMBER_SIZE = 200;
    private final int POSITION_BUFFER = 20;
    private final Color BACKGROUND = Color.LIGHTSKYBLUE;
    private final String FONT = "Verdana";
    private final Color TEXT_COLOR = Color.DARKBLUE;

    private Stage myStage;
    private Group myRoot;
    private Scene myScene;

    public DiceView(List<Integer> allRolls) {
        myStage = new Stage();
        myRoot = new Group();
        myScene = new Scene(myRoot, allRolls.size() * DICE_NUMBER_SIZE, DICE_NUMBER_SIZE, BACKGROUND);
        myStage.setScene(myScene);
        myStage.setResizable(false);
        myStage.setX(800);
        myStage.setY(300);
        for (int i = 0; i < allRolls.size(); i++) {
            Text diceNumberText = new Text(allRolls.get(i) + "");
            diceNumberText.setLayoutX(i * DICE_NUMBER_SIZE + POSITION_BUFFER * 2);
            diceNumberText.setY(DICE_NUMBER_SIZE - POSITION_BUFFER);
            diceNumberText.setStyle(String.format("-fx-font: %d %s;", DICE_NUMBER_SIZE, FONT));
            diceNumberText.setFill(TEXT_COLOR);
            myRoot.getChildren().add(diceNumberText);
        }
        myStage.show();
    }

    public Stage getStage() {
        return myStage;
    }
}
