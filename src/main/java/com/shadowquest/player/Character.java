package com.shadowquest.player;

/** Shared character data and health rules, independent of presentation and combat. */
public abstract class Character {
    private final String name;
    private final double maxHealth;
    private final int attackPower;
    private final int defense;
    private double health;
    private double x;
    private double y;

    /** Creates a character at (0, 0) with full health and non-negative stats. */
    protected Character(String name, double maxHealth, int attackPower, int defense) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Character name must not be null or blank.");
        }
        if (!Double.isFinite(maxHealth) || maxHealth <= 0) {
            throw new IllegalArgumentException("Maximum health must be finite and positive.");
        }
        if (attackPower < 0 || defense < 0) {
            throw new IllegalArgumentException("Attack power and defense must be non-negative.");
        }
        this.name = name.strip();
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.attackPower = attackPower;
        this.defense = defense;
    }

    public String getName() {
        return name;
    }

    public double getHealth() {
        return health;
    }

    public double getMaxHealth() {
        return maxHealth;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public int getDefense() {
        return defense;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isAlive() {
        return health > 0;
    }

    /** Applies a validated health reduction, clamped at zero; no combat formula yet. */
    public void takeDamage(double amount) {
        validateHealthAmount(amount);
        health = Math.max(0, health - amount);
    }

    /** Restores health up to the maximum, including from zero; does not change game state. */
    public void heal(double amount) {
        validateHealthAmount(amount);
        // Limit the addition first so even large valid amounts cannot overflow.
        health += Math.min(amount, maxHealth - health);
    }

    /** Allows subclasses to move while keeping position fields private and finite. */
    protected final void moveBy(double deltaX, double deltaY) {
        double nextX = x + deltaX;
        double nextY = y + deltaY;
        if (!Double.isFinite(nextX) || !Double.isFinite(nextY)) {
            throw new IllegalArgumentException("Movement must produce finite coordinates.");
        }
        // Validate both coordinates before changing either one.
        x = nextX;
        y = nextY;
    }

    private static void validateHealthAmount(double amount) {
        if (!Double.isFinite(amount) || amount < 0) {
            throw new IllegalArgumentException("Health amount must be finite and non-negative.");
        }
    }
}
