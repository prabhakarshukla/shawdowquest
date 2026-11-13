package com.shadowquest.player;

import com.shadowquest.utils.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    @Test
    void initializesPlayerWithDocumentedDefaults() {
        Player player = new Player("Arin");
        assertEquals("Arin", player.getName());
        assertEquals(100, player.getHealth());
        assertEquals(100, player.getMaxHealth());
        assertEquals(10, player.getAttackPower());
        assertEquals(5, player.getDefense());
        assertEquals(200, player.getSpeed());
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
        assertTrue(player.isAlive());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n"})
    void rejectsInvalidPlayerNames(String name) {
        assertThrows(IllegalArgumentException.class, () -> new Player(name));
    }

    @ParameterizedTest
    @CsvSource({"UP, 0, -50", "DOWN, 0, 50", "LEFT, -50, 0", "RIGHT, 50, 0"})
    void movesInEachWorldDirection(Direction direction, double expectedX, double expectedY) {
        Player player = new Player("Arin");
        player.move(direction, 0.25);
        assertEquals(expectedX, player.getX(), 0.000001);
        assertEquals(expectedY, player.getY(), 0.000001);
    }

    @Test
    void movementDependsOnElapsedTimeRatherThanNumberOfUpdates() {
        Player oneStep = new Player("Arin");
        Player manySteps = new Player("Arin");
        oneStep.move(Direction.RIGHT, 1);
        for (int i = 0; i < 10; i++) {
            manySteps.move(Direction.RIGHT, 0.1);
        }
        assertEquals(oneStep.getX(), manySteps.getX(), 0.000001);
    }

    @Test
    void oppositeDirectionsReturnToStartingPosition() {
        Player player = new Player("Arin");
        player.move(Direction.DOWN, 1);
        player.move(Direction.RIGHT, 1);
        player.move(Direction.UP, 1);
        player.move(Direction.LEFT, 1);
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
    }

    @Test
    void zeroTimeDoesNotMovePlayer() {
        Player player = new Player("Arin");
        player.move(Direction.RIGHT, 0.5);
        player.move(Direction.DOWN, 0);
        assertEquals(100, player.getX());
        assertEquals(0, player.getY());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, Double.NaN, Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY, Double.MAX_VALUE})
    void rejectsInvalidOrOverflowingMovementTimeWithoutChangingPosition(double time) {
        Player player = new Player("Arin");
        player.move(Direction.DOWN, 0.5);
        assertThrows(IllegalArgumentException.class, () -> player.move(Direction.RIGHT, time));
        assertEquals(0, player.getX());
        assertEquals(100, player.getY());
    }

    @Test
    void rejectsNullDirectionWithoutChangingPosition() {
        Player player = new Player("Arin");
        assertThrows(IllegalArgumentException.class, () -> player.move(null, 1));
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
    }

    @Test
    void repeatedLargeMovementCannotOverflowPosition() {
        Player player = new Player("Arin");
        double largeTime = Double.MAX_VALUE / player.getSpeed() * 0.75;
        player.move(Direction.RIGHT, largeTime);
        double previousX = player.getX();
        assertThrows(IllegalArgumentException.class, () -> player.move(Direction.RIGHT, largeTime));
        assertEquals(previousX, player.getX());
        assertEquals(0, player.getY());
    }

    @Test
    void deadPlayerDoesNotMoveAndStillValidatesInput() {
        Player player = new Player("Arin");
        player.takeDamage(100);
        player.move(Direction.RIGHT, 1);
        assertFalse(player.isAlive());
        assertEquals(0, player.getX());
        assertEquals(0, player.getY());
        assertThrows(IllegalArgumentException.class, () -> player.move(null, 1));
        assertThrows(IllegalArgumentException.class, () -> player.move(Direction.RIGHT, -1));
    }
}
