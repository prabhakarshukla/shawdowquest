package com.shadowquest.ui;

import com.shadowquest.core.GameConfig;
import com.shadowquest.player.Player;
import java.util.Objects;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/** Movement demonstration: renders a placeholder without changing domain state. */
public final class GameScreen extends StackPane {
    private static final double PLAYER_RADIUS = 12;
    private static final Color BACKGROUND_COLOR = Color.web("#171b2e");
    private static final Color PLAYER_COLOR = Color.web("#f3e6bf");
    private final Player player;
    private final Canvas canvas = new Canvas(
            GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);

    public GameScreen(Player player, Runnable returnToTitle) {
        this.player = Objects.requireNonNull(player, "player");
        Objects.requireNonNull(returnToTitle, "returnToTitle");
        setPrefSize(GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);
        setFocusTraversable(true);
        canvas.setManaged(false);
        Button back = new Button("Back to title");
        back.setFocusTraversable(false);
        back.setOnAction(event -> returnToTitle.run());
        Label controls = new Label("Move: WASD or arrow keys");
        controls.setTextFill(PLAYER_COLOR);
        HBox toolbar = new HBox(16, back, controls);
        toolbar.setAlignment(Pos.CENTER_LEFT);
        toolbar.setMaxSize(USE_PREF_SIZE, USE_PREF_SIZE);
        StackPane.setAlignment(toolbar, Pos.TOP_LEFT);
        StackPane.setMargin(toolbar, new Insets(12));
        getChildren().addAll(canvas, toolbar);
        render();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (canvas.getWidth() != getWidth() || canvas.getHeight() != getHeight()) {
            canvas.setWidth(getWidth());
            canvas.setHeight(getHeight());
            render();
        }
    }

    public void render() {
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        // A fixed screen-center offset makes the unchanged world origin visible.
        double screenX = canvas.getWidth() / 2 + player.getX();
        double screenY = canvas.getHeight() / 2 + player.getY();
        graphics.setFill(PLAYER_COLOR);
        graphics.fillOval(screenX - PLAYER_RADIUS, screenY - PLAYER_RADIUS,
                PLAYER_RADIUS * 2, PLAYER_RADIUS * 2);
    }
}
