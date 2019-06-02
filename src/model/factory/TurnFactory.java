package model.factory;

import model.Board;
import model.roll.Roll;
import model.player.Turn;
import model.player.Player;
import model.rule.Rule;
import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;

/**
 * Represents a factory design to create new turns based on player's type
 *
 * Dependent on Player, Board, Rule, Roll classes
 *
 * @author leahschwartz
 */
public class TurnFactory {

    private static final String TURN_ENDING = "Turn";
    private static final String CLASS_PATH_SEPARATOR = " ";
    private static final int CLASS_PATH_FIRST_INDEX = 1;


    public Turn create(Board board, Collection<Player> players, Player currentPlayer, List<Rule> rules,
                       Player bank, Roll roll){
        Class c;
        try {
            String[] PlayerClassString2 = currentPlayer.getClass().toString().split(CLASS_PATH_SEPARATOR);
            String word = PlayerClassString2[CLASS_PATH_FIRST_INDEX];
            c = Class.forName(word + TURN_ENDING);
            Constructor constructor = c.getConstructor(Board.class, Collection.class, Player.class, List.class, Player.class, Roll.class);
            return (Turn) constructor.newInstance(board, players, currentPlayer, rules, bank, roll);
        } catch (Exception e) {
            return new Turn(board, players, currentPlayer, rules, bank, roll);
        }
    }

}
