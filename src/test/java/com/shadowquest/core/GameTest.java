package com.shadowquest.core;

import com.shadowquest.player.Player;
import com.shadowquest.utils.Direction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    @Test
    void beginsInMenuWithoutAPlayer() {
        Game game = new Game();
        game.pause();
        game.resume();
        game.update(0);
        assertEquals(GameState.MENU, game.getState());
        assertNull(game.getPlayer());
    }

    @Test
    void startsNewGameWithArin() {
        Game game = new Game();
        game.startNewGame();
        assertEquals(GameState.PLAYING, game.getState());
        assertNotNull(game.getPlayer());
        assertEquals("Arin", game.getPlayer().getName());
        assertTrue(game.getPlayer().isAlive());
    }

    @Test
    void pauseAndResumePreserveTheSessionAndIgnoreRepeatedCommands() {
        Game game = new Game();
        game.startNewGame();
        Player player = game.getPlayer();
        player.move(Direction.RIGHT, 0.5);
        player.takeDamage(20);
        game.pause();
        game.pause();
        assertEquals(GameState.PAUSED, game.getState());
        game.update(1);
        assertEquals(GameState.PAUSED, game.getState());
        game.resume();
        game.resume();
        assertEquals(GameState.PLAYING, game.getState());
        assertSame(player, game.getPlayer());
        assertEquals(80, player.getHealth());
        assertEquals(100, player.getX());
    }

    @Test
    void startCannotReplaceAnActiveOrPausedSession() {
        Game game = new Game();
        game.startNewGame();
        Player player = game.getPlayer();
        player.takeDamage(20);
        game.startNewGame();
        assertEquals(GameState.PLAYING, game.getState());
        assertSame(player, game.getPlayer());
        assertEquals(80, player.getHealth());
        game.pause();
        game.startNewGame();
        assertEquals(GameState.PAUSED, game.getState());
        assertSame(player, game.getPlayer());
        assertEquals(80, player.getHealth());
    }

    @Test
    void updateLeavesLivingPlayerAndStateUnchanged() {
        Game game = new Game();
        game.startNewGame();
        game.getPlayer().takeDamage(1);
        game.update(0.5);
        assertEquals(GameState.PLAYING, game.getState());
        assertEquals(99, game.getPlayer().getHealth());
        assertEquals(0, game.getPlayer().getX());
    }

    @Test
    void deathBecomesGameOverOnUpdate() {
        Game game = new Game();
        game.startNewGame();
        game.getPlayer().takeDamage(100);
        assertEquals(GameState.PLAYING, game.getState());
        game.update(0);
        assertEquals(GameState.GAME_OVER, game.getState());
        game.pause();
        game.resume();
        game.update(1);
        assertEquals(GameState.GAME_OVER, game.getState());
    }

    @Test
    void pausedUpdateDefersDeathDetectionUntilPlayingAgain() {
        Game game = new Game();
        game.startNewGame();
        game.pause();
        // Simulate an external domain caller; paused updates must not run game rules.
        game.getPlayer().takeDamage(100);
        game.update(1);
        assertEquals(GameState.PAUSED, game.getState());
        game.resume();
        game.update(0);
        assertEquals(GameState.GAME_OVER, game.getState());
    }

    @Test
    void healingCannotResumeAGameOverSession() {
        Game game = new Game();
        game.startNewGame();
        game.getPlayer().takeDamage(100);
        game.update(0);
        game.getPlayer().heal(100);
        game.resume();
        game.update(1);
        assertEquals(GameState.GAME_OVER, game.getState());
    }

    @Test
    void restartingAfterDefeatCreatesAFreshPlayer() {
        Game game = new Game();
        game.startNewGame();
        Player previousPlayer = game.getPlayer();
        previousPlayer.move(Direction.DOWN, 1);
        previousPlayer.takeDamage(100);
        game.update(0);
        game.startNewGame();
        assertEquals(GameState.PLAYING, game.getState());
        assertNotSame(previousPlayer, game.getPlayer());
        assertEquals(100, game.getPlayer().getHealth());
        assertEquals(0, game.getPlayer().getX());
        assertEquals(0, game.getPlayer().getY());
        previousPlayer.heal(10);
        assertEquals(100, game.getPlayer().getHealth());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void invalidTimeNeverChangesState(double time) {
        Game game = new Game();
        assertThrows(IllegalArgumentException.class, () -> game.update(time));
        assertEquals(GameState.MENU, game.getState());
        game.startNewGame();
        game.getPlayer().takeDamage(100);
        assertThrows(IllegalArgumentException.class, () -> game.update(time));
        assertEquals(GameState.PLAYING, game.getState());
        game.pause();
        assertThrows(IllegalArgumentException.class, () -> game.update(time));
        assertEquals(GameState.PAUSED, game.getState());
    }

    @Test
    void separateGamesDoNotSharePlayerOrState() {
        Game first = new Game();
        Game second = new Game();
        first.startNewGame();
        assertEquals(GameState.MENU, second.getState());
        assertNull(second.getPlayer());
        second.startNewGame();
        first.getPlayer().takeDamage(100);
        first.update(0);
        assertEquals(GameState.PLAYING, second.getState());
        assertEquals(100, second.getPlayer().getHealth());
    }
}
