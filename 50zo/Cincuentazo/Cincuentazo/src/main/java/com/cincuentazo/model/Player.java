package com.cincuentazo.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a player in the game. A player can be human or CPU and has a hand of cards.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class Player {
    private final String name;
    private final boolean human;
    private final List<Card> hand = new ArrayList<>();
    private boolean eliminated = false;

    public Player(String name, boolean human) {
        this.name = name;
        this.human = human;
    }

    public String getName() { return name; }
    public boolean isHuman() { return human; }
    public List<Card> getHand() { return hand; }
    public boolean isEliminated() { return eliminated; }
    public void setEliminated(boolean val) { this.eliminated = val; }

    @Override
    public String toString() { return name; }
}
