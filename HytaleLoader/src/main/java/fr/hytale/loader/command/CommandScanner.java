package fr.hytale.loader.command;

import fr.hytale.loader.plugin.SimplePlugin;
import java.lang.reflect.Method;

/**
 * Utility class for scanning and registering command handlers.
 * <p>
 * This class automatically discovers methods annotated with {@link Command}
 * and registers them as server commands. It handles command properties such as
 * names, aliases, descriptions, and permissions.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.5
 * @since 1.0.0
 * @see Command
 */
public class CommandScanner {

    /**
     * Scans and registers all command methods in the plugin.
     * <p>
     * This is a convenience method that calls
     * {@link #registerCommands(SimplePlugin, Object)}
     * with the plugin as both the plugin and container parameters.
     * </p>
     * 
     * @param plugin the plugin to scan for commands
     */
    public static void scanAndRegister(SimplePlugin plugin) {
        registerCommands(plugin, plugin);
    }

    /**
     * Registers all command methods in a container object.
     * <p>
     * Scans the container for methods annotated with {@link Command} and
     * automatically registers them with the plugin's command registry.
     * </p>
     * 
     * @param plugin    the plugin that owns these commands
     * @param container the object containing command handler methods
     */
    public static void registerCommands(SimplePlugin plugin, Object container) {
        for (Method method : container.getClass().getDeclaredMethods()) {
            Command annotation = method.getAnnotation(Command.class);
            if (annotation == null)
                continue;

            method.setAccessible(true);

            SimpleCommand cmd = new SimpleCommand(
                    annotation.name(),
                    annotation.description(),
                    annotation.requiresConfirmation(),
                    method,
                    container);

            if (!annotation.permission().isEmpty()) {
                cmd.requirePermission(annotation.permission());
            }

            if (annotation.aliases().length > 0) {
                cmd.addAliases(annotation.aliases());
            }

            plugin.getCommandRegistry().registerCommand(cmd);
        }
    }
}
