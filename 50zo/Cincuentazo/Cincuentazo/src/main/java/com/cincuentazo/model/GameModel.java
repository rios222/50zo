package com.cincuentazo.model;

import com.cincuentazo.exceptions.DeckEmptyException;
import com.cincuentazo.exceptions.InvalidMoveException;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Keeps the main game state. It stores the deck, the players and the cards on the table.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class GameModel {
    private final Deck deck;
    private final List<Player> players = new ArrayList<>();
    private final List<Card> tablePile = new ArrayList<>();

    public GameModel() {
        this.deck = new Deck();
    }


public GameModel(int aiCount) throws DeckEmptyException {
    this();
    players.add(PlayerFactory.createHuman());
    for (int i = 1; i <= aiCount; i++) {
        players.add(PlayerFactory.createCPU(i));
    }
    initialDeal();
}

    public void addPlayer(Player p) { players.add(p); }
    public List<Player> getPlayers() { return players; }
    public Deck getDeck() { return deck; }
    public List<Card> getTablePile() { return tablePile; }

    public void initialDeal() throws DeckEmptyException {
        // give 4 cards each
        for (int i=0;i<4;i++) {
            for (Player p : players) {
                Card c = deck.draw();
                if (c==null) throw new DeckEmptyException("Deck empty during initial deal");
                p.getHand().add(c);
            }
        }
        // put one card on table to start
        Card start = deck.draw();
        if (start==null) throw new DeckEmptyException("Deck empty when putting start card");
        tablePile.add(start);
    }

    public int getTableSum() {
        int sum = 0;
        for (Card c : tablePile) {
            if ("A".equals(c.getRank())) sum += 1; // treat A as 1 by default; controller may override
            else sum += c.getDefaultValue();
        }
        return sum;
    }

    public Card getTopTableCard() {
        if (tablePile.isEmpty()) return null;
        return tablePile.get(tablePile.size()-1);
    }

    public void playCard(Player p, Card c, int aceAs) throws InvalidMoveException, DeckEmptyException {
        // verify playable
        int value = ("A".equals(c.getRank())) ? (aceAs==10?10:1) : c.getDefaultValue();
        if (getTableSum() + value > 50) throw new InvalidMoveException("Card exceeds limit");
        // remove from player's hand
        boolean removed = p.getHand().remove(c);
        if (!removed) throw new InvalidMoveException("Player does not have that card");
        tablePile.add(c);
        // draw a card if possible
        Card drawn = deck.draw();
        if (drawn==null) {
            // reshuffle table except top
            reshuffleFromTable();
            drawn = deck.draw();
            if (drawn==null) throw new DeckEmptyException("No cards to draw after reshuffle");
        }
        p.getHand().add(drawn);
    }

    public boolean hasPlayableCard(Player p) {
        for (Card c : p.getHand()) {
            int v = "A".equals(c.getRank()) ? 1 : c.getDefaultValue();
            if (getTableSum() + v <= 50) return true;
            if ("A".equals(c.getRank()) && getTableSum() + 10 <= 50) return true;
        }
        return false;
    }

    public void eliminate(Player p) {
        // send player's hand to bottom of deck
        for (Card c : p.getHand()) deck.addToBottom(c);
        p.getHand().clear();
        p.setEliminated(true);
    }

    public void reshuffleFromTable() {
        if (tablePile.size() <= 1) return;
        Card last = tablePile.remove(tablePile.size()-1);
        List<Card> rest = new ArrayList<>(tablePile);
        tablePile.clear();
        tablePile.add(last);
        Collections.shuffle(rest);
        deck.addAll(rest);
    }

    /**
     * Returns winner if only one not eliminated remains, else null.
     */
    public Player checkWinner() {
        Player last = null;
        int alive = 0;
        for (Player p : players) {
            if (!p.isEliminated()) { alive++; last = p; }
        }
        return (alive==1) ? last : null;
    }
}
