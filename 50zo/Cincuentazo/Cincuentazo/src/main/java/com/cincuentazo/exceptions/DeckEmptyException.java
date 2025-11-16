package com.cincuentazo.exceptions;

/**
 * Checked exception used when the deck has no more cards to give.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class DeckEmptyException extends Exception {
    public DeckEmptyException(String message) {
        super(message);
    }
}
