package com.cincuentazo.test;

import com.cincuentazo.model.Deck;
import com.cincuentazo.model.Card;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DeckTest {

    @Test
    public void testDeckHas52CardsAfterReset() {
        Deck deck = new Deck();
        Assertions.assertEquals(52, deck.size());
    }

    @Test
    public void testDrawRemovesCard() {
        Deck deck = new Deck();
        Card c = deck.draw();
        Assertions.assertNotNull(c);
        Assertions.assertEquals(51, deck.size());
    }
}
