package model.location;

import model.card.Card;
import model.card.PaymentCard;
import model.player.Bank;
import model.player.HumanPlayer;
import model.player.Player;
import model.player.PlayerState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardSpaceTest {


    Player player = new HumanPlayer("LEAH");
    Location card = new CardSpace("cards", 1, "CHANCE");
    Card payCard = new PaymentCard(1, "CHANCE", new int[]{2});
    Player bank = new Bank("");

    @BeforeEach
    void setUp(){
        bank.addCard(payCard);
        player.changeState(PlayerState.NOT_YOUR_TURN);
    }

    @Test
    void activateSpaceAction_playerGetsCard() {
        card.activateSpaceAction(player, bank, 4);
        assertEquals(1, player.getCards().size());
        assertEquals(payCard, player.getLatestCard());
    }

    @Test
    void activateSpaceAction_playerDoesNotGetCardIfBankIsOut() {
        bank.removeCard(payCard);
        card.activateSpaceAction(player, bank, 4);
        assertEquals(0, player.getCards().size());
        assertNotEquals(PlayerState.DRAWING_CARD, player.getCurrentState());
    }

    @Test
    void activateSpaceAction_playerEntersCorrectState() {
        card.activateSpaceAction(player, bank, 4);
        assertEquals(PlayerState.DRAWING_CARD, player.getCurrentState());
    }

}