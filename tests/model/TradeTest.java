package model;

import model.location.propertySpace.PropertySpace;
import model.location.propertySpace.RealEstate;
import model.player.HumanPlayer;
import model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


class TradeTest {

    private Player player1;
    private Player player2;
    private List<PropertySpace> properties = new ArrayList<>();
    private Map<Integer, Integer> rents = new HashMap<>();
    private Trade myTrade;

    @BeforeEach
    void setUp(){
        rents.put(0, 100);
        rents.put(1, 200);
        for (int i = 0; i < 7; i++){
            properties.add(new RealEstate(Integer.toString(i), i, "", 8, 9, 100, rents));
        }
        player1 = new HumanPlayer("");
        player2 = new HumanPlayer("");
        myTrade = new Trade(player1, properties.subList(0,2), player2, properties.subList(3,5), 500);
    }

    @Test
    void doTransaction_tradeOccurs() {
        myTrade.doTransaction();
        assert(properties.get(0).getOwner().equals(player2));
        assert(properties.get(3).getOwner().equals(player1));
    }

    @Test
    void doTransaction_moneyGetsExchanged() {
        player2.increaseMoney(500);
        myTrade.doTransaction();
        assertEquals(500, player1.getMoneyBalance());
        assertEquals(0, player2.getMoneyBalance());
    }

    @Test
    void getAskedForPropertyName_getsCorrectProperties(){
        assertEquals(properties.subList(3,5), myTrade.getAskedForPropertyNames());
    }

    @Test
    void getOfferedPropertyName_getsCorrectProperties(){
        assertEquals(properties.subList(0,2), myTrade.getOfferedPropertyNames());
    }

    @Test
    void doTransaction_tradeWorksWithPropertiesAndMoney(){
        player1.increaseMoney(600);
        myTrade = new Trade(player1, new ArrayList<>(), player2, properties.subList(3,5), -500);
        myTrade.doTransaction();
        assert(properties.get(3).getOwner().equals(player1));
        assertEquals(100, player1.getMoneyBalance());
    }
}