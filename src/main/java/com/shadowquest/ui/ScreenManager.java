package com.shadowquest.ui;

import com.shadowquest.core.GameConfig;
import javafx.scene.Scene;
import javafx.stage.Stage;

/** Owns screen presentation on the application's primary stage. */
public final class ScreenManager {
    private final Stage stage;

    public ScreenManager(Stage stage) {
        this.stage = stage;
    }

    /** Displays the Phase 0 title screen on the JavaFX Application Thread. */
    public void showMainMenu() {
        Scene scene = new Scene(new MainMenuScreen(),
                GameConfig.VIEWPORT_WIDTH, GameConfig.VIEWPORT_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
