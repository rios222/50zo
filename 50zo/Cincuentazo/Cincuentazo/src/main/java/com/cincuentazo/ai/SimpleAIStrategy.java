package com.cincuentazo.ai;

import com.cincuentazo.model.Card;
import com.cincuentazo.model.GameModel;
import com.cincuentazo.model.Player;

import java.util.List;

/**
 * A basic CPU strategy that tries to play a safe card without going over 50.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class SimpleAIStrategy implements AIStrategy {
    @Override
    public Card chooseCard(Player cpu, GameModel model) {
        int table = model.getTableSum();
        List<Card> hand = cpu.getHand();
        // prefer non-eliminating small cards; try A as 10 if fits otherwise 1
        for (Card c : hand) {
            if ("A".equals(c.getRank())) {
                if (table + 10 <= 50) return c;
                if (table + 1 <= 50) return c;
            } else {
                int v = c.getDefaultValue();
                if (table + v <= 50) return c;
            }
        }
        return null;
    }
}
