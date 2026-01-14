package fr.hytale.loader.event;

import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.IBaseEvent;
import com.hypixel.hytale.event.IAsyncEvent;
import fr.hytale.loader.plugin.SimplePlugin;
import java.lang.reflect.Method;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public class EventScanner {

    public static void registerListeners(SimplePlugin plugin, SimpleListener listener) {
        EventRegistry registry = plugin.getEventRegistry();

        for (Method method : listener.getClass().getDeclaredMethods()) {
            EventHandler annotation = method.getAnnotation(EventHandler.class);
            if (annotation == null || method.isBridge() || method.isSynthetic()) {
                continue;
            }

            method.setAccessible(true);

            if (method.getParameterCount() != 1) {
                plugin.getLogger().at(java.util.logging.Level.WARNING).log("Method " + method.getName() + " in "
                        + listener.getClass().getName()
                        + " has @EventHandler but invalid parameter count. Must have exactly 1 event parameter.");
                continue;
            }

            Class<?> eventType = method.getParameterTypes()[0];

            if (!IBaseEvent.class.isAssignableFrom(eventType)) {
                plugin.getLogger().at(java.util.logging.Level.WARNING)
                        .log("Method " + method.getName() + " has parameter " + eventType.getName()
                                + " which is not an IBaseEvent.");
                continue;
            }

            EventPriority priority = annotation.priority();

            if (IAsyncEvent.class.isAssignableFrom(eventType)) {
                registerAsync(registry, priority, (Class<? extends IAsyncEvent>) eventType, listener, method);
            } else {
                registerSync(registry, priority, (Class<? extends IBaseEvent>) eventType, listener, method);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private static void registerSync(EventRegistry registry, EventPriority priority,
            Class<? extends IBaseEvent> eventClass, SimpleListener listener, Method method) {
        Consumer consumer = event -> {
            try {
                method.invoke(listener, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        registry.register(priority, (Class) eventClass, consumer);
    }

    @SuppressWarnings("unchecked")
    private static void registerAsync(EventRegistry registry, EventPriority priority,
            Class<? extends IAsyncEvent> eventClass, SimpleListener listener, Method method) {
        Function function = future -> ((CompletableFuture) future).thenApply(event -> {
            try {
                method.invoke(listener, event);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return event;
        });

        registry.registerAsync(priority, (Class) eventClass, function);
    }

}
