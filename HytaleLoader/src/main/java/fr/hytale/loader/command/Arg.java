package fr.hytale.loader.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to define a command argument on a method parameter.
 * <p>
 * Use this locally with {@link Command} to automatically register arguments.
 * </p>
 *
 * @author HytaleLoader
 * @version 1.0.6
 * @since 1.0.6
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface Arg {
    /**
     * The name of the argument.
     * 
     * @return the argument name
     */
    String name();

    /**
     * The description of the argument.
     * 
     * @return the description
     */
    /**
     * The description of the argument.
     * 
     * @return the description
     */
    String description() default "No description";

    /**
     * Whether the argument is optional.
     * If true, the argument will be registered as optional.
     * Note: Optional arguments must be at the end of the argument list.
     * 
     * @return true if optional, false otherwise
     */
    boolean optional() default false;
}
