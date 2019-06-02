## CHANGES

* We found it necessary to split the Game class, which once held 
all methods for the backend running of a game, into Game and Turn. This
occurred while working on CPUs and realizing we needed a way to split
part of the logic of Game into something that could be abstracted and 
extended in order to make a "CPU" able to run its own turn. 

* We decided to deprecate GameSetUp because we performed most of the set up in our
factories and data reading process. This made GameSetUp a repetitive intermediary 

* We made Jail its own space because the actions that occur on a Jail space are unique to
it and integral to the game Monopoly

* We switched buying properties from a Game function to an internal function when a PropertySpace
is landed on to avoid downcasting

* We deprecated Ownable as an interface because we were not able to get to the point of making 
Cards ownable 

* We added an inheritance hierarchy for Trade and Transaction to avoid mixing their functionality into 
the already long Game class