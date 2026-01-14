package fr.hytale.loader.command;

import fr.hytale.loader.plugin.SimplePlugin;
import java.lang.reflect.Method;

public class CommandScanner {

    public static void scanAndRegister(SimplePlugin plugin) {
        registerCommands(plugin, plugin);
    }

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
