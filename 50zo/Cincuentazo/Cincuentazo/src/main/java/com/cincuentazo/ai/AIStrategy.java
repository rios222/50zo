package com.cincuentazo.ai;

import com.cincuentazo.model.Card;
import com.cincuentazo.model.GameModel;
import com.cincuentazo.model.Player;

/**
 * Defines how a CPU player chooses which card to play.
 *
 * This interface uses simple ideas so it is easy to read and understand.
 */
public interface AIStrategy {
    /**
     * Select a card to play, or return null to pass/eliminate.
     */
    Card chooseCard(Player cpu, GameModel model);
}
