package com.shadowquest.core;

/** The finite set of session states; victory rules are a later milestone. */
public enum GameState {
    MENU,
    PLAYING,
    PAUSED,
    GAME_OVER,
    VICTORY
}
