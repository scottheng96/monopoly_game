package view.tilepane;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import model.location.Location;
import java.util.ResourceBundle;

/**
 * This class sets up the non property pane
 */
public class NonPropertyPane extends AbstractPane{
    public static final String GOTOJAIL = "GO TO JAIL";
    public static final String TAX = "TAX";
    public static final String RAILROAD = "RAILROAD";

    private BorderPane myPane;
    private ResourceBundle nonPropertyPhotos;
    private Image tileImage;

    /** This method constructs a non property pane
     * @param tileWidth the width of the tile
     * @param tileHeight the height of the tile
     * @param location the location
     */
    public NonPropertyPane(double tileWidth, double tileHeight, Location location) {
        super(tileWidth, tileHeight);
        nonPropertyPhotos = ResourceBundle.getBundle("nonPropertyTilePictures");
        myPane = super.getPane();
        ImageView tileImageView = getImageView(location, tileHeight);
        myPane.setCenter(tileImageView);
    }

    private ImageView getImageView(Location location, double tileHeight) {
        String keyName = String.join("_", location.getName().split(" "));
        if (nonPropertyPhotos.containsKey(keyName)){
            tileImage = new Image(this.getClass().getClassLoader().getResourceAsStream(nonPropertyPhotos.getString(keyName)));
        }
        ImageView tileImageView = new ImageView(tileImage);
        tileImageView.setFitHeight(2*tileHeight/3);
        tileImageView.setFitWidth(2*tileHeight/3);
        return tileImageView;
    }
}
