package model.factory;

import controller.exceptions.InvalidDataFileValueException;
import model.card.Card;
import java.lang.reflect.Constructor;

/**
 * Represents a factory design to create new cards through reflection
 *
 * Dependent on Card and subclasses
 *
 * @author leahschwartz
 */
public class CardFactory {

    private static final String CARD_CLASS_PATH = "model.card.";

    public Card create(String cardType, int cardNumber, int[] configurations, String deckType) throws InvalidDataFileValueException {
        System.out.println(cardType);
        Class cardClass;
        try {
            System.out.println(CARD_CLASS_PATH + cardType);
            cardClass = Class.forName(CARD_CLASS_PATH  + cardType);
            Constructor constructor = cardClass.getConstructor(int.class,  String.class, int[].class);
            return (Card)constructor.newInstance(cardNumber, deckType, configurations);
        } catch (Exception e) {
            throw new InvalidDataFileValueException("INVALID CARD TYPE");
        }
    }
}
