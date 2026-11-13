package com.shadowquest.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameMovementTest {
    @Test
    void menuIgnoresMovementWithoutCreatingAPlayer() {
        Game game = new Game();
        game.movePlayer(1, 1, 1);
        assertEquals(GameState.MENU, game.getState());
        assertNull(game.getPlayer());
    }

    @Test
    void playingMovesAndPauseGatesMovementUntilResumed() {
        Game game = new Game();
        game.startNewGame();
        game.movePlayer(1, 0, 0.25);
        assertEquals(50, game.getPlayer().getX());
        game.pause();
        game.movePlayer(1, 1, 1);
        assertEquals(50, game.getPlayer().getX());
        assertEquals(0, game.getPlayer().getY());
        game.resume();
        game.movePlayer(0, -1, 0.25);
        assertEquals(-50, game.getPlayer().getY());
    }

    @Test
    void defeatAndGameOverDoNotMoveEvenIfHealthIsRestored() {
        Game game = new Game();
        game.startNewGame();
        game.getPlayer().takeDamage(100);
        game.movePlayer(1, 1, 1);
        game.update(0);
        game.getPlayer().heal(100);
        game.movePlayer(1, 1, 1);
        assertEquals(GameState.GAME_OVER, game.getState());
        assertEquals(0, game.getPlayer().getX());
        assertEquals(0, game.getPlayer().getY());
    }
}
