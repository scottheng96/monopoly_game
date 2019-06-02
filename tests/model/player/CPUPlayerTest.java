package model.player;

import model.Transaction;
import model.location.propertySpace.PropertySpace;
import model.location.propertySpace.RealEstate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CPUPlayerTest {


    Player robot;
    PropertySpace place;
    Map<Integer, Integer> rents = new HashMap<>();

    @BeforeEach
    void setUp(){
        rents.put(0, 100);
        rents.put(1, 200);
        robot = new CPUPlayer("");
        place = new RealEstate("", 5, "YELLOW", 1, 100, 100, rents);
    }

    @Test
    void changeState_CPUSellsHousesWhileInDebt() {
        place.changeOwner(robot);
        robot.increaseMoney(100);
        place.addHouse();
        assertEquals(1, place.getNumberOfHouses());
        Transaction transaction = new Transaction(robot, new Bank(""), -100);
        robot.addTransaction(transaction);
        transaction.doTransaction();
        assertEquals(0, place.getNumberOfHouses());
    }

    @Test
    void changeState_CPUSellsOnlyOneHouseWhileInDebt() {
        place.changeOwner(robot);
        robot.increaseMoney(1000);
        place.addHouse();
        place.addHouse();
        place.addHouse();
        Transaction transaction = new Transaction(robot, new Bank(""), -750);
        robot.addTransaction(transaction);
        transaction.doTransaction();
        assertEquals(2, place.getNumberOfHouses());
    }


    @Test
    void changeState_CPUSGoesBankrupt() {
        robot.increaseMoney(5);
        Transaction transaction = new Transaction(robot, new Bank(""), -750);
        robot.addTransaction(transaction);
        transaction.doTransaction();
        assertEquals(PlayerState.BANKRUPT, robot.getCurrentState());
    }


}