package com.xngin.events;

/**
 * Interface for objects that can handle events of a specific type.
 * Event listeners are registered with the EventBus and receive callbacks
 * when events they're interested in are published. This allows for
 * decoupled communication between engine systems.
 *
 * @param <T> the type of event this listener handles
 */
public interface EventListener<T extends Event> {
}
