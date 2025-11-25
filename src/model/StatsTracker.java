package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks game statistics using the Observer pattern.
 */
public class StatsTracker implements GameObserver {
    private Map<String, Integer> weaponActivations;
    private Map<String, Integer> ringActivations;
    private int matchesWon;
    private int matchesLost;
    private int totalDamageDone;
    private int totalDamageReceived;
    private int fillsCompleted;

    public StatsTracker() {
        this.weaponActivations = new HashMap<>();
        this.ringActivations = new HashMap<>();
        this.matchesWon = 0;
        this.matchesLost = 0;
        this.totalDamageDone = 0;
        this.totalDamageReceived = 0;
        this.fillsCompleted = 0;
    }

    @Override
    public void notify(GameEvent event) {
        switch (event.getType()) {
            case EQUIPMENT_ACTIVATED:
                EquipmentActivationData data = (EquipmentActivationData) event.getData();
                if (data.isWeapon) {
                    weaponActivations.put(data.name, weaponActivations.getOrDefault(data.name, 0) + 1);
                } else {
                    ringActivations.put(data.name, ringActivations.getOrDefault(data.name, 0) + 1);
                }
                break;
            case MATCH_WON:
                matchesWon++;
                break;
            case MATCH_LOST:
                matchesLost++;
                break;
            case CHARACTER_DAMAGED:
                DamageData damageData = (DamageData) event.getData();
                if (damageData.isPlayer) {
                    totalDamageReceived += damageData.amount;
                } else {
                    totalDamageDone += damageData.amount;
                }
                break;
            case FILL_COMPLETED:
                fillsCompleted++;
                break;
            case FILL_STARTED:
            case CELL_ADDED_TO_FILL:
            case ATTACK_PERFORMED:
            case CHARACTER_KILLED:
            case TURN_FAILED:
            case OPPONENT_ATTACKED:
                // These events don't need to be tracked in statistics
                break;
        }
    }

    public Map<String, Integer> getWeaponActivations() {
        return new HashMap<>(weaponActivations);
    }

    public Map<String, Integer> getRingActivations() {
        return new HashMap<>(ringActivations);
    }

    public int getMatchesWon() {
        return matchesWon;
    }

    public int getMatchesLost() {
        return matchesLost;
    }

    public int getTotalDamageDone() {
        return totalDamageDone;
    }

    public int getTotalDamageReceived() {
        return totalDamageReceived;
    }

    public int getFillsCompleted() {
        return fillsCompleted;
    }

    // Helper classes for event data
    public static class EquipmentActivationData {
        public final String name;
        public final boolean isWeapon;

        public EquipmentActivationData(String name, boolean isWeapon) {
            this.name = name;
            this.isWeapon = isWeapon;
        }
    }

    public static class DamageData {
        public final int amount;
        public final boolean isPlayer;

        public DamageData(int amount, boolean isPlayer) {
            this.amount = amount;
            this.isPlayer = isPlayer;
        }
    }
}
