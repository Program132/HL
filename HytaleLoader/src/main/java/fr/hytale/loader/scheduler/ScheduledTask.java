package fr.hytale.loader.scheduler;

import java.util.concurrent.ScheduledFuture;

/**
 * Represents a scheduled task that can be cancelled.
 * <p>
 * This class wraps a {@link ScheduledFuture} and provides simplified methods
 * to check status and cancel the task.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.3
 * @since 1.0.3
 */
public class ScheduledTask {

    private final ScheduledFuture<?> future;

    /**
     * Creates a new ScheduledTask wrapper.
     * 
     * @param future the underlying ScheduledFuture
     */
    public ScheduledTask(ScheduledFuture<?> future) {
        this.future = future;
    }

    /**
     * Cancels this scheduled task.
     * <p>
     * If the task has not yet started, it will be prevented from running.
     * If the task is currently running, it may be interrupted.
     * </p>
     * 
     * @return true if the task was cancelled, false if it was already completed or
     *         cancelled
     */
    public boolean cancel() {
        return future.cancel(false);
    }

    /**
     * Cancels this scheduled task and attempts to interrupt if it's running.
     * 
     * @return true if the task was cancelled, false if it was already completed or
     *         cancelled
     */
    public boolean cancelAndInterrupt() {
        return future.cancel(true);
    }

    /**
     * Checks if this task has been cancelled.
     * 
     * @return true if the task was cancelled before completion
     */
    public boolean isCancelled() {
        return future.isCancelled();
    }

    /**
     * Checks if this task has completed.
     * <p>
     * Completion may be due to normal termination, an exception, or cancellation.
     * </p>
     * 
     * @return true if the task completed
     */
    public boolean isDone() {
        return future.isDone();
    }

    /**
     * Checks if this task is still running or scheduled to run.
     * 
     * @return true if the task is active
     */
    public boolean isActive() {
        return !future.isDone() && !future.isCancelled();
    }

    /**
     * Gets the underlying ScheduledFuture.
     * 
     * @return the wrapped ScheduledFuture
     */
    public ScheduledFuture<?> getFuture() {
        return future;
    }
}
