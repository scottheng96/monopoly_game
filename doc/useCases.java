/*


public class useCases {
    Player player1 = new Player();
    Player player2 = new Player();
    Die dice = new Die();
    PropertySpace myProperty = new PropertySpace();
    Rules rule = new Rules();

    public void useCase1() {
        player2.increaseMoney(player1.withdrawMoney(100));
        //player2 gets $100 from player1
    }

    public void useCase8() {
        int roll = die.roll();
        //by random number generator, roll == 5
        player1.move(roll);
        //player1 moves 5 spaces forward
    }

    public void useCase10() {
        player1.move(number);
        spot.resolveLanding(player1);
        if (!spot.isOwned()) { // According to the use case the spot is unowned
            player1.changeState(BUYING); // The player will confirm they want to buy the PropertySpace on the front end.
            spot.setOwner(Player1); // within set owner, the property will also be given to the player
            player1.withdrawMoney(spot.getCost());
        }
        player1.changeState(POST_ROLL);
        // The player resumes it's turn having bought the property
    } */
/*

    public void useCase15() {
        Card card = drawWildCard(); // private method in Game
        card.setOwner(player1)
        if (!card.isHoldable()){
            card.doAction(player1, allPlayers) // allPlayers = all player in the game, doAction will make Card not have owner again
        }
    }

    public void useCase19() {
        PropertySpace property = myProperty; //This property will need to be chosen by the user in the GUI. As a placeholder we will use myProperty.
        // At this point, the user will tell the GUI that they want to mortgage their property.
        int money = property.mortgage();
        currentPlayer.increaseMoney(money);
        // The player gets money, and the property is mortgaged.
    }

    public void useCase21() {
        player1.changeState(ROLLING); // The player is rolling.
        int roll = dice.roll();
        player.move(roll);]
        Location spot = player.getCurrentLocation(); // The location that the player lands on.
        spot.resolveLanding(player1); // Whatever actions need to take place after landing on a location.
        if (dice.gotDoubles()) { // Checks if the roll was doubles.
            player.changeState(PRE_ROLL) // It is doubles so this branch of the conditional will be chosen.
        } else { player.changeState(POST_ROLL); }
        // Because the state is PRE_ROLL we are waiting for human to select trade or sell or ROLL from the menu of options
        player.changeState(ROLLING) // The player rolls.
        roll = dice.roll():
    }


    public void useCase12(){
        if (rule.canPlayerBuildOn(Player player1, PropertySpace myProperty){ // checks if player can build based on specific rule of that game
            cost = myProperty.getNextHouseCost();
            myProperty.addHouse();
            player1.withdrawMoney(cost);
        }
    }



}
*/
