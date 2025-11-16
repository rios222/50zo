package com.cincuentazo.ui;

import com.cincuentazo.model.Card;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Objects;

/**
 * Builds an image view for a card so it can be shown in the JavaFX scene.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class CardAdapter {

    private final Card card;

    public CardAdapter(Card c) {
        this.card = c;
    }

    public ImageView getImageView() {
        String fileName = card.getRank() + card.getSuit() + ".png";
        String path = "/assets/cards/" + fileName;

        Image img = new Image(Objects.requireNonNull(
                getClass().getResource(path)
        ).toExternalForm());
        ImageView view = new ImageView(img);

        view.setFitWidth(80);
        view.setFitHeight(120);
        view.setPreserveRatio(true);

        return view;
    }
}
