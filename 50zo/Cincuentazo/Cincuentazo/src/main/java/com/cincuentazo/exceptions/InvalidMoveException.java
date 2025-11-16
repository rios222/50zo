package com.cincuentazo.exceptions;

/**
 * Runtime exception used when a move is not allowed by the rules.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(String message) {
        super(message);
    }
}
