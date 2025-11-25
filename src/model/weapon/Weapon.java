package model.weapon;

import model.Fill;
import model.Opponent;

/**
 * Base interface for weapons.
 * Uses Null Object pattern - NoWeapon implements this.
 */
public interface Weapon {
    // Determines if this weapon activates based on the fill properties.
    boolean activates(Fill fill);

    String getName();
    
    String getDescription();
    
    // Applies weapon effects to determine attack targets and damage multipliers.
    // Returns an AttackResult describing the effects.
    AttackResult applyEffect(Fill fill, Opponent[] opponents, int baseDamage, int targetIndex);
    
    // Attack result describing weapon effects.
    class AttackResult {
        public final int[] targetIndices;
        public final double[] damageMultipliers;
        public final String description;
        
        public AttackResult(int[] targetIndices, double[] damageMultipliers, String description) {
            this.targetIndices = targetIndices;
            this.damageMultipliers = damageMultipliers;
            this.description = description;
        }
    }
}

