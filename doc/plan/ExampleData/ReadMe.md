# Location Directory
## Location Properties
Each location will have a corresponding propertySpace file. This propertySpace file will be named as “StateName.properties”, for example, “North Carolina.properties”. This propertySpace file will contain all information about this state. Additionally, there will be locations like “Go”/”Surprise” and those locations’ propertySpace files will be named according to the same principle. For example, “Go.properties”.
There will be a bunch of things contained in the “Location.properties file”, including name, location number, picture, cost (“None” if no cost”), groupName(“None” if no group), rent(by number of houses), cost of building (or “None”).
Details please see the example called “North Carolina.properties”. In that file,
COST means the money that a player needs to spend to buy this location.
RENT1 states the fee the player needs to pay for the owner of the location if there is no house in the current location. RENT2 states the fee that the player needs to pay if there is one house in the current location. RENT3 states the fee that the player needs to pay if there are two houses in the current location. RENT4 states the fee that player needs to pay if there are three houses in the current location.
BUILDING1 states the money that the player needs to pay for building a house. BUILDING2 states the money that the player needs to pay for building two houses. BUILDING3 states the money that the player needs to pay for building three houses.

## Location Image
The image for each location is a PNG file. Each PNG file will be named according to the nature of the location. For example, if the location is a state (say North Carolina), then it will be named as “North Carolina”. The first letter of each word will be uppercase. If there are multiple words in a state’s name, then there will be a space between two adjacent words. There are also other locations such as “Start”/”Go” and “Surprise”. These locations follow the same naming conventions for their PNG file. For each image file, there will be a “.png” at the end of the file’s name. For example, the image file of North Carolina will be named as “North Carolina.png”

# Player Directory
## Player Properties
At the beginning of the game, the program will create some properties file according the number of the players we have. If there are three players, then the game will create three propertySpace files called Player1.properties, Player2.properties, and Player3.properties.
Each Player.properties file will contain the following information: statuses of the players, places of players, money of players, properties of players, cards held by players.
In the given example Player1.properties, NAME means the name of the player, TURN means whether it’s this player’s turn or not. JAIL means whether the player is in jail or not. LOCATION means the current location that the player is in. MONEY means the money that the player has, and CARD means the cards that the player owned.

#  Game Directory
## Game Property
The Game.properties will be a propertySpace file that contains the set-up of the game, including but not limited to total number of houses and hotels, total money in the game, the board, starting player money, starting player properties. The total number of houses and hotels limited the total number buildings that can be built in one game. The total money in the game is the upper bound of the existing money in the game. The board will decide which board we are using (we may use the board containing different states, or the board containing different cities). The starting player money and the starting player properties determine initial state of each player.

# WildCard Directory
## WildCard Property
The Card.properties will be a propertySpace file that contains different information of cards representing different possible surprises that could appear in the game, such as California earthquake, wild fire, World War III, etc.
SURPRISE1 indicates the first possible unexpected situation that may influence the game, SURPRISE2 indicates the second possible unexpected situation that may influence the game, etc.

# ButtonRule Directory
## ButtonRule Property
Each line in the Rule.properties file will look like the following:
NOTYOURTURN = 0000000
Each 0 represents whether a button should appear in the UI, otherwise it should not. For example, the first 0 may tell you that the “model.roll Dice” button won’t appear if your status is “NOTYOURTURN”
Which number represents which button is not determined yet and we don’t know how many buttons we should have.

# Map Directory
## Map Property
The Map.properties will be a propertySpace file that maps features in the frontend (such as pictures of players) to the corresponding features in the backend. Basically it serves as a frontend token.
In our example, PLAYER1 in the backend maps to the image of Player1.png in the frontend, and PLAYER2 in the backend maps to the image of Player2.png in the frontend.