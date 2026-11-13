package com.shadowquest.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FrameClockTest {
    @Test
    void firstFrameUsesZeroInsteadOfTimeSinceClockOrigin() {
        assertEquals(0, new FrameClock().tick(9_000_000_000L));
    }

    @Test
    void convertsNanosecondsToFractionalSeconds() {
        FrameClock clock = new FrameClock();
        clock.tick(1_000_000_000L);
        assertEquals(0.016, clock.tick(1_016_000_000L), 1e-12);
        assertEquals(0.025, clock.tick(1_041_000_000L), 1e-12);
    }

    @Test
    void equalTimestampsProduceZero() {
        FrameClock clock = new FrameClock();
        clock.tick(0);
        assertEquals(0, clock.tick(0));
    }

    @Test
    void resetExcludesTimeSpentStopped() {
        FrameClock clock = new FrameClock();
        clock.tick(1);
        clock.tick(2);
        clock.reset();
        clock.reset();
        assertEquals(0, clock.tick(50_000_000_000L));
        assertEquals(0.5, clock.tick(50_500_000_000L));
    }

    @Test
    void rejectsBackwardTimeWithoutChangingPreviousTimestamp() {
        FrameClock clock = new FrameClock();
        clock.tick(1_000_000_000L);
        assertThrows(IllegalArgumentException.class, () -> clock.tick(999));
        assertEquals(1, clock.tick(2_000_000_000L));
    }

    @Test
    void timestampsMayHaveNegativeOrigins() {
        FrameClock clock = new FrameClock();
        assertEquals(0, clock.tick(-2_000_000_000L));
        assertEquals(0.25, clock.tick(-1_750_000_000L));
    }
}
