package com.shadowquest.player;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class CharacterTest {
    // A concrete test character exercises the shared rules without adding gameplay.
    private static class TestCharacter extends Character {
        TestCharacter(String name, double maxHealth, int attackPower, int defense) {
            super(name, maxHealth, attackPower, defense);
        }
    }

    @Test
    void initializesSharedPropertiesAndTrimsName() {
        Character character = new TestCharacter("  Arin  ", 80, 12, 4);
        assertEquals("Arin", character.getName());
        assertEquals(80, character.getMaxHealth());
        assertEquals(80, character.getHealth());
        assertEquals(12, character.getAttackPower());
        assertEquals(4, character.getDefense());
        assertEquals(0, character.getX());
        assertEquals(0, character.getY());
        assertTrue(character.isAlive());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t\n"})
    void rejectsMissingNames(String name) {
        assertThrows(IllegalArgumentException.class,
                () -> new TestCharacter(name, 100, 10, 5));
    }

    @ParameterizedTest
    @ValueSource(doubles = {0, -1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void rejectsInvalidMaximumHealth(double maxHealth) {
        assertThrows(IllegalArgumentException.class,
                () -> new TestCharacter("Arin", maxHealth, 10, 5));
    }

    @ParameterizedTest
    @CsvSource({"-1, 5", "10, -1"})
    void rejectsNegativeStats(int attack, int defense) {
        assertThrows(IllegalArgumentException.class,
                () -> new TestCharacter("Arin", 100, attack, defense));
    }

    @Test
    void acceptsZeroAttackAndDefense() {
        Character character = new TestCharacter("Arin", 100, 0, 0);
        assertEquals(0, character.getAttackPower());
        assertEquals(0, character.getDefense());
    }

    @Test
    void damageAndHealingChangeHealthWithoutApplyingCombatStats() {
        Character character = new TestCharacter("Arin", 100, 10, 5);
        character.takeDamage(30.5);
        assertEquals(69.5, character.getHealth());
        character.heal(10.25);
        assertEquals(79.75, character.getHealth());
        assertTrue(character.isAlive());
    }

    @Test
    void healthNeverExceedsMaximum() {
        Character character = new TestCharacter("Arin", 100, 10, 5);
        character.takeDamage(20);
        character.heal(Double.MAX_VALUE);
        character.heal(1);
        assertEquals(100, character.getHealth());
    }

    @Test
    void healingLargeHealthValuesDoesNotOverflow() {
        Character character = new TestCharacter("Arin", Double.MAX_VALUE, 0, 0);
        character.takeDamage(Double.MAX_VALUE / 2);
        character.heal(Double.MAX_VALUE);
        assertEquals(Double.MAX_VALUE, character.getHealth());
        assertTrue(Double.isFinite(character.getHealth()));
    }

    @Test
    void zeroAmountsLeaveHealthUnchanged() {
        Character character = new TestCharacter("Arin", 100, 10, 5);
        character.takeDamage(20);
        character.takeDamage(0);
        character.heal(0);
        assertEquals(80, character.getHealth());
    }

    @ParameterizedTest
    @ValueSource(doubles = {-1, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void invalidHealthAmountsAreRejectedWithoutMutation(double amount) {
        Character character = new TestCharacter("Arin", 100, 10, 5);
        character.takeDamage(20);
        assertThrows(IllegalArgumentException.class, () -> character.takeDamage(amount));
        assertEquals(80, character.getHealth());
        assertThrows(IllegalArgumentException.class, () -> character.heal(amount));
        assertEquals(80, character.getHealth());
    }

    @ParameterizedTest
    @ValueSource(doubles = {100, 101, Double.MAX_VALUE})
    void lethalDamageClampsAtZeroAndMarksCharacterDead(double damage) {
        Character character = new TestCharacter("Arin", 100, 10, 5);
        character.takeDamage(damage);
        character.takeDamage(5);
        assertEquals(0, character.getHealth());
        assertFalse(character.isAlive());
    }

    @Test
    void healingFromZeroRestoresHealth() {
        Character character = new TestCharacter("Arin", 100, 10, 5);
        character.takeDamage(100);
        character.heal(10);
        assertEquals(10, character.getHealth());
        assertTrue(character.isAlive());
    }

    @ParameterizedTest
    @ValueSource(doubles = {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY})
    void invalidPositionChangesLeaveBothCoordinatesUnchanged(double invalid) {
        Character character = new TestCharacter("Arin", 100, 10, 5);
        character.moveBy(10, 20);
        assertThrows(IllegalArgumentException.class, () -> character.moveBy(5, invalid));
        assertThrows(IllegalArgumentException.class, () -> character.moveBy(invalid, 5));
        assertEquals(10, character.getX());
        assertEquals(20, character.getY());
    }

    @Test
    void positionOverflowLeavesBothCoordinatesUnchanged() {
        Character character = new TestCharacter("Arin", 100, 10, 5);
        character.moveBy(Double.MAX_VALUE, 20);
        assertThrows(IllegalArgumentException.class,
                () -> character.moveBy(Double.MAX_VALUE, 5));
        assertEquals(Double.MAX_VALUE, character.getX());
        assertEquals(20, character.getY());
    }
}
