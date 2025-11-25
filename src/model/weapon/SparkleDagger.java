package model.weapon;

import model.Fill;
import model.Opponent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SparkleDagger implements Weapon {
    @Override
    public boolean activates(Fill fill) {
        return fill.getElapsedTime() < 20000; // Less than 20 seconds
    }
    
    @Override
    public String getName() {
        return "Sparkle Dagger";
    }
    
    @Override
    public String getDescription() {
        return "Targets an additional random opponent at 50% damage if fill completed in < 20 seconds";
    }
    
    @Override
    public AttackResult applyEffect(Fill fill, Opponent[] opponents, int baseDamage, int targetIndex) {
        List<Integer> targets = new ArrayList<>();
        List<Double> multipliers = new ArrayList<>();
        
        // Normal attack
        targets.add(targetIndex);
        multipliers.add(1.0);
        
        // Additional random target at 50%
        Random random = new Random();
        List<Integer> aliveIndices = new ArrayList<>();
        for (int i = 0; i < opponents.length; i++) {
            if (opponents[i].isAlive()) {
                aliveIndices.add(i);
            }
        }
        
        if (!aliveIndices.isEmpty()) {
            int randomTarget = aliveIndices.get(random.nextInt(aliveIndices.size()));
            targets.add(randomTarget);
            multipliers.add(0.5); // 50% damage
        }
        
        int[] targetArray = targets.stream().mapToInt(i -> i).toArray();
        double[] multiplierArray = multipliers.stream().mapToDouble(d -> d).toArray();
        
        return new AttackResult(targetArray, multiplierArray, "Sparkle Dagger targets an additional random opponent.");
    }
}

