package com.shadowquest.ui;

import com.shadowquest.core.GameConfig;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/** Minimal title screen. Menu actions will be added in a later phase. */
public final class MainMenuScreen extends StackPane {
    private static final Color BACKGROUND_COLOR = Color.web("#171b2e");
    private static final Color TITLE_COLOR = Color.web("#f3e6bf");
    private static final Font TITLE_FONT = Font.font("System", FontWeight.BOLD, 36);

    private final Canvas canvas = new Canvas(
            GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);

    public MainMenuScreen() {
        setPrefSize(GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);
        // This pane sizes the Canvas itself, so it must not influence layout size.
        canvas.setManaged(false);
        getChildren().add(canvas);
        drawTitle();
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        if (canvas.getWidth() != getWidth() || canvas.getHeight() != getHeight()) {
            canvas.setWidth(getWidth());
            canvas.setHeight(getHeight());
            drawTitle();
        }
    }

    private void drawTitle() {
        double width = canvas.getWidth();
        double height = canvas.getHeight();
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.setFill(BACKGROUND_COLOR);
        graphics.fillRect(0, 0, width, height);
        graphics.setFill(TITLE_COLOR);
        graphics.setFont(TITLE_FONT);
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.fillText(GameConfig.TITLE, width / 2, height / 2, width);
    }
}
