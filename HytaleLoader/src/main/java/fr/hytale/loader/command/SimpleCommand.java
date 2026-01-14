package fr.hytale.loader.command;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;

public class SimpleCommand extends AbstractCommand {
    private final Method method;
    private final Object instance;

    public SimpleCommand(String name, String description, boolean requiresConfirmation, Method method,
            Object instance) {
        super(name, description, requiresConfirmation);
        this.method = method;
        this.instance = instance;
    }

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
