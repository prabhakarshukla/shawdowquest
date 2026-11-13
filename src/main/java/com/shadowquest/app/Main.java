package com.shadowquest.app;

import com.shadowquest.core.GameConfig;
import com.shadowquest.ui.ScreenManager;
import javafx.application.Application;
import javafx.stage.Stage;

/** Starts the JavaFX application and delegates presentation to the UI package. */
public final class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle(GameConfig.TITLE);
        new ScreenManager(stage).showMainMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
