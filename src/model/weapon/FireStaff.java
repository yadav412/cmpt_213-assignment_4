package model.weapon;

import model.Fill;
import model.Opponent;
import java.util.ArrayList;
import java.util.List;

public class FireStaff implements Weapon {
    @Override
    public boolean activates(Fill fill) {
        return fill.getCellCount() >= 15;
    }
    
    @Override
    public String getName() {
        return "Fire Staff";
    }
    
    @Override
    public String getDescription() {
        return "Hits primary target and adjacent opponents if cell count >= 15";
    }
    
    @Override
    public AttackResult applyEffect(Fill fill, Opponent[] opponents, int baseDamage, int targetIndex) {
        List<Integer> targets = new ArrayList<>();
        List<Double> multipliers = new ArrayList<>();
        
        // Primary target
        if (opponents[targetIndex].isAlive()) {
            targets.add(targetIndex);
            multipliers.add(1.0); // 100% damage
        }
        
        // Left side (if exists)
        if (targetIndex > 0 && opponents[targetIndex - 1].isAlive()) {
            targets.add(targetIndex - 1);
            multipliers.add(0.5); // 50% damage
        }
        
        // Right side (if exists)
        if (targetIndex < opponents.length - 1 && opponents[targetIndex + 1].isAlive()) {
            targets.add(targetIndex + 1);
            multipliers.add(0.5); // 50% damage
        }
        
        int[] targetArray = targets.stream().mapToInt(i -> i).toArray();
        double[] multiplierArray = multipliers.stream().mapToDouble(d -> d).toArray();
        
        return new AttackResult(targetArray, multiplierArray, "Fire Staff hits primary target and adjacent opponents.");
    }
}

