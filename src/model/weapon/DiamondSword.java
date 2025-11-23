package model.weapon;

import model.Fill;
import model.Character;

import java.util.ArrayList;
import java.util.List;

public class DiamondSword implements Weapon {
    @Override
    public boolean activates(Fill fill) {
        return fill.isDescending();
    }
    
    @Override
    public String getName() {
        return "Diamond Sword";
    }
    
    @Override
    public AttackResult applyEffect(Fill fill, Character[] opponents, int baseDamage, int targetIndex) {
        List<Integer> targets = new ArrayList<>();
        List<Double> multipliers = new ArrayList<>();
        
        // Primary target
        targets.add(targetIndex);
        multipliers.add(1.0); // 100% damage
        
        // Left side
        if (targetIndex > 0 && opponents[targetIndex - 1].isAlive()) {
            targets.add(targetIndex - 1);
            multipliers.add(0.75); // 75% damage
        }
        
        // Right side
        if (targetIndex < opponents.length - 1 && opponents[targetIndex + 1].isAlive()) {
            targets.add(targetIndex + 1);
            multipliers.add(0.75); // 75% damage
        }
        
        int[] targetArray = targets.stream().mapToInt(i -> i).toArray();
        double[] multiplierArray = multipliers.stream().mapToDouble(d -> d).toArray();
        
        return new AttackResult(targetArray, multiplierArray, "Diamond Sword hits primary target and adjacent opponents.");
    }
}

