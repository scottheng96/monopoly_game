package view;

import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.location.Location;
import model.location.propertySpace.*;
import view.tilepane.NonPropertyPane;
import view.tilepane.PropertyPane;
import java.util.HashMap;
import java.util.Map;

/**
 * To add a pane to the board during the game
 */
public class BoardPane{
    GridPane myBoardView;
    private int numberPerSide;
    private double tileWidth;
    private double tileHeight;
    private int NORTH = 0, SOUTH = 180, EAST = 270, WEST = 90;
    private Map<Integer, double[]> locationCenters;
    private Map<Location, BoardToolTip> myLocationsToToolTips;
    private static final String BOARD_VIEW_PANE_ID = "boardPane";


    public BoardPane(double boardLength, int numTiles, Location middleLocation) {
        myBoardView = new GridPane();
        numberPerSide = numTiles / 4;
        tileWidth = boardLength / (numberPerSide-1);
        tileHeight = tileWidth;
        Location currentLocation = middleLocation;
        locationCenters = new HashMap<>();
        myLocationsToToolTips = new HashMap<>();

        for (int i = 0; i < 4; i++) {
            int rotateBy = setRotate(i);
            for (int j = 0; j < numberPerSide; j++) {
                BorderPane newTile = makePane(tileWidth, tileHeight, currentLocation, rotateBy);
                newTile.setMinWidth(tileWidth);
                newTile.setMinHeight(tileHeight);
                if (currentLocation instanceof PropertySpace){
                    System.out.println(currentLocation.getName());
                    addRealEstateTooltip((PropertySpace) currentLocation, newTile);
                }
                switch(i) {
                    case(0):
                        myBoardView.add(newTile, j, 0);
                        break;
                    case(1):
                        myBoardView.add(newTile,numberPerSide,j);
                        break;
                    case(2):
                        myBoardView.add(newTile,numberPerSide-j, numberPerSide);
                        break;
                    case(3):
                        myBoardView.add(newTile,0,numberPerSide-j);

                }
                double x = newTile.getBoundsInParent().getWidth();
                double y = newTile.getBoundsInParent().getHeight();
                double[] bounds = new double[2];
                bounds[0] = x;
                bounds[1] = y;
                locationCenters.put(i*10 +j,bounds);
                currentLocation = currentLocation.getNextLocation();
            }
            myBoardView.setId(BOARD_VIEW_PANE_ID);
        }
    }

    private void addRealEstateTooltip(PropertySpace currentLocation, BorderPane newTile) {
        BoardToolTip tooltip = new BoardToolTip(currentLocation.getName(), currentLocation.getCost(),
                currentLocation.getOwner().getName(), currentLocation.getBuildingCost(), currentLocation.isMortgaged(), currentLocation.getNumberOfHouses(), currentLocation.getRent());
        myLocationsToToolTips.put(currentLocation, tooltip);
        Tooltip.install(newTile, tooltip);
    }


    private BorderPane makePane(double tileWidth, double tileHeight, Location location, int rotate) {
        BorderPane newTile;
        if (location instanceof RealEstate) {
            PropertySpace thisSpace = (PropertySpace) location;
            newTile = new PropertyPane(tileWidth,tileHeight,location.getName(),thisSpace.getGroup(), thisSpace.getCost()).getPane();
        } else if (location.getLocationNumber()%numberPerSide==0) {
            newTile = new NonPropertyPane(tileHeight,tileHeight, location).getPane();
        } else {
            newTile = new NonPropertyPane(tileWidth,tileHeight, location).getPane();
        }
        newTile.setRotate(rotate);
        return newTile;
    }

    private int setRotate(int i) {
        switch(i) {
            case(0):
                return SOUTH;
            case(1):
                return EAST;
            case(2):
                return NORTH;
            case(3):
                return WEST;
            }
            return 0;
        }

    public double getTileHeight() { return tileHeight; }

    public double getTileWidth() { return tileWidth; }

    public GridPane getMyBoardView() {
        return myBoardView;
    }

    public BoardToolTip getToolTip(Location toolTipLocation){
        return myLocationsToToolTips.get(toolTipLocation);
    }

}
