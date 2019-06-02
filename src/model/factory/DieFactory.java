package model.factory;

import controller.exceptions.InvalidDataFileValueException;
import model.roll.Die;
import java.lang.reflect.Constructor;

/**
 * Represents a factory design to create new dices through reflection
 *
 * Dependent on Die and subclasses
 *
 * @author leahschwartz
 */
public class DieFactory {

    private static final String DIE_CLASS_PATH = "model.roll.";

    public Die create(String dieType, int dieSides) throws InvalidDataFileValueException {
            Class c;
            try {
                System.out.println(DIE_CLASS_PATH + dieType);
                c = Class.forName(DIE_CLASS_PATH + dieType);
                Constructor constructor = c.getConstructor(int.class);
                return (Die)constructor.newInstance(dieSides);
            } catch (Exception e) {
                throw new InvalidDataFileValueException("INVALID DIE TYPE");
            }
        }
    }
