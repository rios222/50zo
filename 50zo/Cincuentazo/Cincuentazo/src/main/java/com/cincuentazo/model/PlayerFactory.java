package com.cincuentazo.model;

/**
 * Creates human and CPU players for the game.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class PlayerFactory {
    public static Player createHuman() { return new Player("You", true); }
    public static Player createCPU(int idx) { return new Player("CPU " + idx, false); }
}
