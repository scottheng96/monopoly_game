package view;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.util.*;

public class ButtonBox {

    private final int BUTTONS_X = 150;
    private final int BUTTONS_Y = 450;
    private final int EXTRA_PADDING = 10;
    private final int BUTTON_PADDING = 50;


    private List<Button> myAllButtons;
    private HBox myButtonBox;
    private Map<String, Button> myButtonsById;
    private ResourceBundle myButtonNameResources = ResourceBundle.getBundle("button_text");

    public ButtonBox() {
        myAllButtons = new ArrayList<>();
        myButtonBox = new HBox();
        myButtonsById = new HashMap<>();
    }


    public void makeButtonsByID(List<String> buttonIDs){
        for (String buttonId : buttonIDs){
            Button button = new Button();
            button.setId(buttonId);
            button.setText(myButtonNameResources.getString(buttonId));
            addButtonToAllButtons(button);
            myButtonsById.put(buttonId, button);
        }
    }

    public void addNewButtonByID(String buttonID){
        makeButtonsByID(new ArrayList<>(Arrays.asList(buttonID)));
        myButtonBox.getChildren().add(myButtonsById.get(buttonID));
    }

    private void addButtonToAllButtons(Button button){
        myAllButtons.add(button);
    }


    public void makeButtonBox() {
        myButtonBox = new HBox(EXTRA_PADDING);
        myButtonBox.setPadding(new Insets(BUTTON_PADDING,BUTTON_PADDING,BUTTON_PADDING,BUTTON_PADDING));
        myButtonBox.relocate(BUTTONS_X, BUTTONS_Y);
        myButtonBox.getChildren().addAll(myAllButtons);
    }

    public List<Button> getAllButtons() {
        return myAllButtons;
    }


    public void disableAllButtons(){
        for (Button button : myAllButtons){
            button.setDisable(true);
        }
    }

    public Button getButtonByID(String ID){
        return myButtonsById.get(ID);
    }

    public HBox getButtonBox(){
        return myButtonBox;
    }
}
