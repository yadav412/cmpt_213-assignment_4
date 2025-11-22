package model;

/**
 * Represents a game event that can be observed.
 * Events contain structured information about what happened in the game.
 */
public class GameEvent {
    public enum EventType {
        FILL_STARTED,
        CELL_ADDED_TO_FILL,
        FILL_COMPLETED,
        ATTACK_PERFORMED,
        CHARACTER_DAMAGED,
        CHARACTER_KILLED,
        MATCH_WON,
        MATCH_LOST,
        TURN_FAILED,
        OPPONENT_ATTACKED,
        EQUIPMENT_ACTIVATED
    }

    private final EventType type;
    private final String source;
    private final Object data;

    public GameEvent(EventType type, String source, Object data) {
        this.type = type;
        this.source = source;
        this.data = data;
    }

    public EventType getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public Object getData() {
        return data;
    }
}

