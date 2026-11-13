package com.shadowquest.ui;

import com.shadowquest.core.GameConfig;
import com.shadowquest.core.GameLoop;
import com.shadowquest.core.Game;
import com.shadowquest.input.InputHandler;
import java.util.function.DoubleConsumer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/** Owns screen presentation on the application's primary stage. */
public final class ScreenManager {
    private final Stage stage;
    private GameLoop gameLoop;
    private InputHandler input;

    public ScreenManager(Stage stage) {
        this.stage = stage;
        stage.sceneProperty().addListener((observable, previous, current) -> leaveGame());
        stage.addEventHandler(WindowEvent.WINDOW_HIDDEN, event -> leaveGame());
        stage.focusedProperty().addListener((observable, previous, focused) -> {
            if (!focused && input != null) {
                input.clear();
            }
        });
    }

    /** Displays the Phase 0 title screen on the JavaFX Application Thread. */
    public void showMainMenu() {
        leaveGame();
        Scene scene = new Scene(new MainMenuScreen(),
                GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                event.consume();
                showGame();
            }
        });
        stage.setScene(scene);
        stage.show();
    }

    public void showGame() {
        leaveGame();
        Game game = new Game();
        game.startNewGame();
        GameScreen screen = new GameScreen(game.getPlayer(), this::showMainMenu);
        Scene scene = new Scene(screen, GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);
        stage.setScene(scene);
        input = new InputHandler();
        input.attach(scene);
        stage.show();
        screen.requestFocus();
        startGameLoop(deltaTime -> {
            game.update(deltaTime);
            game.movePlayer(input.getHorizontal(), input.getVertical(), deltaTime);
        }, screen::render);
    }

    private void leaveGame() {
        stopGameLoop();
        if (input != null) {
            input.detach();
            input = null;
        }
    }

    /** Starts callbacks after the gameplay scene has been installed. */
    public void startGameLoop(DoubleConsumer update, Runnable render) {
        stopGameLoop();
        gameLoop = new GameLoop(update, render);
        gameLoop.start();
    }

    public void stopGameLoop() {
        if (gameLoop != null) {
            gameLoop.stop();
            gameLoop = null;
        }
    }
}
