package fr.hytale.loader.command;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Command {
    String name();

    String description() default "";

    String[] aliases() default {};

    String permission() default "";

    boolean requiresConfirmation() default false;
}
