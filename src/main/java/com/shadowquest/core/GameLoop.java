package com.shadowquest.core;

import java.util.Objects;
import java.util.function.DoubleConsumer;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;

/** JavaFX frame adapter; callbacks supply update and rendering behavior. */
public final class GameLoop extends AnimationTimer {
    private final FrameClock clock = new FrameClock();
    private final DoubleConsumer update;
    private final Runnable render;
    private boolean running;
    private boolean handlingFrame;

    public GameLoop(DoubleConsumer update, Runnable render) {
        this.update = Objects.requireNonNull(update, "update");
        this.render = Objects.requireNonNull(render, "render");
    }

    @Override
    public void start() {
        requireFxThread();
        if (!running) {
            clock.reset();
            super.start();
            running = true;
        }
    }

    @Override
    public void stop() {
        requireFxThread();
        super.stop();
        running = false;
        clock.reset();
    }

    @Override
    public void handle(long now) {
        if (!running || handlingFrame) {
            return;
        }
        // Native window changes can trigger a nested JavaFX pulse.
        handlingFrame = true;
        try {
            update.accept(clock.tick(now));
            // An update may navigate away and stop this loop.
            if (running) {
                render.run();
            }
        } finally {
            handlingFrame = false;
        }
    }

    public boolean isRunning() {
        return running;
    }

    private static void requireFxThread() {
        if (!Platform.isFxApplicationThread()) {
            throw new IllegalStateException("Game loop lifecycle must run on the JavaFX thread.");
        }
    }
}
