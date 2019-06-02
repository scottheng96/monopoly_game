package model.location.propertySpace;


import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class RealEstateTest {

    private PropertySpace property;
    private Player player1;
    private Map<Integer,Integer> rents;


    @BeforeEach
    void setUp(){
        rents = new HashMap<>();
        rents.put(0,100);
        rents.put(1,300);
        rents.put(2,800);
        player1 = new HumanPlayer("");
        property = new RealEstate("place", 1, "R", 2, 100, 10, rents);
        property.changeOwner(player1);
        player1.increaseMoney(700);
    }

    @Test
    void updateRent_rentWorksWithoutHouses() {
        assertEquals(100, property.getRent());
    }

    @Test
    void updateRent_rentUpdatesWithHouses() {
        property.addHouse();
        property.addHouse();
        System.out.println(property.getNumberOfHouses());
        assertEquals(800, property.getRent());
    }

    @Test
    void updateRent_rentGoesBackDownAfterSellingHouses() {
        property.addHouse();
        property.addHouse();
        property.sellHouse();
        assertEquals(300, property.getRent());
    }


    @Test
    public void mortgage_PropertyGetsMortgaged() {
        property.changeOwner(player1);
        property.mortgage();
        assertTrue(property.isMortgaged());
        assertEquals(750, player1.getMoneyBalance());
    }


    @Test
    public void mortgage_PlayerGetsMoney() {
        property.mortgage();
        property.changeOwner(player1);
        assertTrue(property.isMortgaged());
        property.unmortgage();
        assertFalse(property.isMortgaged());
        assertEquals(695, player1.getMoneyBalance());
    }

}