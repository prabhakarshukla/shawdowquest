package com.shadowquest.ui;

import com.shadowquest.core.GameConfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/** Minimal title screen. Menu actions will be added in a later phase. */
public final class MainMenuScreen extends StackPane {

    public MainMenuScreen() {
        Label title = new Label(GameConfig.TITLE);
        title.getStyleClass().add("game-title");
        title.setWrapText(true);
        title.setAlignment(Pos.CENTER);
        title.setMaxWidth(Double.MAX_VALUE);

        setPadding(new Insets(32));
        getStyleClass().add("launch-screen");
        getChildren().add(title);
        getStylesheets().add(MainMenuScreen.class
                .getResource("/styles/launch.css").toExternalForm());
    }
}
