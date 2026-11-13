package com.shadowquest.core;

import com.shadowquest.player.Player;

/** Coordinates one session without depending on JavaFX or running a game loop. */
public class Game {
    private GameState state = GameState.MENU;
    private Player player;

    public GameState getState() {
        return state;
    }

    /** Returns the session's player, or null before the first game starts. */
    public Player getPlayer() {
        return player;
    }

    /** Starts from the menu or restarts after defeat; other states are unchanged. */
    public void startNewGame() {
        if (state == GameState.MENU || state == GameState.GAME_OVER) {
            player = new Player("Arin");
            state = GameState.PLAYING;
        }
    }

    /** Pauses an active session. Calls in other states have no effect. */
    public void pause() {
        if (state == GameState.PLAYING) {
            state = GameState.PAUSED;
        }
    }

    /** Resumes a paused session. Calls in other states have no effect. */
    public void resume() {
        if (state == GameState.PAUSED) {
            state = GameState.PLAYING;
        }
    }

    /**
     * Checks for player defeat while playing. A future loop will call this method.
     *
     * @param deltaTime elapsed seconds, finite and non-negative
     * @throws IllegalArgumentException if elapsed time is invalid
     */
    public void update(double deltaTime) {
        if (!Double.isFinite(deltaTime) || deltaTime < 0) {
            throw new IllegalArgumentException("Elapsed time must be finite and non-negative.");
        }
        if (state == GameState.PLAYING && !player.isAlive()) {
            state = GameState.GAME_OVER;
        }
    }
}
