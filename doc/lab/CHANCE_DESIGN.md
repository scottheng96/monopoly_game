Two ideas for chance/community cards:

1. Abstract class 'card'

The abstract class card will be extended by different types of cards (e.g. change player's money, move 
player's location).

Each card will have these variables:
* boolean Asset
* int money
* int location
* Shape card
* Text cardText

The each card will have the following methods:
a. getDrawn() 

* The getDrawn method is the same for all cards, it will be called by the player upon stepping on 
a chance/ community square.
* the getDrawn card will check first if it is an asset or not from the Asset Boolean. If the asset boolean
is true, it will be stored with the player to be called upon to performAction later.
* If the card Asset boolean is false, the performAction method will be called and performed immediately.

b. performAction() 

* the performAction class will make changes to the current player/other player's properties. It is called
by the player upon drawing or when it is an asset (view clickable). The performAction class will call
helper methods from the player class (e.g. changeMoney, move Location, etc.) 

2. An enumerated class "Community/Chance Card"

This enumerated class will only have method, which performed immediately. It will call all possible methods
that can update player's variables (e.g. money stash, location). If it is an asset card, the performAction
method (the only method) inputs all null values, meaning that there is no immediate update on the player.
Instead, it will return a true/false boolean that the card will be stored by the player's properties and 
will be called later.