package model;

import java.util.List;

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

    // Data for ATTACK_PERFORMED events.
    public static class AttackData {
        public final int strength;
        public final List<String> activeRings;
        public final String weaponName;
        public final boolean weaponActivated;
        public final String weaponDescription;
        public final List<TargetDamage> targets;

        public AttackData(int strength, List<String> activeRings,
                String weaponName, boolean weaponActivated,
                String weaponDescription, List<TargetDamage> targets) {
            this.strength = strength;
            this.activeRings = activeRings;
            this.weaponName = weaponName;
            this.weaponActivated = weaponActivated;
            this.weaponDescription = weaponDescription;
            this.targets = targets;
        }
    }

    // Information about damage dealt to a single target.
    public static class TargetDamage {
        public final int targetIndex; // 0=left, 1=middle, 2=right
        public final int damage;
        public final boolean killed;
        public final boolean missed; // true if target was already dead

        public TargetDamage(int targetIndex, int damage, boolean killed, boolean missed) {
            this.targetIndex = targetIndex;
            this.damage = damage;
            this.killed = killed;
            this.missed = missed;
        }

        // Convenience constructor for hits
        public TargetDamage(int targetIndex, int damage, boolean killed) {
            this(targetIndex, damage, killed, false);
        }
    }

    // Data for CELL_ADDED_TO_FILL events.
    public static class CellAddedData {
        public final int row;
        public final int col;
        public final int value;
        public final int currentStrength;

        public CellAddedData(int row, int col, int value, int currentStrength) {
            this.row = row;
            this.col = col;
            this.value = value;
            this.currentStrength = currentStrength;
        }
    }

    // Data for OPPONENT_ATTACKED events.
    public static class OpponentAttackData {
        public final int attackerIndex;
        public final int damage;

        public OpponentAttackData(int attackerIndex, int damage) {
            this.attackerIndex = attackerIndex;
            this.damage = damage;
        }
    }
}
