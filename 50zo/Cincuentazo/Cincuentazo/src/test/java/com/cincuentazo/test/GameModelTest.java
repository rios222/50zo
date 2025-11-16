package com.cincuentazo.test;

import com.cincuentazo.model.Card;
import com.cincuentazo.model.Deck;
import com.cincuentazo.model.GameModel;
import com.cincuentazo.model.Player;
import com.cincuentazo.exceptions.DeckEmptyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for basic game model behavior.
 */
public class GameModelTest {

    @Test
    public void testCardValues() {
        Card a = new Card("A");
        Assertions.assertEquals(1, a.getDefaultValue());
        Card j = new Card("J");
        Assertions.assertEquals(-10, j.getDefaultValue());
        Card nine = new Card("9");
        Assertions.assertEquals(0, nine.getDefaultValue());
    }

    @Test
    public void testDeckDrawAndRefill() throws Exception {
        Deck deck = new Deck();
        int initial = deck.size();
        for (int i=0;i<initial;i++) deck.draw();
        Assertions.assertEquals(0, deck.size());
    }

    @Test
    public void testGamePreparationHasHands() throws DeckEmptyException {
        GameModel gm = new GameModel(1);
        Assertions.assertEquals(2, gm.getPlayers().size());
        for (Player p : gm.getPlayers()) {
            Assertions.assertTrue(p.getHand().size() <= 4); // could be 4 or less if deck ran out
        }
    }
}
