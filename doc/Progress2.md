# Second Progress Presentation

## Features of your running program:

* Splash Screen
* Game Screen
* Board

## Team member contribution

* Scott: Board (frontend)
* Qinzhe: Management Screen/model.roll Dice Animation/Board (backend)
* James: Rules SetUp Screen/Player SetUp Screen/Connection between screens
* Leah: Game, Turns, CPU, PlayerStates, Rules
* Jordan: DataReader/DataWriter
* Arilia: Tooltips

## Data Files
* Player
* Load/Save games
* Added data file for the Rules

## Revisit the goal of the program: Flexibility of the program

* Keep Refactoring
* Enough communication between teammates
* Change the code a lot if we find a better design

## describe two APIs in detail (one from the first presentation and a new one):

#### API One - PropertySpace

## Java code for the API (either interface or abstract class, should not really be a concrete class)

## what service does it provide?

* Allows us to simulate a Property in Monopoly with functions such as being owned, charging rent, adding or removing
houses, and being mortgaged
* Keeps track of changing costs as well as status as buildable, part of Monopoly, etc.
* Runs through different scenarios upon be landed on to execute the actions that occur when a player lands on a Property
(getting chance to buy, needing to pay) 

## how does it provide for extension?
* Includes abstract updateRent() method - different property types are defined throughout Monopoly by how rent is calculated
* Allows any new property to be defined with unique manner of rent updating
* Also enables properties to add/alter landing actions by overwriting activateSpaceAction() method

## how has it changed (if at all)?
* Definition of owning changed to allow for easier access to Game properties
* Enabled knowledge of Monopoly (originally a Game function)   
* Way to buy property changed (orginally a Player function)

## how does it support users (your team mates) to write readable, well design code?
* Methods are named cleanly and descriptively 
* Methods are well commented, with any necessary notes for development included
* Deprecated Tags are added where necessary 




#### API Two - Rules 

## Java code for the API (either interface or abstract class, should not really be a concrete class)

#### Old API

```java
@Deprecated
public interface Rules {

    // BASED ON NEW DESIGN IDEAS THIS CLASS WILL BE SPLIT INTO INDIVIDUAL RULES


    /**
     * Assesses if a player can build on a given PropertySpace at the instantaneous state of the game
     * @param builder A player object that is attempting to build on its PropertySpace
     * @param propertySpaceToBuildOn A PropertySpace object on which the builder is attempting to build
     * @return a boolean answering the question, "can player x build on PropertySpace y?"
     */
    public boolean canPlayerBuildOn(Player builder, PropertySpace propertySpaceToBuildOn);

    /**
     * Checks if game has ended
     * @param allPlayers all player in game
     * @return boolean
     */
    public boolean isGameOver(Collection<Player> allPlayers);

    /**
     * Chooses who wins based on particular game rule
     * @param allPlayers all player in game
     * @return winning player
     */
    public Player getWinner(Collection<Player> allPlayers);

    /**
     * Decides if rent is necessary to pay - in some instances, a player might be unable to collect rent
     * @param lander player landed on PropertySpace
     * @param owner Owner of PropertySpace
     * @return boolean
     */
    public boolean doesLanderPaysRent(Player lander, Player owner);

    /**
     * Conducts the pass Go rule, since this commonly changes based on Game version
     */
    public void doPassGoRule();

    /**
     * Conducts the free parking rule, since this commonly changes based on Game version
     */
    public void doFreeParkingRule();

    public void doRollingDoublesRule(int numberOfTimesDoublesRolled, Player currentPlayer);

}
```

#### New API

```java
public abstract class Rule {


    public Rule(){

    }


    /**
     * Checks if rule should happen at this time in the game
     * @return boolean
     */
    public abstract boolean shouldRuleHappen(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers);

    /**
     * Does action associated with the rule
     * @return new status of game after the rule
     */
    public abstract GameStatus doRule(Board board, GameStatus gameStatus, Player currentPlayer, Collection<Player> allPlayers, Player bank);

}
```

## what service does it provide?

The Rule object helps at various stages of the game. Some examples of Rules are the "BuildingOnlyOnMonopolyRule" or "JailOnThreeDoublesRule". Previously, we tried to have a bunch of different rules in one class, however, that meant we would have to call very specific methods in different parts of the code. Now, each rule follows the same format via abstraction.

## how does it provide for extension?

Now, it is way easier to create a new rule. Just make a new Rule object, write the two methods in the new API, update the front end, and you are done. Earlier, you had to write new methods in the Rules class, find specifically where you wanted them to act on the game, and update the front end. 

## how has it changed (if at all)?

The class has been abstracted. As a result, the code is cleaner and more readable 

## how does it support users (your team mates) to write readable, well design code?


## Use cases

#### A player builds one house on a property in a monopoly, and the rent is updated. 

```java
RealEstate toBeBuilt;
Player player;

toBeBuilt.addHouse();
        //Within the method add house the following happens:
        //if (canHousesBeAdded()) {
        //            myNumberOfHousesPropertyObject.setValue(myNumberOfHousesPropertyObject.get() + 1);
        //            updateRent();
        //        }
```

#### A player lands on a location and the game uses the rule objects to apply the correct change

```java
Turn turn;

turn.rollDiceToMove();

WITHIN rollDiceToMove();
    myCurrentPlayer.changeState(myCurrentPlayer.getCurrentState().execute(myCurrentPlayer, myPlayers, myBank));
    rollDice();
    checkRules();
    for (int roll : myAllRolls) {
        myCurrentPlayer.move(roll);
    }
    checkRules();
```

## Design changed based on the progress of the project
* Problems implementing PlayerState
* Original design - PlayerStates as Enums, no functionality
* Drove game logic/worked but was confusing, difficult to edit/add to without breaking
* Group discussion about flaws of design - decision to make a change 
* Changed to abstract method within Enum that could be executed - more flexible, easier to add to, 
logic better contained/abstracted

## Final Sprint
* Finish the game
* Add some Easten Eggs
