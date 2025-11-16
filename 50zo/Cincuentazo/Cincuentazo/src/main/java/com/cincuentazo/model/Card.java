package com.cincuentazo.model;

/**
 * Represents a single playing card with a rank and a suit.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class Card {
    private final String rank;
    private final String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() { return rank; }
    public String getSuit() { return suit; }

    /**
     * Default numeric value (A handled specially by model/game rules).
     */
    public int getDefaultValue() {
        switch (rank) {
            case "J": case "Q": case "K": return -10;
            case "9": return 0;
            case "A": return 1; // fallback, game may treat A as 1 or 10
            default:
                try { return Integer.parseInt(rank); } catch (NumberFormatException e) { return 0; }
        }
    }

    @Override
    public String toString() { return rank + suit; }
}
