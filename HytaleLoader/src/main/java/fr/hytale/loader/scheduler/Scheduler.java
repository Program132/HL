package fr.hytale.loader.scheduler;

import java.util.concurrent.*;

/**
 * HytaleLoader task scheduler for executing tasks synchronously and
 * asynchronously.
 * <p>
 * This scheduler provides methods to run tasks immediately, after a delay, or
 * repeatedly.
 * Tasks are executed using a thread pool executor.
 * </p>
 * 
 * <h2>Usage Examples:</h2>
 * 
 * <pre>{@code
 * Scheduler scheduler = new Scheduler();
 * 
 * // Run immediately
 * scheduler.runTask(() -> {
 *     System.out.println("Immediate task");
 * });
 * 
 * // Run after 5 seconds
 * scheduler.runTaskLater(() -> {
 *     System.out.println("Delayed task");
 * }, 5000);
 * 
 * // Run every 1 second, starting after 2 seconds
 * ScheduledTask task = scheduler.runTaskTimer(() -> {
 *     System.out.println("Repeating task");
 * }, 2000, 1000);
 * 
 * // Cancel the repeating task later
 * task.cancel();
 * }</pre>
 * 
 * @author HytaleLoader
 * @version 1.0.3
 * @since 1.0.3
 */
public class Scheduler {

    private final ScheduledExecutorService executor;
    private final ExecutorService asyncExecutor;

    /**
     * Creates a new scheduler with default thread pool sizes.
     */
    public Scheduler() {
        this(4, 8);
    }

    /**
     * Creates a new scheduler with custom thread pool sizes.
     * 
     * @param corePoolSize  the number of threads to keep in the pool
     * @param asyncPoolSize the number of threads for async tasks
     */
    public Scheduler(int corePoolSize, int asyncPoolSize) {
        this.executor = Executors.newScheduledThreadPool(corePoolSize, new ThreadFactory() {
            private int threadId = 0;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "HytaleLoader-Scheduler-" + threadId++);
                thread.setDaemon(true);
                return thread;
            }
        });

        this.asyncExecutor = Executors.newFixedThreadPool(asyncPoolSize, new ThreadFactory() {
            private int threadId = 0;

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r, "HytaleLoader-Async-" + threadId++);
                thread.setDaemon(true);
                return thread;
            }
        });
    }

    /**
     * Runs a task immediately (on the next available thread).
     * 
     * @param task the task to run
     * @return a ScheduledTask that can be used to cancel the task
     */
    public ScheduledTask runTask(Runnable task) {
        ScheduledFuture<?> future = executor.schedule(task, 0, TimeUnit.MILLISECONDS);
        return new ScheduledTask(future);
    }

    /**
     * Runs a task after a specified delay.
     * 
     * @param task        the task to run
     * @param delayMillis the delay in milliseconds before running the task
     * @return a ScheduledTask that can be used to cancel the task
     */
    public ScheduledTask runTaskLater(Runnable task, long delayMillis) {
        ScheduledFuture<?> future = executor.schedule(task, delayMillis, TimeUnit.MILLISECONDS);
        return new ScheduledTask(future);
    }

    /**
     * Runs a task repeatedly with a fixed delay between executions.
     * 
     * @param task               the task to run
     * @param initialDelayMillis the delay before the first execution
     * @param periodMillis       the period between successive executions
     * @return a ScheduledTask that can be used to cancel the task
     */
    public ScheduledTask runTaskTimer(Runnable task, long initialDelayMillis, long periodMillis) {
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, initialDelayMillis, periodMillis,
                TimeUnit.MILLISECONDS);
        return new ScheduledTask(future);
    }

    /**
     * Runs a task asynchronously on a separate thread pool.
     * <p>
     * Async tasks do not block the main server thread and are useful for
     * I/O operations, database queries, or other long-running operations.
     * </p>
     * 
     * @param task the task to run asynchronously
     * @return a CompletableFuture representing the async task
     */
    public CompletableFuture<Void> runTaskAsync(Runnable task) {
        return CompletableFuture.runAsync(task, asyncExecutor);
    }

    /**
     * Runs a task asynchronously and returns a result.
     * 
     * @param <T>  the type of the result
     * @param task the task to run
     * @return a CompletableFuture representing the async task result
     */
    public <T> CompletableFuture<T> runTaskAsync(Callable<T> task) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }, asyncExecutor);
    }

    /**
     * Cancels all scheduled tasks and shuts down the scheduler.
     * <p>
     * This should be called when the plugin is disabled.
     * </p>
     */
    public void shutdown() {
        executor.shutdown();
        asyncExecutor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
            if (!asyncExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                asyncExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            asyncExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Checks if the scheduler has been shut down.
     * 
     * @return true if the scheduler is shut down
     */
    public boolean isShutdown() {
        return executor.isShutdown() && asyncExecutor.isShutdown();
    }
}
