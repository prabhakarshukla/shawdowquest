package com.shadowquest.input;

import java.util.EnumSet;
import java.util.Objects;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/** Tracks held keys; it never changes a player or executes combat actions. */
public final class InputHandler {
    private final EnumSet<KeyCode> pressedKeys = EnumSet.noneOf(KeyCode.class);
    private final EventHandler<KeyEvent> pressedHandler = event -> {
        if (isSupported(event.getCode())) {
            press(event.getCode());
            event.consume();
        }
    };
    private final EventHandler<KeyEvent> releasedHandler = event -> {
        if (isSupported(event.getCode())) {
            release(event.getCode());
            event.consume();
        }
    };
    private Scene scene;

    public void attach(Scene scene) {
        Objects.requireNonNull(scene, "scene");
        detach();
        this.scene = scene;
        scene.addEventFilter(KeyEvent.KEY_PRESSED, pressedHandler);
        scene.addEventFilter(KeyEvent.KEY_RELEASED, releasedHandler);
    }

    public void detach() {
        if (scene != null) {
            scene.removeEventFilter(KeyEvent.KEY_PRESSED, pressedHandler);
            scene.removeEventFilter(KeyEvent.KEY_RELEASED, releasedHandler);
            scene = null;
        }
        clear();
    }

    public void press(KeyCode key) {
        if (isSupported(Objects.requireNonNull(key, "key"))) {
            pressedKeys.add(key);
        }
    }

    public void release(KeyCode key) {
        pressedKeys.remove(Objects.requireNonNull(key, "key"));
    }

    public boolean isPressed(KeyCode key) {
        return pressedKeys.contains(Objects.requireNonNull(key, "key"));
    }

    public int getHorizontal() {
        return (isPressed(KeyCode.D) || isPressed(KeyCode.RIGHT) ? 1 : 0)
                - (isPressed(KeyCode.A) || isPressed(KeyCode.LEFT) ? 1 : 0);
    }

    public int getVertical() {
        return (isPressed(KeyCode.S) || isPressed(KeyCode.DOWN) ? 1 : 0)
                - (isPressed(KeyCode.W) || isPressed(KeyCode.UP) ? 1 : 0);
    }

    public void clear() {
        pressedKeys.clear();
    }

    private static boolean isSupported(KeyCode key) {
        return switch (key) {
            case W, A, S, D, UP, DOWN, LEFT, RIGHT, SPACE, E, ESCAPE -> true;
            default -> false;
        };
    }
}
