package model.weapon;

import model.Fill;
import model.Opponent;

/**
 * Null Object pattern implementation for when player has no weapon.
 */
public class NoWeapon implements Weapon {
    @Override
    public boolean activates(Fill fill) {
        return false;
    }
    
    @Override
    public String getName() {
        return "None";
    }
    
    @Override
    public AttackResult applyEffect(Fill fill, Opponent[] opponents, int baseDamage, int targetIndex) {
        // No weapon, just target the selected character
        return new AttackResult(new int[]{targetIndex}, new double[]{1.0}, "");
    }
}

