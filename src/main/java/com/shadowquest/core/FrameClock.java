package com.shadowquest.core;

/** Converts successive frame timestamps from nanoseconds to elapsed seconds. */
public final class FrameClock {
    private long previousTime;
    private boolean hasPreviousTime;

    public double tick(long now) {
        if (!hasPreviousTime) {
            previousTime = now;
            hasPreviousTime = true;
            return 0;
        }
        long elapsed = now - previousTime;
        if (elapsed < 0) {
            throw new IllegalArgumentException("Frame timestamps must advance monotonically.");
        }
        previousTime = now;
        return elapsed / 1_000_000_000.0;
    }

    public void reset() {
        hasPreviousTime = false;
    }
}
