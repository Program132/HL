package fr.hytale.loader.command;

import com.hypixel.hytale.server.core.command.system.AbstractCommand;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.arguments.system.RequiredArg;
import com.hypixel.hytale.server.core.command.system.arguments.system.OptionalArg;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgTypes;
import com.hypixel.hytale.server.core.command.system.arguments.types.ArgumentType;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Internal command wrapper for HytaleLoader.
 * <p>
 * This class wraps methods annotated with {@link Command} and integrates them
 * with Hytale's native command system. It uses reflection to invoke the
 * command methods when executed, supporting automatic argument parsing.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.7
 * @since 1.0.0
 */
public class SimpleCommand extends AbstractCommand {
    private final Method method;
    private final Object instance;
    private final List<com.hypixel.hytale.server.core.command.system.arguments.system.Argument<?, ?>> arguments = new ArrayList<>();

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

        // Register arguments based on method parameters
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter param = parameters[i];

            // Skip the first parameter if it is CommandContext (it should be)
            if (i == 0 && CommandContext.class.isAssignableFrom(param.getType())) {
                continue;
            }

            // Check for @Arg annotation
            if (param.isAnnotationPresent(Arg.class)) {
                Arg argParams = param.getAnnotation(Arg.class);
                ArgumentType<?> type = getArgType(param.getType());
                if (type != null) {
                    if (argParams.optional()) {
                        OptionalArg<?> optArg = this.withOptionalArg(argParams.name(), argParams.description(), type);
                        arguments.add(optArg);
                    } else {
                        // Required argument
                        RequiredArg<?> reqArg = this.withRequiredArg(argParams.name(), argParams.description(), type);
                        arguments.add(reqArg);
                    }
                }
            }
        }
    }

    private ArgumentType<?> getArgType(Class<?> type) {
        if (type == String.class) {
            return ArgTypes.STRING;
        } else if (type == int.class || type == Integer.class) {
            return ArgTypes.INTEGER;
        } else if (type == float.class || type == Float.class) {
            return ArgTypes.FLOAT;
        } else if (type == boolean.class || type == Boolean.class) {
            return ArgTypes.BOOLEAN;
        } else if (type == double.class || type == Double.class) {
            return ArgTypes.FLOAT; // Best effort for double
        }
        return null;
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

            // Prepare arguments for invocation
            List<Object> invokeArgs = new ArrayList<>();
            // First argument is always context
            if (method.getParameterCount() > 0
                    && CommandContext.class.isAssignableFrom(method.getParameterTypes()[0])) {
                invokeArgs.add(context);
            }

            // Add parsed arguments
            for (int i = 0; i < arguments.size(); i++) {
                com.hypixel.hytale.server.core.command.system.arguments.system.Argument<?, ?> arg = arguments.get(i);
                Object val = context.get(arg);

                // Handle nulls for primitives to avoid NPE during reflection invoke
                if (val == null) {
                    int paramIndex = i + (CommandContext.class.isAssignableFrom(method.getParameterTypes()[0]) ? 1 : 0);
                    Class<?> paramType = method.getParameterTypes()[paramIndex];

                    if (paramType.isPrimitive()) {
                        if (paramType == boolean.class)
                            val = false;
                        else if (paramType == int.class)
                            val = 0;
                        else if (paramType == float.class)
                            val = 0.0f;
                        else if (paramType == double.class)
                            val = 0.0d;
                        else if (paramType == long.class)
                            val = 0L;
                        else if (paramType == byte.class)
                            val = (byte) 0;
                        else if (paramType == short.class)
                            val = (short) 0;
                        else if (paramType == char.class)
                            val = '\u0000';
                    }
                }

                invokeArgs.add(val);
            }

            method.invoke(instance, invokeArgs.toArray());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return CompletableFuture.completedFuture(null);
    }
}
