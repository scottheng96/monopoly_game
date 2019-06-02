package view.popup;

import controller.FrontEndDataReader;
import controller.exceptions.InvalidDataFileValueException;
import model.roll.Roll;
import model.player.Player;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Map;

/**
 * This class sets up a factory that can be used to make all the pop ups.
 */
public class PopUpFactory {

    private static final String POP_UP_PATH = "view.popup.";
    private static final Map<String, String> myCardTextsMap = null;

    public PopUpFactory()  {
    }
    /** This method creates a pop up.
     * @param myCurrentPlayer current player of the game, we put "my" at the beginning to show that this is the constructor
     *                        of the parent class.
     * @param allPlayers all the players of the game
     * @param bank an imaginary player that serves as a bank
     * @param roll roll
     * @param myFrontEndDataReader a reader that can be used to read front end data
     */
    public AbstractPopUp create(Player myCurrentPlayer, Collection<Player> allPlayers, Player bank, Roll roll, FrontEndDataReader myFrontEndDataReader) throws InvalidDataFileValueException {

        Class c;
        try {
            String popUpName = makePopUpClassName(myCurrentPlayer.getCurrentState().name());
            c = Class.forName(POP_UP_PATH + popUpName + "PopUp");
            Constructor constructor = c.getConstructor(Player.class, Collection.class, Player.class, Roll.class, FrontEndDataReader.class);
            AbstractPopUp popUpInstance = (AbstractPopUp) constructor.newInstance(myCurrentPlayer, allPlayers, bank, roll, myFrontEndDataReader);
            return popUpInstance;
        } catch (Exception e) {
            System.out.println("There is not such popUp");
        }
        return null;
    }

    private String makePopUpClassName(String name) {
        String[] PUArray = name.toLowerCase().split("_");
        for (int i =0;i<PUArray.length;i++) {
            PUArray[i] = PUArray[i].substring(0,1).toUpperCase() + PUArray[i].substring(1);
        }
        return String.join("", PUArray);
    }

    public Map<String,String> getMyCardTextsMap() {return myCardTextsMap;}
}



