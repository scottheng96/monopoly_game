## Implementation Plan
We will implement the following features for backend: systems which allow the user to buy, to sell, and 
to trade properties (Leah, Jordan); a player system which allows the players to move on the board, doing specific actions 
when they land on each spot, such as buying a propertySpace, picking up a card, or going to the jail,
and to be influenced by different types of cards (Leah, Jordan, Qinzhe); a board system that 
contains locations (Jordan, Leah, Qinzhe); a file reader/writer that allow the user to start a new game, 
to load the previous game, and to save the current game (Qinzhe). 
We will also implement the following features for frontend: a control panel at the beginning which allows the user 
to choose different tokens (James, Scott); a control panel that allows the user to do trading and other stuff 
when the game is in progress (James, Scott, Arilia); several nice looking boards (Arilia).

![Splash Screen](https://coursework.cs.duke.edu/compsci307_2019spring/monopoly_team07/blob/master/doc/plan/GUI/monopoly_start%20splash%20screen.png)

![Game Set Up Scree](https://coursework.cs.duke.edu/compsci307_2019spring/monopoly_team07/blob/master/doc/plan/GUI/monopoly_set%20up%20screen.png)

![Game Screen](https://coursework.cs.duke.edu/compsci307_2019spring/monopoly_team07/blob/master/doc/plan/GUI/monopoly_game%20screen.png)

![Other](https://coursework.cs.duke.edu/compsci307_2019spring/monopoly_team07/blob/master/doc/plan/GUI/monopoly_various%20game%20statuses.png)


### Individual Parts
Jordan and Leah will be responsible for the backend. Arilia and Scott will be responsible for the frontend.
Qinzhe and James will work on both parts, but Qinzhe will primarily focus on the backend (specifically data), while James will primarily 
focus on the frontend.

### Challenge Feature
Trading: players can trade with each other during the game.
Player profiles, such as saving, viewing high scores, and saving preferences.

### Sprint Features
#### Sprint 1 Features
FrontEnd: main screen (showing the board), create menu screen for rolling, trading, ending turn
Functionality to start game, roll, end turn button
See player move across board 
Make button to change configurations but does not need to be functional yet

BackEnd: 
Being able to read basic data files
Set up the game (all the initial conditions of the game), including building the structure of the map 
Human Players
Players should be able to move, roll and buy properties
Being able to start the game properly
Have functionally changing PlayerStates 


#### Sprint 2 Features
FrontEnd: all the screens (splashscreen, board, gamestatus screens, buttons, menu)
Add ability to choose player tokens
Make sure all buttons work - buying houses, mortgaging and unmorgaging 
Adding more features to the location (pictures of flags, landscape, etc.)
Adding multiple maps
Be responsive on Frontend to PlayerStates

BackEnd: 
Make sure all the buttons have corresponding implementations in the backend
Players will be able to build houses, and do all the trading activities. 
Players will be able to be influenced by basic Wildcards.
Automated Players
Add at least one implementation for variations of Game


#### Sprint 3 Features
FrontEnd: all the screens (splashscreen, board, gamestatus screens, buttons, menu)
Make sure all buttons work
Make the whole screen look more beautiful
Front end implementation of player profiles
Adding more fancy features to the frontend, such as more configuration options house depictions

BackEnd: 
Players can choose different maps or rules to play with. - dynamic changes to play different variations
Player Profiles
Add many more variations of Wildcards
Add more data and configurations

## Design Plan

### Overview
* Front-End- Start Game and Setup Game splash screens, and game screen. game screen will consist of player blocks
that contain player's information (money, properties), a visual display of the board with player tokens, a game status
bar in the middle of the board (to visualise game flow) and a menu bar where players will interact with the program.
* Buttons is abstract, board is a set of tiles, tiles is abstract. Front-end will largely consist of objects, and methods
* that take user input and communicates with the controller
* Controller and heart of the project - Game class. This will contain mostly private methods, as it is at the 
top of the project hierarchy. 
* Board itself is represented by Locations, an abstract class that is then further abstracted into 
Properties vs un-ownable spaces. 
* Properties then diverges into such categories as RailRoads, Utilites, and RealEstates. 
* Users of the game are modelled as Players. 
* Players and Locations communicate to each other via a suite of methods, and these largely define the gameplay interactions. 
* Rules class for handling specific game rules
* Further specifications such as Dice and a special GameSetUp classes can be customized using 
 configuration files. 

Our hope is that this design is will allow us to flexibly change the dice, the board, 
the properties on the board, and other fundamental features of the game.

### Design Goals
Goals to maximize the flexibility of the project:
* Using interfaces and abstract classes such as public abstract class Property  
* Keeping classes, methods, and variables relatively short and making them serve single purposes.
* Reducing direct dependence by referring to other classes through interfaces or intermediaries rather than directly 
when it is possible
* Using subclassing to avoid "case-based code logic". 
* Eliminating sources of duplicated code through hierarchies.

Primary architecture goals of the project:
* Using Resource files for all configurations to make data easily alterable -- includes data files for board, players, 
properties, frontend tokens, etc.
* MVC model -- model, view, controller
* Encapsulation and abstraction in order to not bleed parts of the project together

We want to keep features that we think are most commonly switched out in different versions open to extension. 
This includes making changes to the board, both in model and view, adding new space types, adding any type of
wildcards, using different rules, and having wildly different starting configurations.
Parts that are more closed are specific subclasses, such as RailRoads. In this example, RailRoads are very much
defined by how the rent is determined and changed, and so if such a feature changes, this space isn't really the
RailRoad anymore, but rather a new type of Property, which should just be created rather than trying to make
alterations to the existing subclass. 

### APIs 

#### API 1
One API is the Player API. This API serves as a representation of someone participating in the Monopoly game. 
The Player class:
 * Is abstract 
 * Has implemented methods for actions such as getting the amount of money the player has, buying a new Property, or 
 finding out how many turns the player has been in jail. Player is flexible and extendable because it is
abstract and can have different variations. 
* Has an abstract method that changes the player's state, which reflects what a player is currently doing. 

We thought that by making the state changing method flexible, it would make it easier to add other types of players, 
such as automated players. When human players have a state change, there is frontend action that will decide what happens
next, whereas if the player is automated, no such input is need. The ability to change the implementation of this method
will make it simpler to add a very different type of player in the future. 
This API supports teammates by telling them exactly what information they can know about a player and by giving
them an easy way to change certain components of the frontend, based only off the Player's state.


#### API 2
Another API is the Property API. This API serves as a representation of the Properties on the board in a Monopoly game.
The Property class:
* Is abstract
* Inherits from Location class
* Has implemented methods for actions such as building houses, mortgaging, and resolving when a player lands on it.
* Has an abstract method for updating rent, which would need to occur when the owner of a group member propertySpace changes
or the houses on a group member propertySpace changes 

We feel this API is provides for extension because more Properties can be easily created without altering existing
ones and common methods are already written and usable. We do not have to change RailRoads or RealEstate Properties, 
but rather can just create another variation if we need a very different type of Property that decides its rent
in a new manner.
This API supports teammates by encapsulating the behavior of a Property and only giving them essential information,
such as how many houses are on the Property or if the Property is mortgaged, rather than letting all the functionality
bleed into the controller class.


### Use Cases
```java
21. Player rolls dice, gets doubles, and gets to roll again after they move.
 public void useCase21() {
        player1.changeState(ROLLING); // The player is rolling.
        int roll = dice.roll();
        player.move(roll);]
        Location spot = player.getCurrentLocation(); LocationT
        spot.resolveLanding(player1); LocationT
        if (dice.gotDoubles()) { // Checks if the roll was doubles.
            player.changeState(PRE_ROLL) // It is doubles so this branch of the conditional will be chosen.
        } else { player.changeState(POST_ROLL); }
        // Because the state is PRE_ROLL we are waiting for human to select trade or sell or ROLL from the menu of options
        player.changeState(ROLLING) // The player rolls.
        roll = dice.roll():
    }

12. Player builds a house.
 public void useCase12(){
        if (rules.canPlayerBuildOn(Player player1, Property myProperty){
            cost = myProperty.getNextHouseCost();
            myProperty.addHouse();
            player1.withdrawMoney(cost);
        }
    }
```

### Alternative Design
* Major issue: How classes interact with each other to keep them active/distribute intelligence
* Ex. Relationship between Players and things they own (Cards, Properties)
* Decision on if classes should be aware of each other, which uses the other?
* Need to make clear what a Player owns for summing money, assests, trading, building, etc.
* But Properties also need to know who owns them for monopolies, costs, paying owner, etc.
* Decision to have dual awareness to increase functionality that can be encapsulated in each
* Will require careful design to ensure  consistentency - relationship will be a focus of first sprint
