package fr.hytale.loader.internal;

import com.hypixel.hytale.server.core.HytaleServer;
import com.hypixel.hytale.logger.HytaleLogger;

/**
 * Internal dispatcher for ECS (Entity Component System) events.
 * <p>
 * This class receives native Hytale ECS events, wraps them in HytaleLoader
 * event objects,
 * and dispatches them through the HytaleLoader event bus for mods to listen to.
 * </p>
 * 
 * @author HytaleLoader
 * @version 1.0.1
 * @since 1.0.1
 */
public class EcsEventDispatcher {

        /**
         * Handles block break events from the native Hytale ECS.
         * 
         * @param event the native break block event
         */
        public void onBreakBlock(com.hypixel.hytale.server.core.event.events.ecs.BreakBlockEvent event) {
                HytaleLogger.getLogger().at(java.util.logging.Level.INFO)
                                .log("[HytaleLoader] BreakBlockEvent received!");
                fr.hytale.loader.event.types.ecs.BreakBlockEvent newEvent = new fr.hytale.loader.event.types.ecs.BreakBlockEvent(
                                event);
                HytaleServer.get().getEventBus()
                                .dispatchFor(fr.hytale.loader.event.types.ecs.BreakBlockEvent.class, null)
                                .dispatch(newEvent);
        }

        /**
         * Handles block place events from the native Hytale ECS.
         * 
         * @param event the native place block event
         */
        public void onPlaceBlock(com.hypixel.hytale.server.core.event.events.ecs.PlaceBlockEvent event) {
                HytaleLogger.getLogger().at(java.util.logging.Level.INFO)
                                .log("[HytaleLoader] PlaceBlockEvent received!");
                fr.hytale.loader.event.types.ecs.PlaceBlockEvent newEvent = new fr.hytale.loader.event.types.ecs.PlaceBlockEvent(
                                event);
                HytaleServer.get().getEventBus()
                                .dispatchFor(fr.hytale.loader.event.types.ecs.PlaceBlockEvent.class, null)
                                .dispatch(newEvent);
        }

        /**
         * Handles block use/interaction events from the native Hytale ECS.
         * 
         * @param event the native use block event
         */
        public void onUseBlock(com.hypixel.hytale.server.core.event.events.ecs.UseBlockEvent.Pre event) {
                HytaleLogger.getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] UseBlockEvent received!");
                fr.hytale.loader.event.types.ecs.UseBlockEvent newEvent = new fr.hytale.loader.event.types.ecs.UseBlockEvent(
                                event);
                HytaleServer.get().getEventBus().dispatchFor(fr.hytale.loader.event.types.ecs.UseBlockEvent.class, null)
                                .dispatch(newEvent);
        }

        /**
         * Handles block damage events from the native Hytale ECS.
         * 
         * @param event the native damage block event
         */
        public void onDamageBlock(com.hypixel.hytale.server.core.event.events.ecs.DamageBlockEvent event) {
                HytaleLogger.getLogger().at(java.util.logging.Level.INFO)
                                .log("[HytaleLoader] DamageBlockEvent received!");
                fr.hytale.loader.event.types.ecs.DamageBlockEvent newEvent = new fr.hytale.loader.event.types.ecs.DamageBlockEvent(
                                event);
                HytaleServer.get().getEventBus()
                                .dispatchFor(fr.hytale.loader.event.types.ecs.DamageBlockEvent.class, null)
                                .dispatch(newEvent);
        }

        /**
         * Handles item drop events from the native Hytale ECS.
         * 
         * @param event the native drop item event
         */
        public void onDropItem(com.hypixel.hytale.server.core.event.events.ecs.DropItemEvent.Drop event) {
                HytaleLogger.getLogger().at(java.util.logging.Level.INFO).log("[HytaleLoader] DropItemEvent received!");
                fr.hytale.loader.event.types.ecs.DropItemEvent newEvent = new fr.hytale.loader.event.types.ecs.DropItemEvent(
                                event);
                HytaleServer.get().getEventBus().dispatchFor(fr.hytale.loader.event.types.ecs.DropItemEvent.class, null)
                                .dispatch(newEvent);
        }

        /**
         * Handles zone discovery events from the native Hytale ECS.
         * 
         * @param event the native discover zone event
         */
        public void onDiscoverZone(com.hypixel.hytale.server.core.event.events.ecs.DiscoverZoneEvent.Display event) {
                HytaleLogger.getLogger().at(java.util.logging.Level.INFO)
                                .log("[HytaleLoader] DiscoverZoneEvent received!");
                fr.hytale.loader.event.types.ecs.DiscoverZoneEvent newEvent = new fr.hytale.loader.event.types.ecs.DiscoverZoneEvent(
                                event);
                HytaleServer.get().getEventBus()
                                .dispatchFor(fr.hytale.loader.event.types.ecs.DiscoverZoneEvent.class, null)
                                .dispatch(newEvent);
        }

        /**
         * Handles crafting recipe events from the native Hytale ECS.
         * 
         * @param event the native craft recipe event
         */
        public void onCraftRecipe(com.hypixel.hytale.server.core.event.events.ecs.CraftRecipeEvent.Pre event) {
                HytaleLogger.getLogger().at(java.util.logging.Level.INFO)
                                .log("[HytaleLoader] CraftRecipeEvent received!");
                fr.hytale.loader.event.types.ecs.CraftRecipeEvent newEvent = new fr.hytale.loader.event.types.ecs.CraftRecipeEvent(
                                event);
                HytaleServer.get().getEventBus()
                                .dispatchFor(fr.hytale.loader.event.types.ecs.CraftRecipeEvent.class, null)
                                .dispatch(newEvent);
        }
}
