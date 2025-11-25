package model.weapon;

import model.Fill;
import model.Opponent;

import java.util.ArrayList;
import java.util.List;

public class FrostBow implements Weapon {
    @Override
    public boolean activates(Fill fill) {
        return fill.isAscending();
    }
    
    @Override
    public String getName() {
        return "Frost Bow";
    }
    
    @Override
    public AttackResult applyEffect(Fill fill, Opponent[] opponents, int baseDamage, int targetIndex) {
        List<Integer> targets = new ArrayList<>();
        List<Double> multipliers = new ArrayList<>();
        
        // Hit all opponents
        for (int i = 0; i < opponents.length; i++) {
            if (opponents[i].isAlive()) {
                targets.add(i);
                multipliers.add(1.0); // 100% damage
            }
        }
        
        int[] targetArray = targets.stream().mapToInt(i -> i).toArray();
        double[] multiplierArray = multipliers.stream().mapToDouble(d -> d).toArray();
        
        return new AttackResult(targetArray, multiplierArray, "Frost Bow hits all opponents.");
    }
}

