package model.player;

import model.location.*;

import model.location.propertySpace.PropertySpace;
import model.location.propertySpace.RealEstate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HumanPlayerTest {

    Player player;
    Location start;
    Location jail;
    PropertySpace balticAvenue;
    private Map<Integer, Integer> rents = new HashMap<>();


    @BeforeEach
    void setUp(){
        rents.put(0,100);
        player = new HumanPlayer("Leah");
        start = new MoneySpace("GO", 0, 300);
        jail = new MoneySpace("JAIL", 1, 300);
        balticAvenue = new RealEstate("BALTIC AVENUE", 4, "BROWN", 0, 70, 4, rents);
        start.setNextLocation(jail);
    }

    @Test
    void changeState_PlayersStateChanges() {
        player.changeState(PlayerState.ROLLING);
        assertEquals(PlayerState.ROLLING, player.getCurrentState());
    }

    @Test
    void move_PlayerMovesSomeNumberOfSpaces(){
        player.moveTo(start);
        player.move(1);
        assertEquals(jail, player.getCurrentLocation());
    }

    @Test
    void moveTo_PlayerMovesToNewSpace(){
        player.moveTo(jail);
        assertEquals(jail, player.getCurrentLocation());
        player.moveTo(start);
        assertEquals(start, player.getCurrentLocation());
    }

    @Test
    void increaseMoney_PlayerGetsMoreMoney(){
        player.increaseMoney(100);
        assertEquals(100, player.getMoneyBalance());
    }

    @Test
    void withdrawMoney_PlayerLosesMoney(){
        player.increaseMoney(100);
        int moneyWithdrawn = player.withdrawMoney(100);
        assertEquals(0, player.getMoneyBalance());
        assertEquals(100, moneyWithdrawn);
    }

    @Test
    void addProperty_PlayerGetsProperty(){
        player.addProperty(balticAvenue);
        assert(player.getProperties().contains(balticAvenue));
     //   assert(player.hasProperty(balticAvenue));
    }

    @Test
    void addProperty_PlayerGetsPropertyOnlyOnce(){
        player.addProperty(balticAvenue);
        player.addProperty(balticAvenue);
        assert(player.hasProperty(balticAvenue));

        player.removeProperty(balticAvenue);
        for (PropertySpace p : player.getProperties()){
            System.out.println(p.getName());
        }
        assertFalse(player.hasProperty(balticAvenue));
    }

    @Test
    void removeProperty_PlayerNoLongerHasProperty(){
        player.addProperty(balticAvenue);
        player.removeProperty(balticAvenue);
        assertFalse(player.hasProperty(balticAvenue));
    }

    @Test
    void hasProperty_PlayerBuysProperty(){
        balticAvenue.changeOwner(player);
        assert (player.hasProperty(balticAvenue));
    }

    @Test
    void hasProperty_PlayerDoesNotBuyProperty(){
        assertFalse(player.hasProperty(balticAvenue));
    }

//    @Test
//    void increaseNumberOfDoubles_PlayerGetsSeveralDoubles(){
//        assertEquals(0, player.getNumberOfDoubles());
//        player.increaseNumberOfDoubles();
//        player.increaseNumberOfDoubles();
//        assertEquals(2, player.getNumberOfDoubles());
//    }
}