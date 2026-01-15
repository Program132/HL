package fr.hytale.loader.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a method as a command handler.
 * <p>
 * Methods annotated with {@code @Command} will automatically be registered
 * as server commands when the plugin is enabled. The method must accept a
 * {@link com.hypixel.hytale.server.core.command.system.CommandContext}
 * parameter.
 * </p>
 * 
 * <h2>Usage Example:</h2>
 * 
 * <pre>
 * {@code
 * &#64;Command(name = "hello", description = "Greets the player")
 * public void onHello(CommandContext ctx) {
 *     ctx.sender().sendMessage(Message.raw("Hello!"));
 * }
 * 
 * @Command(name = "teleport", aliases = { "tp",
 *         "warp" }, permission = "myplugin.teleport", description = "Teleports a player")
 * public void onTeleport(CommandContext ctx) {
 *     // Teleport logic
 * }
 * }
 * </pre>
 * 
 * @author HytaleLoader
 * @version 1.0.2
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    /**
     * The name of the command.
     * <p>
     * This is the primary way players will invoke the command.
     * Example: {@code name = "hello"} creates the {@code /hello} command.
     * </p>
     * 
     * @return the command name
     */
    String name();

    /**
     * A human-readable description of what the command does.
     * <p>
     * This description may be shown in help menus or command listings.
     * </p>
     * 
     * @return the command description
     */
    String description() default "";

    /**
     * Alternative names for this command.
     * <p>
     * Players can use any of these aliases to invoke the same command.
     * Example: {@code aliases = {"tp", "warp"}} allows {@code /tp} and
     * {@code /warp}.
     * </p>
     * 
     * @return array of command aliases
     */
    String[] aliases() default {};

    /**
     * The permission required to execute this command.
     * <p>
     * If specified, only players with this permission can use the command.
     * If empty, the command is available to all players.
     * </p>
     * 
     * @return the required permission node
     */
    String permission() default "";

    /**
     * Whether this command requires user confirmation before execution.
     * <p>
     * Useful for dangerous or irreversible operations.
     * </p>
     * 
     * @return {@code true} if confirmation is required, {@code false} otherwise
     */
    boolean requiresConfirmation() default false;
}
