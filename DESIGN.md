# DESIGN

## High Level Design Goals

Our goals for this project were to synthesize and utilize the design tools that we have obtained over the course
of the semester in order to make a functional, flexible, and extendable virtual version of Monopoly. We tried to
take concepts from each of our previous projects that we felt we now better understood. The first of these were
the concepts of abstraction and encapsulation, which we first learned during Game. We attempted to abstract Monopoly
by breaking it into small and simple pieces, which we hoped to make flexible enough to mix and match in our final
product. We also tried to focus our efforts on encapsulation, in order to be able to change inner workings of our
game while keeping an overall consistent API. This was especially important because of the large team size and need
for coherent and compatible code.
The next design idea we brought came from Simulation, which taught us about the Open-Closed principle. We really
wanted to try and keep certain portions of the project open to extension. In particular, we felt that adding new
game variations, new rules, and new spaces were some of the most important aspects of building an effective and
flexible project. As such, we tried to build inheritance hierarchies around these features.
We also brought the MVC design model from Simulation. Initially, we divided ourselves into model, view, and controller
team members and although these roles shifted somewhat, we all committed to keeping our code clearly divided. This
meant sometimes having lots of code in controller classes, but we felt this was worth it in order to keep the model
independent from its depiction and to try and keep as much of the model out of the view as we could. We hope this
will allow for code reusablility.
Finally, we wanted to use new designs that we have learned over the course of the last month. One concept we found
to be very powerful was reflection, which we felt could make it easier to be data driven by allowing other coders
to add new subclasses that would automatically be incorporated into the game. This was our approach for important
features we wanted to keep extendable, such as rules. We also tried to integrate reflection into our view by using
it to decrease hard coding in areas such as buttons where we saw it possible to do so. In addition to reflection, we
focused on looking at design patterns, like the observer pattern to get rid of too much querying and the factory
pattern. These designs allowed our code to be better structured and organized.

## Adding New Features 


### Adding New Rules
Since rules are at the heart of the game, we wanted to make it very simple to add new ones.

* Create a Rule in the rule package
* Extend the abstract rule class, which calls for two methods to be implemented: shouldRuleHappen() and doRule()
* shouldRuleHappen() will determine if doRule() is called
* doRule() will perform some game changing action
* Include any new rule names in the Rules section of the rules.properties file included in a game folder
* The rule will automatically be created through reflection and checked/possibly executed whenever checkRules() runs

### Adding New Location Types
* Create a new class that extends either the PropertySpace class (if you want it to be ownable/buyable) or the abstract
Location class
* Implement the activateSpace() method, which should perform some action, likely affecting the player in some way
* Incorporate the new Location type (unless it is a PropertySpace) into the Location factory to account for any parameters
* Change/make new data files in the board directory in the game folder of your choice that use this location type

### Adding New Card Types
* Create a new class that extends the abstract Card class
* Implement the doAction() method, which will be called when a card is drawn if that card is not "holdable"
* Create data files for the card backend as well as in the card frontend properties file to decide what message
 you want displayed to the user 
* The card will automatically be created in the CardFactory, using reflection

### Adding a New Token
* Copy the file image (jpg, png etc) into the resource folder
* go to the tokenTypes.properties file, and add one to the total number of tokens
* then add a path to your token by adding a row with the key "token" + an incremental number and the name of the file image "xxxx.png" 


## Design Decisions

### PlayerStates

Figuring out how to represent Game logic was a real challenge and for a while, it was bleeding throughout the whole
Game. At first, PlayerStates was just an Enum without any associated functions. We initially just wanted the Game
class to handle all that logic. However, this became very confusing. There were far too many conditionals in our code.
One positive we saw was that it seemed like a centralization of the game logic, but we could also see as the game
grew that our code in the Game class was getting extremely long. Additionally, other classes were having to constantly
set or check the player's state and we wanted to reduce that need. One suggestion was to use an abstract class for the
player's state with concrete implementations. This, however, would make it challenging to know what state the player is
in for any other class, because they would have to check the subclass of the state object. In the end, we choose to
use an Enum but to implement an abstract method for it called execute() which would allow the state to use the information
it was given to change itself. This decision really helped our design by getting rid of many conditionals and instances
of setting the player's state. It also allowed the playerState to execute a good portion of the game logic itself. One
downside of this was that an Enum cannot take in and store dynamic variable passed into its constructor for use in methods,
meaning that the parameters required to execute its state change had to passed in in several different places in the code.
Another flaw is that the design can make the code harder to understand. Seeing playerState.execute() does not tell someone
unfamiliar with the code exactly what the player is doing. Overall however, this design choice cleaned up the code and made
it less conditional, making it a better design than our other considerations.

### Rules 

Another design decision that took quite a while was to represent Rules in our game. The idea of changable rules felt
very abstract and confusing initially. For this reason, our first design was quite flawed. We originally wanted to
have a Rules class, which was basically just a collection of methods that comprised the rules set for that game. It
would have required subclasses for any new rules. This was particularly inflexible because it meant large code changes
or additions for an overall small game change. The benefit is that it would have made it very clear which rules belonged
to which game and made the process of designing an entirely new rule set straight forward, but it would not have allowed
for mixing and matching rules or turning certain rules "on" and "off". Having dynamic rules that change in game would
have been very difficult to do. One way we considered changing this design was to have a superclass for rule which had
abstract subclasses of rules grouped together by type, for example, winning rules would all extend a winning rule class.
This seemed neat because it would enable us to know exactly when to check each type of rule, however it was also limiting
because it would not allow for more than one rule of a type at a time and it would also put rules into strict subsets.
Defining a new subset would need lots of code changes. Finally, we decided to have a single abstract rule class, which
required methods shouldRuleHappen() and doRule(). This enabled us to have many different rules, as well as made writing
and integrating new rules relatively easy. The downside is that all the rules are checked together, so there needs
to be careful and specific logic to check if a rule should happen. This part of the class is quite conditional, but we
felt the tradeoff was worth it for the flexablility it offered.

### Pop Ups

Part of our design decisions centered around flexibility and reproducibility. We therefore initially
chose to create inheritance classes of an abstract class and call their constructors when a person would land
on a particular location, depending on the resolution type or payment, buying or movement. We decided that since
the back end was governed largely by player states, we would create a instance of the pop up class depending on the player
state and fill it data and its button functionality respectively. Using reflection, we created a pop up factory
that would make different pop up with the same number of inputs, the least amount of information required to create
the appropriate pop up (the current player, the collection of players, the bank and the roll). Depending on the player 
states, pop up would decided be shown. For example, there player states of pre-roll and post-roll would not show the pop up
when the show() method was called, while player states buying and drawing_card would. Using the pop up factory,
it was easy to create different kinds of pop ups. This design decision is also easily extendable, if in future other
coders would like to create more pop ups, but would have to adhere to the functionalities of the back end of changing player states.

### Location Design

Our initial design was that we would have two interfaces/abstract classes, one for locations, and one for holdable objects. Locations would extend/inherit 
both, representing both the board and holdable card. Furthermore, we planned a heirarchy of locations: specific location extensions such as jail, free 
parking, go to jail, and so on. The properties would have their abstract extension of location, with concrete instantiations of real-estates, railroads, 
and utilities. 

Quickly, the holdable interface was ditched. And instead of an abstract location, we thought that we would have a concrete location for the 
representation of the board. Then, each location would hold a list of space objects. These space objects would be several different classes extending an 
abstract class Space. The specific Space classes would be things like, payment space, teleport space, ... When a player lands on a location, we could 
iterate through the list of Spaces and call one method that each one has to enact the multiple different types of actions. The reason for this change was 
that (at the time) we wanted to be able to have more than one types of functionality at the same location. We wanted the flexibility to customize a game to
the extent where the user could create a space that simultaneously could be owned, charge rent, act as a jail, collect a tax, give money (like in house 
rules free parking)...

This design was subsequently cut, as we found challenges in this implementation. Furthermore, we thought that it was fine to expect a certain level of 
concreteness such that the game was still monopoly. Therefore, we went back to an abstract Location. However, instead of specific instances of Location, 
such as Go and Luxary tax, we instead made more general Locations. For example, we have a MoneySpace that extends Location and functions as go and tax 
locations (giving negative money to the player). Furthermore, we still have the abstract extension of Location, PropertySpace. These are the ownable 
locations. This allowed us to best utilize polymorphism to make our code flexible and extendible.

### Front end design decisions

We have two abstract classes. One for splash screens, while the other one for game screens. The reason we 
decided to use two abstract classes is that we realized that splash screens are independent from game screens. The 
splash screens take the responsibility to set up the game, while the game screens have nothing to do with the splash screens.
To be more specific, the splash screens include all the welcome screen, the rules set up screen, the players set up screen, etc.
While the game screens in fact only include one screen called GameViewScene, but there are a bunch of sub screens like those 
for panes on the GameViewScene. Notice that in order to distinguish the difference between the game screens and the splash screens,
the two abstract classes were named differently, one ended with "scene", while the other one ended with "screen".

Another major decision we make was that we don't need to make a button factory. The reason is that we are pretty sure how each button 
will function, and therefore, there is no need to make a button factory. Making a button factory doesn't make our life easier because 
we put the listener to the controller, that is to say, no button will be functional without the help from the controller, and if we had 
a button factory, then we would only set up the position for each button in the button factory. Therefore, we decided to set up buttons 
on each screen instead of using a button factory.

Thirdly, and most importantly, we decide to use a controller to connect different screens. That's to say, we won't be able to change 
from one screen to another screen without a controller. In the controller, if we want to change from one screen to another, the controller
will just initialize a new screen. The screens are therefore independent from each other. Therefore, if something dramatically changed 
for one screen, nothing would need to be changed for the screen appearing before/after it.

Finally, during the game, though every pane appears in the same screen, we try to make them independent of each other. For example, the 
screen that dice number appear is completely independent of the screen for the management pane. Such a decision makes us change part of 
the screen much easier.

## Assumptions and Simplifying Decisions 
There are several major assumptions and simplifying decisions that we made during development. 
1. **At least one human player.** Our CPUs take their turns on their own. If there aren't any human players, the CPUs will 
rapidly cycle through their turns at a speed too fast for the log to display that would make it an enjoyable experience. 
Therefore, popups do not appear for CPU actions. You can see the actions they take in the log, in the tooltips, and their
management panel when playing with CPUs.

2. **Limited Player Input** Using UI controls like ChoiceBoxes and Spinners in place of text input areas allowed us to 
implement a simple error checking method that disabled the Continue buttons until all areas are filled. This way, we did 
not have to check for valid values during new games since the input was restricted to correct values. 

3. **Similar screen resolutions** In order to focus more time on the backend implementation, we assumed that all players 
of our monopoly program would be on similar screen resolutions. This was because the UI elements we used to build our 
game were not the most responsive to screen resizing. Thus, it is best played on fullscreen. Also, based on the game 
versions we chose, it allowed us to keep a square board that looked connected at different sizes.

