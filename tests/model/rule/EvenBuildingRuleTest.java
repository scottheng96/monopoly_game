package model.rule;

import model.Board;
import model.GameStatus;
import model.roll.NSidedDie;
import model.roll.Roll;
import model.location.Location;
import model.location.MoneySpace;
import model.location.propertySpace.PropertySpace;
import model.location.propertySpace.RealEstate;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class EvenBuildingRuleTest {

    private Rule myRule;
    private List<String> playerNames = new ArrayList<>(Arrays.asList("LEAH", "JORDAN", "Q"));
    private List<Player> myPlayers;
    private Map<Integer, Integer> rents = new HashMap<>();
    private Location start = new MoneySpace("GO", 0, 300);
    private PropertySpace yellow1;
    private PropertySpace yellow2;
    private PropertySpace blue;
    private Location end = new MoneySpace("END", 1, -300);
    private List<Location> myLocations;
    private List<PropertySpace> myProperties;
    private Board myBoard;
    private Bank myBank;
    private Roll myRoll = new Roll(new ArrayList<>(Collections.singleton(new NSidedDie(4))));


    @BeforeEach
    public void setup(){
        myPlayers = new ArrayList<>();
        myRule = new EvenBuildingRule();
        for (String name : playerNames){
            Player player = new HumanPlayer(name);
            player.increaseMoney(100);
            myPlayers.add(player);
        }
        rents.put(0,1);
        rents.put(1,2);
        rents.put(2,2);
        yellow1 = new RealEstate("yellow1", 2, "YELLOW", 2, 300,20, rents);
        yellow2 = new RealEstate("yellow2", 3, "YELLOW", 2, 300, 20, rents);
        blue = new RealEstate("blue", 4, "BLUE", 1, 300, 20, rents);
        myLocations = new ArrayList<>(Arrays.asList(start, end, yellow1, yellow2, blue));
        myProperties = new ArrayList<>(Arrays.asList(yellow1, yellow2, blue));
        myBoard = new Board(myLocations);
        myBank = new Bank("BANK");

    }


    @Test
    void shouldRuleHappen_ruleShouldAlwaysHappen() {
        assertTrue(myRule.shouldRuleHappen(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myRoll));
    }

    @Test
    void doRule_EvenlyBuiltPropertiesAreBuildable() {
        for (PropertySpace propertySpace : myProperties){
            propertySpace.changeOwner(myPlayers.get(0));
            propertySpace.setHouseCanBeAdded(true);
        }
        yellow1.addHouse();
        yellow2.addHouse();
        myRule.doRule(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myBank, myRoll);
        assert(yellow2.canHousesBeAdded());
        assert(blue.canHousesBeAdded());
    }

    @Test
    void doRule_OneHouseBehindPropertiesAreBuildable() {
        for (PropertySpace propertySpace : myProperties){
            propertySpace.changeOwner(myPlayers.get(0));
        }
        yellow1.addHouse();
        myRule.doRule(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myBank, myRoll);
        assert(yellow2.canHousesBeAdded());
    }

    @Test
    void doRule_NotEvenlyBuiltPropertiesAreUnbuildable() {
        for (PropertySpace propertySpace : myProperties){
            propertySpace.changeOwner(myPlayers.get(0));
        }
        yellow2.setHouseCanBeAdded(true);
        yellow2.addHouse();
        myRule.doRule(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myBank, myRoll);
        assertFalse(yellow2.canHousesBeAdded());
    }


    @Test
    void doRule_OwnerIsBrokePropertyIsUnbuildable() {
        for (PropertySpace propertySpace : myProperties){
            propertySpace.changeOwner(myPlayers.get(0));
        }
        yellow2.setHouseCanBeAdded(true);
        myPlayers.get(0).withdrawMoney(100);
        myRule.doRule(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myBank, myRoll);
        assertFalse(yellow2.canHousesBeAdded());
    }

    @Test
    void doRule_NotMonopolizedPropertyIsUnbuildable() {
        yellow1.changeOwner(myPlayers.get(0));
        yellow2.changeOwner(myPlayers.get(1));
        yellow1.setHouseCanBeAdded(true);
        myRule.doRule(myBoard, GameStatus.PLAYING, myPlayers.get(0), myPlayers, myBank, myRoll);
        assertFalse(yellow1.canHousesBeAdded());
    }

}