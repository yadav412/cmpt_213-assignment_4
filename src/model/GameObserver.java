package model;

/**
 * Observer interface for game events.
 * Implementations can register with the game model to receive notifications.
 */
public interface GameObserver {
    void notify(GameEvent event);
}

