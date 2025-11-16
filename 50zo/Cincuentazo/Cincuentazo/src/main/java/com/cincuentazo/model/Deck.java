package com.cincuentazo.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents the deck of cards used in the game. It can give cards and receive them back.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class Deck {
    private final List<Card> cards = new ArrayList<>();

    public Deck() {
        reset();
    }

    public final void reset() {
        cards.clear();
        String[] suits = {"H","D","C","S"};
        String[] ranks = {"A","2","3","4","5","6","7","8","9","10","J","Q","K"};
        for (String r : ranks) for (String s : suits) cards.add(new Card(r,s));
        shuffle();
    }

    public void shuffle() { Collections.shuffle(cards); }

    public boolean isEmpty() { return cards.isEmpty(); }

    public Card draw() {
        if (cards.isEmpty()) return null;
        return cards.remove(cards.size()-1);
    }

    public void addToBottom(Card c) { cards.add(0,c); }

    public int size() { return cards.size(); }

    public void addAll(List<Card> c) { cards.addAll(c); }
}
