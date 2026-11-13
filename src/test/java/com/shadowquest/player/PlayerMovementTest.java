package com.shadowquest.player;

import com.shadowquest.utils.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PlayerMovementTest {
    @ParameterizedTest
    @CsvSource({"0,-1", "0,1", "-1,0", "1,0", "-1,-1", "1,-1", "-1,1", "1,1"})
    void allEightDirectionsTravelAtTheSameSpeed(int horizontal, int vertical) {
        Player player = new Player("Arin");
        player.move(horizontal, vertical, 0.25);
        double length = Math.hypot(horizontal, vertical);
        assertEquals(horizontal / length * 50, player.getX(), 1e-9);
        assertEquals(vertical / length * 50, player.getY(), 1e-9);
        assertEquals(50, Math.hypot(player.getX(), player.getY()), 1e-9);
    }

    @Test
    void noInputAndZeroTimePreservePosition() {
        Player player = new Player("Arin");
        player.move(1, 0, 1);
        player.move(0, 0, 2);
        player.move(1, -1, 0);
        assertEquals(200, player.getX());
        assertEquals(0, player.getY());
    }

    @Test
    void diagonalMovementIsIndependentOfFramePartition() {
        Player single = new Player("Arin");
        Player multiple = new Player("Arin");
        single.move(1, -1, 1);
        for (int i = 0; i < 100; i++) {
            multiple.move(1, -1, 0.01);
        }
        assertEquals(single.getX(), multiple.getX(), 1e-9);
        assertEquals(single.getY(), multiple.getY(), 1e-9);
    }

    @ParameterizedTest
    @CsvSource({"UP,0,-1", "DOWN,0,1", "LEFT,-1,0", "RIGHT,1,0"})
    void originalDirectionApiMatchesAxisApi(Direction direction, int horizontal, int vertical) {
        Player original = new Player("Arin");
        Player axes = new Player("Arin");
        original.move(direction, 0.5);
        axes.move(horizontal, vertical, 0.5);
        assertEquals(original.getX(), axes.getX());
        assertEquals(original.getY(), axes.getY());
    }

    @ParameterizedTest
    @CsvSource({"2,0", "-2,0", "0,2", "0,-2"})
    void invalidAxesDoNotChangePosition(int horizontal, int vertical) {
        Player player = new Player("Arin");
        assertThrows(IllegalArgumentException.class, () -> player.move(horizontal, vertical, 1));
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, Double.NaN, Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY, Double.MAX_VALUE})
    void invalidTimeIsRejectedEvenWithNoInput(double time) {
        Player player = new Player("Arin");
        assertThrows(IllegalArgumentException.class, () -> player.move(1, 1, time));
        assertThrows(IllegalArgumentException.class, () -> player.move(0, 0, time));
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
    }

    @Test
    void deadPlayerDoesNotMoveDiagonally() {
        Player player = new Player("Arin");
        player.takeDamage(100);
        player.move(1, 1, 1);
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
    }
}
