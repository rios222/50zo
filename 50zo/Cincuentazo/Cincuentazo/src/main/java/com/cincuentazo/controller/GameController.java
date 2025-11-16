package com.cincuentazo.controller;

import com.cincuentazo.ai.AIStrategy;
import com.cincuentazo.ai.SimpleAIStrategy;
import com.cincuentazo.model.Card;
import com.cincuentazo.model.GameModel;
import com.cincuentazo.model.Player;
import com.cincuentazo.model.PlayerFactory;
import com.cincuentazo.exceptions.DeckEmptyException;
import com.cincuentazo.exceptions.InvalidMoveException;
import com.cincuentazo.ui.CardAdapter;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controls the main game screen. It shows cards, handles turns and updates the text on the screen.
 *
 * This class uses simple ideas so it is easy to read and understand.
 */
public class GameController {

    @FXML private Label tableSumLabel;
    @FXML private Label statusLabel;
    @FXML private Button backButton;
    @FXML private HBox tableBox;
    @FXML private HBox playerHandBox;
    @FXML private BorderPane rootPane;

    private GameModel model;
    private ScheduledExecutorService aiExecutor;
    private final List<AIStrategy> aiStrategies = new ArrayList<>();
    private int humanIndex = 0;


@FXML
public void initialize() {
    Platform.runLater(() -> {
        if (rootPane != null) {
            rootPane.requestFocus();
        }
    });
}

    public void initGame(int aiCount) {
        model = new GameModel();
        model.addPlayer(PlayerFactory.createHuman());
        for (int i=1;i<=aiCount;i++) {
            model.addPlayer(PlayerFactory.createCPU(i));
            aiStrategies.add(new SimpleAIStrategy());
        }
        try {
            model.initialDeal();
        } catch (DeckEmptyException e) {
            e.printStackTrace();
        }

        // render initial UI
        renderAllPlayers();
        renderHumanHand();
        renderTable();
        statusLabel.setText("Your turn");
    }

    
private void renderAllPlayers() {
    // ensure there's a top area to show CPUs
    Platform.runLater(() -> {
        HBox cpuBox = new HBox(12);
        cpuBox.setAlignment(Pos.CENTER);
        for (int i = 1; i < model.getPlayers().size(); i++) {
            Player p = model.getPlayers().get(i);
            VBox v = new VBox(4);
            v.setAlignment(Pos.CENTER);

            Label name = new Label(p.getName());
            name.setFont(Font.font(14));

            Label count = new Label("Cards: " + p.getHand().size());
            count.setId("count_" + i);

            HBox handBox = new HBox(3);
            handBox.setId("cpuHand_" + i);
            handBox.setAlignment(Pos.CENTER);
            updateCpuHandGraphics(p, handBox);

            v.getChildren().addAll(name, count, handBox);
            cpuBox.getChildren().add(v);
        }
        if (rootPane != null) {
            VBox center = new VBox(6);
            center.setAlignment(Pos.CENTER);
            center.getChildren().addAll(cpuBox, tableBox);
            rootPane.setCenter(center);
        }
    });
}


private void updateCpuCounts() {
    Platform.runLater(() -> {
        for (int i = 1; i < model.getPlayers().size(); i++) {
            Player p = model.getPlayers().get(i);
            Label lbl = (Label) rootPane.lookup("#count_" + i);
            if (lbl != null) {
                lbl.setText("Cards: " + p.getHand().size());
            }
            HBox handBox = (HBox) rootPane.lookup("#cpuHand_" + i);
            if (handBox != null) {
                updateCpuHandGraphics(p, handBox);
            }
        }
    });
}

private void updateCpuHandGraphics(Player p, HBox handBox) {
    handBox.getChildren().clear();
    int total = p.getHand().size();
    int toShow = Math.min(total, 5);
    for (int j = 0; j < toShow; j++) {
        Region back = new Region();
        back.setPrefSize(20, 30);
        back.setStyle(
                "-fx-background-color: linear-gradient(to bottom, #fdfdfd, #c0c0c0);" +
                "-fx-border-color: #333333;" +
                "-fx-border-radius: 4;" +
                "-fx-background-radius: 4;"
        );
        handBox.getChildren().add(back);
    }
    if (total > toShow) {
        Label more = new Label("+" + (total - toShow));
        more.setTextFill(Color.LIGHTGRAY);
        more.setFont(Font.font(11));
        handBox.getChildren().add(more);
    }
}

private void renderHumanHand() {
        Platform.runLater(() -> {
            playerHandBox.getChildren().clear();
            Player human = model.getPlayers().get(0);
            for (Card c : human.getHand()) {
                StackPane wrapper = new StackPane();
                javafx.scene.image.ImageView iv = new CardAdapter(c).getImageView();
                wrapper.getChildren().add(iv);
                // click handler
                iv.setOnMouseClicked(event -> {
                    onHumanPlayCard(c);
                });
                wrapper.setPrefSize(90,130);
                playerHandBox.getChildren().add(wrapper);
            }
        });
    }

    

    private void onHumanPlayCard(Card c) {
        try {
            int aceAs = ("A".equals(c.getRank()) && model.getTableSum() + 10 <= 50) ? 10 : 1;
            model.playCard(model.getPlayers().get(0), c, aceAs);

            renderHumanHand();
            renderTable();
            updateCpuCounts();

            Player winner = model.checkWinner();
            if (winner != null) {
                statusLabel.setText("Winner: " + winner.getName());
            } else {
                statusLabel.setText("CPU Turn...");
                aiTurnSequence();
            }

        } catch (Exception e) {
            statusLabel.setText("Invalid move: " + e.getMessage());
        }
    }


private void renderTable() {
        Platform.runLater(() -> {
            tableBox.getChildren().clear();
            java.util.List<Card> pile = model.getTablePile();
            if (pile.isEmpty()) {
                tableSumLabel.setText("Table sum: 0");
                return;
            }
            int startIndex = Math.max(0, pile.size() - 5);
            for (int i = startIndex; i < pile.size(); i++) {
                Card c = pile.get(i);
                StackPane wrapper = new StackPane();
                javafx.scene.image.ImageView iv = new CardAdapter(c).getImageView();
                wrapper.getChildren().add(iv);
                tableBox.getChildren().add(wrapper);
            }
            tableSumLabel.setText("Table sum: " + model.getTableSum());
        });
    }


private void aiTurnSequence() {
        // schedule AI turns without blocking UI
        if (aiExecutor!=null && !aiExecutor.isShutdown()) aiExecutor.shutdownNow();
        aiExecutor = Executors.newSingleThreadScheduledExecutor();
        // start from player index 1 to end
        int start = 1;
        List<Player> players = model.getPlayers();
        Random random = new Random();
        for (int i=start;i<players.size();i++) {
            final int idx = i;
            aiExecutor.schedule(() -> {
                if (model.getPlayers().get(idx).isEliminated()) return;
                Player cpu = model.getPlayers().get(idx);
                AIStrategy strat = aiStrategies.size() >= (idx) ? aiStrategies.get(idx-1) : new SimpleAIStrategy();
                Card choice = strat.chooseCard(cpu, model);
                if (choice==null) {
                    // eliminate if no playable
                    if (!model.hasPlayableCard(cpu)) {
                        model.eliminate(cpu);
                        Platform.runLater(() -> statusLabel.setText(cpu.getName() + " eliminated")); 
                    }
                } else {
                    try {
                        int aceAs = ("A".equals(choice.getRank()) && model.getTableSum()+10 <= 50) ? 10 : 1;
                        model.playCard(cpu, choice, aceAs);
                        
final Card played = choice;
Platform.runLater(() -> {
    renderTable();
    updateCpuCounts();
    statusLabel.setText(cpu.getName() + " jugó " + played.toString());
});
} catch (Exception e) {
                        Platform.runLater(() -> statusLabel.setText("Error during AI play")); 
                    }
                }
            }, 2 + random.nextInt(3), TimeUnit.SECONDS); // random 2-4s per CPU
        }
        // schedule a final task to end AI turn and give control back to player
        aiExecutor.schedule(() -> {
            Platform.runLater(() -> {
                renderHumanHand();
                renderTable();
                updateCpuCounts();
                Player winner = model.checkWinner();
                if (winner!=null) {
                    statusLabel.setText("Winner: " + winner.getName());
                } else {
                    statusLabel.setText("Your turn");
                }
            });
        }, (players.size()-start+2)*2, TimeUnit.SECONDS);
    }

    

@FXML
public void onKeyPressed(KeyEvent event) {
    if (model == null || model.getPlayers().isEmpty()) return;
    Player human = model.getPlayers().get(0);
    if (human.isEliminated()) return;

    int index = -1;
    KeyCode code = event.getCode();
    if (code == KeyCode.DIGIT1 || code == KeyCode.NUMPAD1) index = 0;
    else if (code == KeyCode.DIGIT2 || code == KeyCode.NUMPAD2) index = 1;
    else if (code == KeyCode.DIGIT3 || code == KeyCode.NUMPAD3) index = 2;
    else if (code == KeyCode.DIGIT4 || code == KeyCode.NUMPAD4) index = 3;

    if (index >= 0 && index < human.getHand().size()) {
        Card c = human.getHand().get(index);
        onHumanPlayCard(c);
    }
}

@FXML
    public void onBack() {
        // simple back to menu: reload menu
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/menu.fxml"));
            Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
