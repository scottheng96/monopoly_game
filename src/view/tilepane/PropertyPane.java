package view.tilepane;

import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class PropertyPane extends AbstractPane {
    private BorderPane myPane;

    public PropertyPane(double tileWidth, double tileHeight, String tileName, String color, int cost) {
        super(tileWidth,tileHeight);
        myPane = super.getPane();

        final double frameHeight = tileHeight / 4;
        Rectangle colorFrame = makeColorFrame(tileWidth, frameHeight, Color.valueOf(color));
        Text nameText = makeText(tileName);
        nameText.setTextAlignment(TextAlignment.CENTER);

        Text pCost = makeText("Cost: "+cost);

        myPane.setTop(colorFrame);
        myPane.setCenter(nameText);
        myPane.setBottom(pCost);
        addName(tileName);
    }

}
