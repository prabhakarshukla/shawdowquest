package com.shadowquest.input;

import com.shadowquest.player.Player;
import javafx.scene.input.KeyCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class InputHandlerTest {
    @ParameterizedTest
    @CsvSource({"W,0,-1", "S,0,1", "A,-1,0", "D,1,0",
            "UP,0,-1", "DOWN,0,1", "LEFT,-1,0", "RIGHT,1,0"})
    void mapsBothControlSchemes(KeyCode key, int horizontal, int vertical) {
        InputHandler input = new InputHandler();
        input.press(key);
        assertEquals(horizontal, input.getHorizontal());
        assertEquals(vertical, input.getVertical());
        input.release(key);
        assertEquals(0, input.getHorizontal());
        assertEquals(0, input.getVertical());
    }

    @ParameterizedTest
    @CsvSource({"W,A,-1,-1", "W,D,1,-1", "S,A,-1,1", "S,D,1,1",
            "UP,LEFT,-1,-1", "UP,RIGHT,1,-1", "DOWN,LEFT,-1,1", "DOWN,RIGHT,1,1"})
    void simultaneousKeysProduceNormalizedPlayerMovement(KeyCode first, KeyCode second,
                                                       int horizontal, int vertical) {
        InputHandler input = new InputHandler();
        input.press(first);
        input.press(second);
        assertEquals(horizontal, input.getHorizontal());
        assertEquals(vertical, input.getVertical());
        Player player = new Player("Arin");
        player.move(input.getHorizontal(), input.getVertical(), 1);
        assertEquals(200, Math.hypot(player.getX(), player.getY()), 1e-9);
    }

    @Test
    void aliasesAndRepeatDoNotAddSpeedOrReleaseOtherKeys() {
        InputHandler input = new InputHandler();
        input.press(KeyCode.D);
        input.press(KeyCode.D);
        input.press(KeyCode.RIGHT);
        assertEquals(1, input.getHorizontal());
        input.release(KeyCode.D);
        assertEquals(1, input.getHorizontal());
        input.release(KeyCode.RIGHT);
        input.release(KeyCode.RIGHT);
        assertEquals(0, input.getHorizontal());
    }

    @Test
    void oppositeKeysCancelAndRemainingKeyResumes() {
        InputHandler input = new InputHandler();
        input.press(KeyCode.W);
        input.press(KeyCode.DOWN);
        input.press(KeyCode.A);
        input.press(KeyCode.RIGHT);
        assertEquals(0, input.getHorizontal());
        assertEquals(0, input.getVertical());
        input.release(KeyCode.DOWN);
        input.release(KeyCode.A);
        assertEquals(1, input.getHorizontal());
        assertEquals(-1, input.getVertical());
    }

    @Test
    void releaseStopsOnlyThatAxis() {
        InputHandler input = new InputHandler();
        input.press(KeyCode.W);
        input.press(KeyCode.D);
        input.release(KeyCode.W);
        assertEquals(0, input.getVertical());
        assertEquals(1, input.getHorizontal());
    }

    @Test
    void clearAndDetachReleaseAllKeys() {
        InputHandler input = new InputHandler();
        input.press(KeyCode.W);
        input.press(KeyCode.SPACE);
        input.clear();
        assertEquals(0, input.getVertical());
        assertFalse(input.isPressed(KeyCode.SPACE));
        input.press(KeyCode.D);
        input.detach();
        input.detach();
        assertEquals(0, input.getHorizontal());
    }

    @ParameterizedTest
    @EnumSource(value = KeyCode.class, names = {"SPACE", "E", "ESCAPE"})
    void futureActionsAreTrackedWithoutMovement(KeyCode key) {
        InputHandler input = new InputHandler();
        input.press(key);
        assertTrue(input.isPressed(key));
        assertEquals(0, input.getHorizontal());
        assertEquals(0, input.getVertical());
        input.release(key);
        assertFalse(input.isPressed(key));
    }

    @Test
    void unsupportedKeysAndInitialStateDoNotMove() {
        InputHandler input = new InputHandler();
        input.press(KeyCode.Q);
        assertFalse(input.isPressed(KeyCode.Q));
        Player player = new Player("Arin");
        player.move(input.getHorizontal(), input.getVertical(), 1);
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
    }
}
