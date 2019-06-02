# Introduction
The problem that our team is trying to solve by writing this program is Monopoly, a board game named after concept of domination of market by single entity. More specific information about Monopoly can be found here: https://en.wikipedia.org/wiki/Monopoly_(game).
 
We hope to develop a project with good functionality, design, and analysis. Also, we want the project to be super flexible to adding new rules and changing specifics of the game. The heart of the game play should remain largely the same for every type of monopoly.

We will maximize the flexibility of the project by:
Using interfaces and abstract classes such as public abstract class Property 
Keeping classes, methods, and variables relatively short and making them serve single purposes.
Reducing direct dependence by referring to other classes through interfaces or intermediaries rather than directly when it is possible.
Using subclassing to avoid "case-based code logic". 
Minimizing the amount of duplicated code.

The primary architecture of the project is as below:
Resource files -- including all the data files such as board, players, properties, frontend tokens, ect.
MVC model -- model, view, controller.
1. Model: The central component of the pattern. It is the application's dynamic data structure, independent of the user interface. It directly manages the data, logic and rules of the application.
2. View: Any representation of information, visualizing the game play. It also takes responsibility for the front end of the whole project.
3. Controller: Accept user inputs during the game and convert them to commands for the model or view.
APIs -- application programming interface that helps build a variety of games in Monopoly.


# Overview
Our program will be divided into the standard Model-View-Controller set-up. 
View will have a BoardView class that will read a configuration file to set 
up the board in the specified order. There will also be an abstract Button 
class that allows for the creation of different buttons in order for players 
to buy and sell properties. The controller is where gameplay takes 
place, and all of the behind the scenes work to carry out actions is handled. 
The model is a large section built by many classes working together to model
such important things as players and their estates, movement around the board,
paying rent, buying and selling houses and properties, and mortgaging.

A robust view of our design can be seen here:
[Image of design overview](https://ibb.co/HCLgbJW)

The model module will be split into smaller modules based on inheritance hierarchies.
For example, an important module will be the Location module, which will deal with
all Locations in the game, including purchasable properties and other game Locations
where certain things happen when a player lands. 


# User Interface
Our game forces the user to navigate selection dropdowns and clickable buttons
in order to limit as many errors as possible. Some actions, like auctioning, will require
text input that will need to catch errors (for example, a non-numeric input for
a dollar amount). Otherwise, we have limited in-game user input to clickable properties 
on the actual board and a small section of buttons which enable and disable based
on whether or not it is your turn, the location you are at, and other factors depending
on which version of monopoly you are playing. We plan on using configuration files and
math to generate different numbers and types of tiles in order to make sure that no 
matter the size or number the game board itself looks clean and presentable. 

Another important aspect is the game status which will change the center of the 
board's view based on what space is rolled and direct users toward actions to help 
facilitate game play. Tooltips and other hoverables will allow the player to know 
what is going on and what options they have at any given time. 



# Design Details

## Front End
There will be an abstract class named Panel so that the three panels mentioned above (the one to choose tokens, the 
one to control the player, and the one to control the game) will be able to extend from that abstract class. 
All the panel will only contain buttons. The buttons will be implemented through a design factory. The button will also 
be an abstract classes and there are different types of buttons extending from it. The one to choose tokens need to read 
pictures from the data files. The one to control the game (load/save/quit) will need to deal with writing and reading the 
configuration files, but those are not big issues. 
Finally, the board will have a reader to read all the pictures and the set up from the data files. Then the board will 
set up the map according to those pictures and those data files passed to the board. 

## Back End
The controller and heart of the project is the Game class. This will contain mostly private methods, as it is at the 
top of the project hierarchy. The board itself is a implemented on the backend as a linked list of Locations, an abstract 
class that is then further abstracted into Properties vs un-ownable spaces. Properties then diverges into such categories
 as RailRoads, Utilites, and RealEstates. The users of the game are modelled as Players. Players and Locations 
 communicate to each other via a suite of methods, and these largely define the gameplay interactions. We also 
 include further specifications such as Dice and a special GameSetUp class. All of these can be customized using 
 configuration files. Our hope is that this design is will allow us to flexibly change the dice, the board, 
 the properties on the board, and other fundamental features of the game.
 


# Example Games

We will attempt to implement 3 example games with different variations: 1. Monopoly Junior 2. Monopoly Millionaire 3. Monopoly custom games
These variations of the game will be easily implemented because our design maximises the uses of abstractions and inheritance.
Since all of our locations will be defined by resource bundles, to change the location names, rent amount and other properties of propertySpace 
we will simply use new resource bundles. As each of the variations have different games, especially for user customization, we have 
implemented a rule/ruleset class so as to apply new rules that will override the original rules of the game, which will be called by
each location upon the user landing on the location. Since card is also an abstraction, it will be easy to create new and creative
assets and chance/community cards that will vary game play depending on the desires of the user.


# Design Considerations

## Front End
The design of the frontend of the project is simple and straightforward. The frontend will be splitted into three parts: Board, a panel allowing the user to choose the token, a panel allowing the user to control the player when the game is in progress. Each part will be independent from others, and a general panel that allows the user to save/load/quit the game. The panel allowing the user to choose the token will appear at the beginning of the game. The users will take turns to choose their tokens. After that, the Board system will set up the board (the map). During the game, the Board system will display the houses on the map and also some general information about the game like the amount of the money that bank has and the number of houses that are allowed to be built. Finally, in each player’s turn, there will be a control panel that allows the user to control his player. He can roll the dice, buy houses, build hotels, etc. There will also be another panel that allows the user to save/quit the game at anytime he wants. That panel will also be displayed on the screen. 
There will probably be other features that we want to implement: for example, we may want to switch the view when a player is buying things, that will require a different view system. However, that is not too hard and that view system will be completely independent from the previous systems.
Conclusively speaking, we want to separate the frontend as much as possible, and that’s our key of design.

## Back End
One consideration that we need to resolve before completing a full design is how to deal with issues of specific
subclasses. Based on the abstract hierarchy we have come up with to represent all the places in a game, we may only know
that Players are on Locations and we need to have the subclass of Locations, Properties, in order to perform certain 
methods. This needs to be fixed using a strategy such as generics or changing the hierarchy in some way, which we are
still trying to figure out.

One major decision we discussed at length is how Players and the things they own, such as Cards and Properties, should 
be related. We wanted a way to make it very clear what a Player owned but at the same time, we wanted it to be flexible.
We very much considered having a Player hold all of the Properties and Cards that they owned and 
the Property or Card knowing nothing about its owner. However,
this created issues with making the Properties and Cards active classes. It also made it difficult 
to know how the Property could function. For example, if there was a Monopoly on Properties of that 
group or making sure the owner would get paid when someone landed on their propertySpace. Properties and Cards both need the 
Player to do certain things, such as resolve being landed on or performing a special Card action.  On the other hand, 
Putting the Property in would make it easier to sum up the total assets that belong to a Player.
Therefore, we decided Properties do need to know their owner, but Players also should know which 
Properties they own. These two will always need to be consistent and well-designed, and making sure this 
 relationship works will be a focus in our first sprint.

