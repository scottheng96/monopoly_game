# Monopoly

##This project implements the game of Breakout.
Name: Arilia Frederick, Jordan Shapiro, Leah Schwartz, Scott Heng, Qinzhe Liu, Yuxuan Pan

## This project implements the game of Monopoly
#### Name: Arilia Frederick, Jordan Shapiro, Leah Schwartz, Scott Heng, Qinzhe Liu, Yuxuan Pan

##Timeline
Start Date: April 3, 2019
Finish Date: April 26, 2019
Hours Spent: 300 Hours

## Roles
#### Leah: 
* Model functionality: Game, Players, Cards, Trading, Auctioning, Transactions, Rules, PlayerStates/Game Logic
* Testing for Model classes
* View functionality: functionality of Trading, Auctioning, Managment, button logic, Popup buttons
#### Scott: 
* Board, tokens, player movement, player tabs, frontend data, popups, cards
* Controller class and game/turn logic
* Game variations and customization capabilities
#### Qinzhe:
* Splashscreens, frontend data reading, frontend panels, token choosing, frontend dice
#### Jordan:
* Backend data reading and writing, saving and loading games, 
* Backend Locations 
#### Yuxuan:
* Frontend houses, Rules frontend screen
#### Arilia:
* Tooltips
* Frontend testing

##Resources Used
Stack Overflow, Java Documentation (particularly for JavaFX), other documentation
has been referenced in comments in the code itself.


## Running the Program
Main class: MainGame.java

To start the program, simply build the project and run GameMain. It will set the stage, the scene
and instantiate the controller where the game will be created. 

## Files used to start the project 
Data files needed:
<<<<<<< HEAD
- folders: game_version, frontEndProperties, monopoly_junior, monopoly_regular, picture
- a multitude of png and jpg images (all are in the resource folder) e.g. arrow_icon, gamebackground.jpg

## Files used to test the project 
All of our test classes are in the "test" folder, where it is divided specifically
for frontend and backend testing. They used a combination of JUNIT and TESTFX,
and will run smoothly.

Errors that we have successfully handled invalid user input, and
 invalid data. A pop up is shown when these errors are handled.
 We also have enabled the ability to disable buttons on the frontend to prevent
 invalid user activity to prevent the program from crashing.

## data and property files that we need 
-main.css
-rule_numbers.properties
-tokenTypes.properties
-popup_messages.properties
-nonPropertyTilePictures.properties
-logging_messages.properties
-property and path files in monopoly_junior, monopoly_regular


## Key/Mouse inputs
When playing the game, during your roll turn, pressing any number key on the 
keyboard will allow you to move that specific number of spaces.


- folders: game_version, frontEndProperties, monopoly_junior, monopoly_regular, picture, testing

##Key/Mouse inputs:
* Once on the game screen, press any key to begin the game
* Press a number key to move player to a new space

##Notes
The testing for this project was primitive. Wanted to implement JUnit tests but it was difficult
considering we only had a lesson on in on the day that Game Complete was due. The game runs smoothly
otherwise, and the game scare and high score is updated each time we restart the game. We did our best
to make use of abstractions and inheritance with the power ups and the splash screen set ups. The
only thing that we did not abstract was setting up the game because there were many variables that
would have to be considered if we created an abstraction, and we felt that there would be an unnecessary
amount of getter/setters that we felt would make the main class too complicated.

##Impressions
This project was incredibly interesting and rewarding being able to see our final product that is
interactive and we would even say fun. This project also gave us great practice in making abstract
classes and understanding how javafx works, and we have gained greater confidence in utilising the
dynamic capabilities of object-oriented programming.
One interesting limitation that we constantly considered was the ability to make the game expandable
where others could take off from our code and make their own iterations of the game. Despite keeping
that in mind, it was difficult to design our code specifically to cater to its ability to be expanded on
and have a comprehensive main class that is fully functional.
From this project we have gained new insights about practically using inheritance effectively,
as well as how to create a game with javafx. We also learnt how to work with someone else effectively
through communication and cohesive planning, to utilize git branches and at best reduce the amount of
conflicts when merging, to make our progress more efficient.
Furthermore, the challenge of coordinating between six people was new and difficult.

##Known Bugs
* Sometimes the movement cheat key stops working, or causes the game buttons to gray out, although this has not happened
recently
* Popups occasionally freezes or slow, but will resume if the screen is minimized or just toggled slightly
* If you run a game with only CPUs, it could cause the program to become rather overloaded
