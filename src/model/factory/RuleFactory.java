package model.factory;

import controller.exceptions.InvalidDataFileValueException;
import model.rule.Rule;
import java.lang.reflect.Constructor;

/**
 * Represents a factory design to create new rules using reflection
 *
 * Dependent on Rule and subclasses
 *
 * @author leahschwartz
 */
public class RuleFactory {

    private static final String RULE_CLASS_PATH = "model.rule.";

    public Rule create(String ruleName) throws InvalidDataFileValueException {
        System.out.println(ruleName);
        Class c;
        try {
            System.out.println(RULE_CLASS_PATH + ruleName);
            c = Class.forName(RULE_CLASS_PATH + ruleName);
            Constructor constructor = c.getConstructor();
            return (Rule)constructor.newInstance();
        } catch (Exception e) {
            throw new InvalidDataFileValueException("INVALID RULE NAME");
        }
    }
}
