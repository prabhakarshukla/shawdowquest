package com.shadowquest.player;

import com.shadowquest.utils.Direction;

/** A player with initial stats and movement in world coordinates. */
public class Player extends Character {
    private static final double INITIAL_MAX_HEALTH = 100;
    private static final int INITIAL_ATTACK_POWER = 10;
    private static final int INITIAL_DEFENSE = 5;
    private static final double MOVEMENT_SPEED = 200;

    private final double speed;

    /** Creates a full-health player at the world origin. Stats are initial tuning values. */
    public Player(String name) {
        super(name, INITIAL_MAX_HEALTH, INITIAL_ATTACK_POWER, INITIAL_DEFENSE);
        speed = MOVEMENT_SPEED;
    }

    /** Returns movement speed in world units per second. */
    public double getSpeed() {
        return speed;
    }

    /**
     * Moves a living player in one direction by speed multiplied by elapsed seconds.
     * No level boundaries or collisions are applied at this stage.
     *
     * @throws IllegalArgumentException for a null direction, invalid time, or overflow
     */
    public void move(Direction direction, double deltaTime) {
        if (direction == null) {
            throw new IllegalArgumentException("Movement direction must not be null.");
        }
        switch (direction) {
            case UP -> move(0, -1, deltaTime);
            case DOWN -> move(0, 1, deltaTime);
            case LEFT -> move(-1, 0, deltaTime);
            case RIGHT -> move(1, 0, deltaTime);
        }
    }

    /** Moves along digital input axes (-1, 0, 1), normalizing diagonals. */
    public void move(int horizontal, int vertical, double deltaTime) {
        if (horizontal < -1 || horizontal > 1 || vertical < -1 || vertical > 1) {
            throw new IllegalArgumentException("Movement axes must be -1, 0, or 1.");
        }
        if (!Double.isFinite(deltaTime) || deltaTime < 0) {
            throw new IllegalArgumentException("Elapsed time must be finite and non-negative.");
        }
        double distance = speed * deltaTime;
        if (!Double.isFinite(distance)) {
            throw new IllegalArgumentException("Elapsed time is too large for movement.");
        }
        if (!isAlive() || (horizontal == 0 && vertical == 0)) {
            return;
        }
        double length = Math.hypot(horizontal, vertical);
        moveBy(horizontal / length * distance, vertical / length * distance);
    }
}
