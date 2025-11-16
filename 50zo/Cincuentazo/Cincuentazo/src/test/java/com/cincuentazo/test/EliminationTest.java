package com.cincuentazo.test;

import com.cincuentazo.model.GameModel;
import com.cincuentazo.model.Player;
import com.cincuentazo.model.Card;
import com.cincuentazo.exceptions.DeckEmptyException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class EliminationTest {

    @Test
    public void testEliminateMovesCardsToDeck() throws DeckEmptyException {
        GameModel gm = new GameModel();
        Player p = new Player("Test", true);
        gm.addPlayer(p);
        int before = gm.getDeck().size();
        p.getHand().add(new Card("2", "H"));
        p.getHand().add(new Card("3", "D"));

        gm.eliminate(p);

        Assertions.assertTrue(p.isEliminated());
        Assertions.assertEquals(before + 2, gm.getDeck().size());
    }
}
