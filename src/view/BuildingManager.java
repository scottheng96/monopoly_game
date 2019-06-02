package view;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import view.screen.GameSceneView;

import java.util.*;

/**
 * To show the construction and remove of houses on the board in the frontend.
 */
public class BuildingManager {


    private final String houseImagePath = "house.png";
    private final String hotelImagePath = "hotel.png";
    private ImageView[] myHouseView;
    private Image myHouseImage;
    private ImageView myHotelView;
    private Image myHotelImage;
    private Map<Integer, ArrayList<ImageView>> myMap = new HashMap<>();

    public BuildingManager(){

    }

    /**
     * To set a house/hotel at the correct location on the board.
     * @param myImageView The imageview of the house/hotel we want to build
     * @param xPos The correct X coordinates of the imageview
     * @param yPos The correct Y coordinates of the imageview
     */
    public void setLocation(ImageView myImageView, double xPos, double yPos) {
        myImageView.setX(xPos);
        myImageView.setY(yPos);
    }

    /**
     * To build/remove a house/hotel at the given location.
     * @param myGameScene The Scene of the game
     * @param root The root that contains all the elements that are shown on the screen
     * @param expectedHouses The expected number of houses at a certain location
     * @param maxHouses The maximum number of houses that could be built at a certain location
     * @param locationNum The number of the location where we want to build the house
     */
    public void setBuilding(GameSceneView myGameScene, Group root, int expectedHouses, int maxHouses, int locationNum) {

        double xPos = myGameScene.getTokenLocationCenters().get(locationNum)[0];
        double yPos = myGameScene.getTokenLocationCenters().get(locationNum)[1];
        double tileWidth = myGameScene.getTileWidth();
        double tileHeight = myGameScene.getTileHeight();
        int numPerSide = myGameScene.getNumberPerSide();
        double houseSize = tileHeight / maxHouses;

        myHouseImage = new Image(houseImagePath);
        myHouseView = new ImageView[400];
        myHotelImage = new Image(hotelImagePath);
        myHotelView = new ImageView(myHotelImage);

        int div = locationNum / numPerSide;

        if (myMap.containsKey(locationNum)) {
            for (ImageView i : myMap.get(locationNum)) {
                i.setVisible(false);
            }
        }
        if (expectedHouses < maxHouses) {
            Set<ImageView> Houses = new HashSet<>();

            for (int i = 0; i < expectedHouses; i++) {
                myHouseView[i] = new ImageView(myHouseImage);
                myHouseView[i].setFitHeight(houseSize);
                myHouseView[i].setPreserveRatio(true);
                if (div == 0) {
                    setLocation(myHouseView[i], tileWidth / maxHouses * i + xPos - tileWidth / 2, yPos - tileHeight / 2);
                }
                if (div == 1) {
                    setLocation(myHouseView[i], xPos + tileWidth / 2, tileHeight / maxHouses * i + yPos - tileHeight / 2);
                }
                if (div == 2) {
                    setLocation(myHouseView[i], xPos - tileWidth / 2 + tileWidth / maxHouses * i, yPos + tileHeight / 2);
                }
                if (div == 3) {
                    setLocation(myHouseView[i], xPos - tileWidth / 2, yPos - tileHeight / 2 + tileHeight / maxHouses * i);
                }

                root.getChildren().add(myHouseView[i]);
                Houses.add(myHouseView[i]);

            }

            myMap.put(locationNum, new ArrayList<>(Houses));

        } else {
            myHotelView.setFitHeight(houseSize * 2);
            myHotelView.setPreserveRatio(true);
            if (div == 0) {
                setLocation(myHotelView, xPos, yPos - tileHeight / 2);
            }
            if (div == 1) {
                setLocation(myHotelView, xPos + tileWidth / 2, yPos);
            }
            if (div == 2) {
                setLocation(myHotelView, xPos, yPos + tileHeight / 2);
            }
            if (div == 3) {
                setLocation(myHotelView, xPos - tileWidth / 2, yPos);
            }
            Set<ImageView> Hotels = new HashSet<>();
            Hotels.add(myHotelView);
            root.getChildren().add(myHotelView);
            myMap.put(locationNum, new ArrayList<>(Hotels));
        }
    }
}