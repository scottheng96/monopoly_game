package view;

import javafx.scene.control.Tooltip;

public class BoardToolTip extends Tooltip {

    private static final String TOOL_TIP_MESSAGE = "Name: %s \nOwner: %s \nCost: %d \nCost To Build One House: %d \nMortgaged?: %s " +
            "\n Current Number of Houses: %d \n Rent: %d";

    public BoardToolTip(String locationName, int locationCost, String locationOwner, int costToBuildOneHouse, boolean isMortgaged, int numberOfHouses, int rent) {
        updateInformation(locationName, locationCost, locationOwner, costToBuildOneHouse, isMortgaged, numberOfHouses, rent);
    }

    public void updateInformation(String locationName, int locationCost, String locationOwner, int costToBuildOneHouse, boolean isMortgaged, int numberOfHouses, int rent){
        String myText = String.format(TOOL_TIP_MESSAGE, locationName, locationOwner, locationCost, costToBuildOneHouse, isMortgaged, numberOfHouses, rent);
        setText(myText);
    }
}