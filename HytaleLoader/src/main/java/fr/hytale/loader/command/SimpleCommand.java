package fr.hytale.loader.command;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

/**
 * Internal command wrapper for HytaleLoader.
 * <p>
 * This class wraps methods annotated with {@link Command} and integrates them
 * with Hytale's native command system. It uses reflection to invoke the
 * command methods when executed.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.3
 * @since 1.0.0
 */
public class SimpleCommand extends AbstractCommand {
    private final Method method;
    private final Object instance;

    /**
     * Constructs a new SimpleCommand.
     * 
     * @param name                 the command name
     * @param description          the command description
     * @param requiresConfirmation whether the command requires confirmation
     * @param method               the method to invoke when the command is executed
     * @param instance             the object instance containing the method
     */
    public SimpleCommand(String name, String description, boolean requiresConfirmation, Method method,
            Object instance) {
        super(name, description, requiresConfirmation);
        this.method = method;
        this.instance = instance;
    }

    /**
     * Executes the command by invoking the wrapped method.
     * 
     * @param context the command execution context
     * @return a completed future
     */
    @Override
    public CompletableFuture<Void> execute(CommandContext context) {
        try {
            method.setAccessible(true);
            method.invoke(instance, context);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }
}
