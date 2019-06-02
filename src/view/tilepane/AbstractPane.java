package view.tilepane;

import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * This class is a pane.
 */
public abstract class AbstractPane {
    BorderPane myPane;
    static private final double fontsize = 3;
    static private final String font = "Verdana";

    /** This method constructs a pane
     * @param paneWidth the width of the pane
     * @param paneHeight the height of the pane
     */
    public AbstractPane(double paneWidth, double paneHeight) {
        myPane = new BorderPane();
        myPane.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        myPane.setStyle("-fx-background-color: white;");
        myPane.setPrefSize(paneWidth, paneHeight);
    }

    /** This methods gets a button pane.
     * @return BorderPane
     */
    public BorderPane getPane() { return myPane;}

    protected Text makeText(String text) {
        Text newText = new Text(text);
        newText.setFont(Font.font(font,fontsize));
        return newText;
    }

    protected Rectangle makeColorFrame(double frameWidth, double frameHeight, Paint color) {
        Rectangle frame = new Rectangle(frameWidth, frameHeight);
        frame.setFill(color);
        return frame;
    }

    protected void addName(String name){
        Text nameText = makeText(name);
        myPane.setCenter(nameText);
    }
}
